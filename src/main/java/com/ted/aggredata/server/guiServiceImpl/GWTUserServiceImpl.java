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
}
