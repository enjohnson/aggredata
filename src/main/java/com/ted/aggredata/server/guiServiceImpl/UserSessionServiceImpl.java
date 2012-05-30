/*
 * Copyright (c) 2012. The Energy Detective. All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ted.aggredata.server.guiServiceImpl;

import com.ted.aggredata.client.guiService.UserSessionService;
import com.ted.aggredata.model.CustomFieldInfo;
import com.ted.aggredata.model.GlobalPlaceholder;
import com.ted.aggredata.model.ServerInfo;
import com.ted.aggredata.model.User;
import com.ted.aggredata.server.services.EmailService;
import com.ted.aggredata.server.services.GatewayService;
import com.ted.aggredata.server.services.GroupService;
import com.ted.aggredata.server.services.UserService;
import nl.captcha.Captcha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpSession;

public class UserSessionServiceImpl extends SpringRemoteServiceServlet implements UserSessionService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    ServerInfo serverInfo;

    @Autowired
    CustomFieldInfo usersCustomFields;

    @Autowired
    CustomFieldInfo gatewaysCustomFields;

    @Autowired
    CustomFieldInfo groupsCustomFields;

    @Autowired
    EmailService emailService;


    @Autowired
    GatewayService gatewayService;

    @Autowired
    GroupService groupService;

    Logger logger = LoggerFactory.getLogger(UserSessionServiceImpl.class);
    public static final String USER_SESSION_KEY = "AGGREDATA_USER";

    @Override
    public GlobalPlaceholder logon(String username, String password) {
        if (logger.isInfoEnabled()) logger.info("Processing logon request for " + username);
        getThreadLocalRequest().getSession().removeAttribute(USER_SESSION_KEY);

        try {
            Authentication request = new UsernamePasswordAuthenticationToken(username, password);

            if (authenticationManager == null) logger.error("AuthenticationManager is null");
            Authentication result = authenticationManager.authenticate(request);

            SecurityContextHolder.getContext().setAuthentication(result);
        } catch (AuthenticationException e) {
            logger.warn("Authentication failed: " + e.getMessage());
            return null;
        }

        if (logger.isInfoEnabled()) logger.info("Authentication success: " + SecurityContextHolder.getContext().getAuthentication());
        User user = userService.getUserByUserName(username);
        if (user.getAccountState() != User.STATE_ENABLED) {
            if (logger.isWarnEnabled()) logger.info("User account disabled: " + user);
            logoff();
            return null;
        }

        getThreadLocalRequest().getSession().setAttribute(USER_SESSION_KEY, user);
        return loadGlobal(user);
    }

    @Override
    public void logoff() {

        if (logger.isInfoEnabled()) {
            User user = (User) getThreadLocalRequest().getSession().getAttribute(USER_SESSION_KEY);
            logger.info("Processing logoff request for " + user);
        }
        //Make sure the user object is cleared from session
        getThreadLocalRequest().getSession().removeAttribute(USER_SESSION_KEY);
        //Invalidate the spring security session
        SecurityContextHolder.getContext().setAuthentication(null);
        //Invalidate the user session
        getThreadLocalRequest().getSession().invalidate();
    }


    @Override
    public GlobalPlaceholder getUserFromSession() {
        GlobalPlaceholder globalPlaceholder = new GlobalPlaceholder();
        globalPlaceholder.setServerInfo(serverInfo);
        globalPlaceholder.setUserCustomFields(usersCustomFields);

        //Check to make sure the user has a valid spring securtity session
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.info("User not authenticated");
            return globalPlaceholder;
        }

        //Grab the user object from the session
        if (logger.isInfoEnabled()) logger.info("Looking up user session");
        User user = (User) getThreadLocalRequest().getSession().getAttribute(USER_SESSION_KEY);

        if (user == null) {
            logger.info("No user found in session");
            return globalPlaceholder;

        }

        if (logger.isDebugEnabled()) logger.info("Found user object for: " + user);


        return loadGlobal(user);
    }

    @Override
    public void resetPassword(String username) {
        logger.info("Resetting password request set for " + username);
        User user = userService.getUserByUserName(username);
        if (user == null) {
            logger.warn("User" + username + " not registered in system");
            return;
        }
        emailService.sendResetPassword(user);


    }

    @Override
    public int validateCaptcha(String captchaString, String username, String password, User user) {
        if (!serverInfo.isAllowRegistration()) return  UserSessionService.RESULT_REGISTRATION_DISABLED;

        //Check the captcha
        HttpSession session = getThreadLocalRequest().getSession();
        if (serverInfo.isUseCaptcha()) {
            Captcha captcha = (Captcha) session.getAttribute(Captcha.NAME);
            if (!captcha.isCorrect(captchaString)) return UserSessionService.RESULT_FAIL_CAPTCHA;
        }

        //Check dupe email
        User dupeUser = userService.getUserByUserName(user.getUsername());
        //Check to make sure the user doesn't exist and hasn't already been activated.
        if (dupeUser != null && dupeUser.getAccountState() != User.STATE_WAITING_ACTIVATION) return UserSessionService.RESULT_DUPE_USERNAME;

        logger.info("Creating new user " + user);
        if (user.getRole() == null) user.setRole(User.ROLE_USER);
        User savedUser = userService.createUser(user, User.STATE_WAITING_ACTIVATION);
        userService.changePassword(savedUser, password);

        //Send the activation
        emailService.sendActivationEmail(savedUser);
        return UserSessionService.RESULT_SUCCESS;
    }


    private GlobalPlaceholder loadGlobal(User user) {
        if (logger.isDebugEnabled()) logger.debug("Loading Globals for " + user);
        GlobalPlaceholder globalPlaceholder = new GlobalPlaceholder();
        globalPlaceholder.setSessionUser(user);
        globalPlaceholder.setServerInfo(serverInfo);
        globalPlaceholder.setUserCustomFields(usersCustomFields);
        globalPlaceholder.setGatewayCustomFields(gatewaysCustomFields);
        globalPlaceholder.setGroupCustomFields(groupsCustomFields);

        //Check to see if the user has any gateways activated OR belongs to a group w/ an activated gateway
       globalPlaceholder.setShowActivation(gatewayService.countByUser(user) == 0);

        return globalPlaceholder;
    }


    @Override
    public User newUser(String username, String password, User user) {
        User savedUser = new User();
        //Check dupe email
        User dupeUser = userService.getUserByUserName(user.getUsername());
        //Check to make sure the user doesn't exist and hasn't already been activated.
        if (dupeUser != null && dupeUser.getAccountState() != User.STATE_WAITING_ACTIVATION) return savedUser;

        logger.info("Creating new user " + user);
        if (user.getRole() == null) user.setRole(User.ROLE_USER);
        savedUser = userService.createUser(user, User.STATE_WAITING_ACTIVATION);
        userService.changePassword(savedUser, password);
        
        return savedUser;

    }
}
