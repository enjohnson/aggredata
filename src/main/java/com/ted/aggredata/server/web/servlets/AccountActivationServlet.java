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

package com.ted.aggredata.server.web.servlets;

import com.ted.aggredata.model.Gateway;
import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.ServerInfo;
import com.ted.aggredata.model.User;
import com.ted.aggredata.server.guiServiceImpl.UserSessionServiceImpl;
import com.ted.aggredata.server.services.GatewayService;
import com.ted.aggredata.server.services.GroupService;
import com.ted.aggredata.server.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.w3c.dom.Document;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


/**
 * *
 * This is the servlet that handles activation of a new gateway. It checks the security key against enabled user accounts
 * and adds the gateway and mtu's if one does not exists. If a gateway exists, it updates the gateway as active if it has been marked inactive.
 */
public class AccountActivationServlet extends HttpServlet {

    static Logger logger = LoggerFactory.getLogger(AccountActivationServlet.class);

    @Autowired
    UserService userService;

    @Autowired
    GatewayService gatewayService;

    @Autowired
    GroupService groupService;

    @Autowired
    ServerInfo serverInfo;


    @Autowired
    AuthenticationManager authenticationManager;



    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        logger.debug("Account Activate GET received");
        String key = request.getParameter("key");
        if (key == null) {
            logger.warn("No key specified for account activation");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        //Verify key
        User user = userService.getUserByActivationKey(key);
        if (user == null || user.getAccountState()==User.STATE_DISABLED) {
            logger.warn("User not enabled for activation or key not valid:" + user);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        //Enable account

        user.setAccountState(User.STATE_ENABLED);
        user.setActivationKey(null);
        userService.checkUserConfig(user);
        if (logger.isInfoEnabled())logger.info("Enabled account:" + user);

        //Do authentication
        request.getSession().removeAttribute(UserSessionServiceImpl.USER_SESSION_KEY);

        try {
            Authentication authRequest = new UsernamePasswordAuthenticationToken(user.getUsername(), userService.getPassword(user));
            if (authenticationManager == null) logger.error("AuthenticationManager is null");
            Authentication result = authenticationManager.authenticate(authRequest);
            SecurityContextHolder.getContext().setAuthentication(result);
        } catch (AuthenticationException e) {
            logger.warn("Authentication failed: " + e.getMessage());
            response.sendRedirect("/aggredata/index.html");
        }

        if (logger.isInfoEnabled()) logger.info("Authentication success: " + SecurityContextHolder.getContext().getAuthentication());
        request.getSession().setAttribute(UserSessionServiceImpl.USER_SESSION_KEY, user);

        //redrect user to login
        response.sendRedirect("/aggredata/index.html");

    }
}
