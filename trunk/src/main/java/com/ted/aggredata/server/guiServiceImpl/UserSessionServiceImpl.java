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

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ted.aggredata.client.guiService.UserSessionService;
import com.ted.aggredata.model.GlobalPlaceholder;
import com.ted.aggredata.model.ServerInfo;
import com.ted.aggredata.model.User;
import com.ted.aggredata.server.services.GatewayService;
import com.ted.aggredata.server.services.UserService;
import com.ted.aggredata.server.util.KeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserSessionServiceImpl extends SpringRemoteServiceServlet implements UserSessionService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;
    
    @Autowired
    ServerInfo serverInfo;

    @Autowired
    GatewayService gatewayService;

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

        logger.info("Authentication success: " + SecurityContextHolder.getContext() .getAuthentication());
        User user = userService.getUserByUserName(username);
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

        //Check to make sure the user has a valid spring securtity session
        if (SecurityContextHolder.getContext().getAuthentication() == null)
        {
            logger.info("User not authenticated");
            return null;
        }


        //Grab the user object from the session
        if (logger.isInfoEnabled()) logger.info("Looking up user session");
        User user = (User) getThreadLocalRequest().getSession().getAttribute(USER_SESSION_KEY);
        
        if (user == null) {
            logger.info("No user found in session");
            return null;
        }

        if (logger.isDebugEnabled()) logger.info("Found user object for: " + user);

        
        return loadGlobal(user);
    }
    
    private GlobalPlaceholder loadGlobal(User user)
    {
        if (logger.isDebugEnabled()) logger.debug("Loading Globals for " + user);
        GlobalPlaceholder globalPlaceholder = new GlobalPlaceholder();
        globalPlaceholder.setSessionUser(user);
        globalPlaceholder.setServerInfo(serverInfo);
        globalPlaceholder.setGateways(gatewayService.getByUser(user));
        

        return globalPlaceholder;
    }

    @Override
    public User saveUser(User user){
        logger.info("Saving user information");
        getThreadLocalRequest().getSession().removeAttribute(USER_SESSION_KEY);
        user = userService.saveUser(user);
        getThreadLocalRequest().getSession().setAttribute(USER_SESSION_KEY, user);
        return user;
    }
}
