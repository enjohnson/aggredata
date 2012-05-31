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

import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.User;
import com.ted.aggredata.server.dao.UserDAO;
import com.ted.aggredata.server.services.GroupService;
import com.ted.aggredata.server.services.UserService;
import com.ted.aggredata.server.util.KeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class UserServiceImpl implements UserService {


    static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    protected UserDAO userDao;

    @Autowired
    protected GroupService groupService;




    public User createUser(User entity, int accountState) {
        logger.debug("creating user " + entity);
        if (entity.getTimezone() == null || entity.getTimezone().length() == 0) entity.setTimezone("US/Eastern");
        User user = userDao.create(entity);
        user.setAccountState(accountState);
        checkUserConfig(user);
        return user;
    }

    public void deleteUser(User entity) {
        logger.debug("deleting user " + entity);
        userDao.delete(userDao.findById(entity.getId()));
    }

    public User changeUserStatus(User entity, boolean enabled) {
        logger.debug("Changing enabled state to " + enabled);
        if (enabled) entity.setAccountState(User.STATE_ENABLED);
        else  entity.setAccountState(User.STATE_DISABLED);
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
            User user = userDao.getUserByUserName(username);
            if (user == null) return null;
            checkUserConfig(user);
            return user;
        } catch (Exception ex) {
            logger.error("getUserByUserName:" + ex.getMessage(), ex);
            return null;
        }
    }

    public User saveUser(User entity) {
        try {
            logger.info("Saving user information");
            return userDao.save(entity);
        } catch (Exception ex) {
            logger.info("saveUser: " + ex.getMessage(), ex);
            return null;
        }
    }

    @Override
    public User getUserByActivationKey(String key) {
        if (logger.isDebugEnabled()) logger.debug("Looking up user with activation key " + key);
        User user = userDao.getUserByKey(key);
        if (user == null) return null;
        checkUserConfig(user);
        return user;
    }


    /**
     * This double checks that the user has an activation key and at least one group. Used in case users are
     * created in the database directly.
     *
     * @param user
     */
    public void checkUserConfig(User user) {
        if (logger.isDebugEnabled()) logger.debug("Checking user configuration for " + user);

        //Check to make sure an activation key is generated. This should be done on
        //user creation but this check is here in case we do a direct database
        //load of users.
        if (user.getActivationKey() == null || user.getActivationKey().length() == 0) {
            if (logger.isDebugEnabled()) logger.debug("Generating new activation key for " + user);

            String key = KeyGenerator.generateSecurityKey(18);
            while (!userDao.isUniqueKey(key)) {
                key = KeyGenerator.generateSecurityKey(18);
            }
            user.setActivationKey(key);
            userDao.save(user);
        }

        //Check to see at least one group is create
        List<Group> groupList = groupService.getOwnedByUser(user);
        if (groupList == null || groupList.size() == 0) {
            if (logger.isDebugEnabled()) logger.debug("adding default group for " + user);
            groupService.createGroup(user, "Default Group");
        }


    }

    @Override
    public List<User> findUsers() {
        return userDao.findUsers();
    }

    @Override
    public String getPassword(User user) {
        if (logger.isDebugEnabled()) logger.info("Looking up password for " + user);
        return userDao.getPassword(user);
    }

    @Override
    public List<User> findUsers(String substring) {
        return userDao.findUsers(substring);
    }

    @Override
    public List<User> findUsers(Group group) {
        return userDao.findUsers(group);
    }

    @Override
    public User findUser(Long userId) {
        return userDao.findById(userId);
    }
}
