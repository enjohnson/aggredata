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

import com.ted.aggredata.model.Gateway;
import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.User;
import com.ted.aggredata.server.dao.GatewayDAO;
import com.ted.aggredata.server.dao.GroupDAO;
import com.ted.aggredata.server.services.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the GroupService interface
 */

@Transactional
public class GroupServiceImpl implements GroupService {

    @Autowired
    protected GroupDAO groupDAO;

    @Autowired
    protected GatewayDAO gatewayDAO;


    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Group getGroup(User user, Long groupId) {
        return groupDAO.findGroupByUser(user, groupId);
    }

    public Group createGroup(User user, String description) {
        if (logger.isDebugEnabled()) logger.debug("Creating new group w/ description " + description + " for user " + user);
        return groupDAO.create(user, description);

    }

    public void deleteGroup(Group group) {
        if (logger.isInfoEnabled()) logger.info("Deleting group " + group);
        groupDAO.delete(group);
    }

    public Group getGroup(User user, String description) {
        return groupDAO.getOwnedGroup(user, description);
    }

    public List<Group> getByUser(User user) {
        if (logger.isDebugEnabled()) logger.debug("Returning all groups for the user " + user);
        return groupDAO.findGroupsByUser(user);
    }

    public void addUserToGroup(User user, Group group, Group.Role role) {
        if (logger.isDebugEnabled()) logger.debug("Adding " + user + " to " + group + " with the role of " + role);
        groupDAO.addGroupMembership(user, group, role);
    }

    public void removeUserFromGroup(User user, Group group) {
        if (logger.isDebugEnabled()) logger.debug("Removing " + user + " from " + group);
        groupDAO.removeGroupMembership(user, group);
    }

    public void changeUserRole(User user, Group group, Group.Role role) {
        if (logger.isDebugEnabled()) logger.debug("Changing " + user + " role in group " + group + " to " + role);
        groupDAO.updateGroupMembership(user, group, role);
    }

    @Override
    public void addGatewayToGroup(User user, Group group, Gateway gateway) {
        //Check user permissions first
        List<Group> groupList = groupDAO.findGroupsByUser(user);
        for (Group userGroup : groupList) {
            if (userGroup.getId().equals(group.getId()) && (userGroup.getRole() == Group.Role.OWNER || userGroup.getRole() == Group.Role.MEMBER)) {
                if (logger.isInfoEnabled()) logger.info("Adding " + gateway + " from " + group);
                gatewayDAO.addGatewayToGroup(gateway, group);
                return;
            }
        }

        if (logger.isWarnEnabled()) logger.warn("User " + user + " does not have access to group: " + group);


    }

    @Override
    public void removeGatewayFromGroup(User user, Group group, Gateway gateway) {
        //Check user permissions first
        List<Group> groupList = groupDAO.findGroupsByUser(user);
        for (Group userGroup : groupList) {
            if (userGroup.getId().equals(group.getId()) && (userGroup.getRole() == Group.Role.OWNER || userGroup.getRole() == Group.Role.MEMBER)) {
                if (logger.isInfoEnabled()) logger.info("Adding " + gateway + " from " + group);
                gatewayDAO.removeGatewayFromGroup(gateway, group);
                return;
            }
        }
        if (logger.isWarnEnabled()) logger.warn("User " + user + " does not have access to group: " + group);
    }

    @Override
    public Group saveGroup(Group group) {
        if (group.getId() == null) {
            logger.error("Group must be created first.");
            return null;
        }
        if (logger.isInfoEnabled()) logger.info("Saving " + group);
        groupDAO.save(group);
        return group;
    }


}
