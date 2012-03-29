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

package com.ted.aggredata.server.services.impl;

import com.ted.aggredata.model.User;
import com.ted.aggredata.server.dao.UserDAO;
import com.ted.aggredata.server.services.UserService;
import com.ted.aggredata.server.util.KeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    protected UserDAO userDao;

    Logger logger = LoggerFactory.getLogger(getClass());


    public User createUser(User entity) {
        logger.debug("creating user " + entity);
        return userDao.create(entity);
    }

    public void deleteUser(User entity) {
        logger.debug("deleting user " + entity);
        userDao.delete(userDao.findById(entity.getId()));
    }

    public User changeUserStatus(User entity, boolean enabled) {
        logger.debug("Changing enabled state to " + enabled);
        entity.setState(enabled);
        userDao.save(entity);
        return entity;
    }

    public User changeUserRole(User entity, String role) {
        entity.setRole(role);
        userDao.save(entity);
        return entity;
    }

    public User changePassword(User entity, String newPassword) {
        logger.debug("Changing user's password");
        User userEntity = userDao.findById(entity.getId());
        userDao.updatePassword(userEntity, newPassword);
        return userEntity;
    }

    public User changeUserName(User entity, String newUsername) {
        logger.info("Changing username from : " + entity.getUsername() + "  to " + newUsername);
        User userEntity = userDao.findById(entity.getId());
        userEntity.setUsername(newUsername);
        userDao.save(userEntity);
        return userEntity;
    }

    public User getUserByUserName(String username) {
        try {
            logger.debug("Looking up user with username " + username);
            User user =  userDao.getUserByUserName(username);

            //Check to make sure an activation key is generated. This should be done on
            //user creation but this check is here in case we do a direct database
            //load of users.
            if (user.getActivationKey()== null || user.getActivationKey().length()==0){
                if (logger.isDebugEnabled()) logger.debug("Generating new activation key for " + user);
                user.setActivationKey(KeyGenerator.generateSecurityKey(10));
                userDao.save(user);
            }


            return user;
        } catch (Exception ex) {
            logger.error("getUserByUserName:" + ex.getMessage(), ex);
            return null;
        }
    }
}
