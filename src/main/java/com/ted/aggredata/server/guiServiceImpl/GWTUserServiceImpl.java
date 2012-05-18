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

import com.ted.aggredata.client.guiService.GWTUserService;
import com.ted.aggredata.model.User;
import com.ted.aggredata.server.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class GWTUserServiceImpl extends SpringRemoteServiceServlet implements GWTUserService {

    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(GWTUserServiceImpl.class);
    public static final String USER_SESSION_KEY = "AGGREDATA_USER";

    @Override
    public User saveUser(User user) {
        logger.info("Saving user information");
        getThreadLocalRequest().getSession().removeAttribute(USER_SESSION_KEY);
        user = userService.saveUser(user);
        getThreadLocalRequest().getSession().setAttribute(USER_SESSION_KEY, user);
        return user;
    }

    @Override
    public User changePassword(User user, String Password) {
        logger.info("Updating Password");
        getThreadLocalRequest().getSession().removeAttribute(USER_SESSION_KEY);
        userService.changePassword(user, Password);
        getThreadLocalRequest().getSession().setAttribute(USER_SESSION_KEY, user);
        return user;
    }

    @Override
    public User changeUsername(User user, String username) {
        logger.info("Updating username");
        getThreadLocalRequest().getSession().removeAttribute(USER_SESSION_KEY);
        userService.changeUserName(user, username);
        getThreadLocalRequest().getSession().setAttribute(USER_SESSION_KEY, user);
        return user;
    }

    @Override
    public List<User> findUsers() {
        User requestingUser = (User)getThreadLocalRequest().getSession().getAttribute(USER_SESSION_KEY);
        if (logger.isInfoEnabled()) logger.info(requestingUser + " is requesting a list of all users");
        if (requestingUser != null && requestingUser.getRole().equals(User.ROLE_ADMIN)){
            if (logger.isInfoEnabled()) logger.info("Looking up all users");
            return userService.findUsers();
        } else {
            logger.warn(requestingUser + " is attempting to get a list of users but is not an admin!");
            return new ArrayList<User>();
        }

    }

    @Override
    public User createUser(User user){
        User requestingUser = (User)getThreadLocalRequest().getSession().getAttribute(USER_SESSION_KEY);
        if (user.getRole() == null) user.setRole(User.ROLE_USER);
        if (logger.isInfoEnabled()) logger.info(requestingUser + " is creating a new user");
        if (requestingUser != null && requestingUser.getRole().equals(User.ROLE_ADMIN)) {
            return userService.createUser(user);
        } else {
            logger.warn(requestingUser + " is attempting to create a user but is not an admin!");
            return null;
        }
    }

    @Override
    public void deleteUser(User user){
        User requestingUser = (User)getThreadLocalRequest().getSession().getAttribute(USER_SESSION_KEY);
        if (logger.isInfoEnabled()) logger.info(requestingUser + " is deleting user " + user);
        if (requestingUser != null && requestingUser.getRole().equals(User.ROLE_ADMIN)) {
            userService.deleteUser(user);
        }
    }

    @Override
    public List<User> findUsers(String substring) {
        if (logger.isInfoEnabled()) logger.info("Looking up users with substring " + substring);
        return userService.findUsers(substring);
    }
}
