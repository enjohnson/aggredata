/*
 * Copyright (c) 2011. The Energy Detective. All Rights Reserved
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

package server.com.ted.aggredata.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import server.com.ted.aggredata.dao.GroupDAO;
import server.com.ted.aggredata.model.Group;
import server.com.ted.aggredata.model.User;
import server.com.ted.aggredata.services.GroupService;

import java.util.List;

/***
 * Implementation of the GroupService interface
 */

@Transactional
public class GroupServiceImpl implements GroupService {

    @Autowired
    protected GroupDAO groupDAO;
    Logger logger = LoggerFactory.getLogger(getClass());

    public void createGroup(User user, String description) {
        logger.debug("Creating new group w/ description " + description + " for user " + user);
        logger.debug("Checking for existing group");
        Group oldGroup = groupDAO.getGroup(user, description);
        if (oldGroup == null)
        {
            Group group = new Group();
            group.setDescription(description);
            group.setOwnerUserId(user.getId());
            groupDAO.create(group);
            group = groupDAO.getGroup(user, description);
            groupDAO.addGroupMembership(user, group, Group.Role.ADMIN);
        }
    }

    public void deleteGroup(Group group) {
        logger.info("Deleting group " + group);
        groupDAO.deleteGroupMemberships(group);
        groupDAO.delete(group);
    }

    public List<Group> getByUser(User user) {
        logger.debug("Returning all groups for the user " + user);
        return groupDAO.getGroups(user);
    }

    public void addUserToGroup(User user, Group group, Group.Role role) {
        logger.debug("Adding " + user + " to " + group + " with the role of " + role);
        groupDAO.addGroupMembership(user, group, role);
    }

    public void removeUserFromGroup(User user, Group group) {
        logger.debug("Removing " + user + " from " + group);
        groupDAO.removeGroupMembership(user, group);
    }

    public void changeUserRole(User user, Group group, Group.Role role) {
        logger.debug("Changing " + user + " role in group " + group + " to " + role);
        groupDAO.updateGroupMembership(user, group, role);
    }

}
