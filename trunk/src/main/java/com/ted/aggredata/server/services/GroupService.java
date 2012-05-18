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

package com.ted.aggredata.server.services;

import com.ted.aggredata.model.Gateway;
import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.User;

import java.util.List;

/**
 * Public interface to the user management service
 */
public interface GroupService {

    /**
     * Creates a new group
     *
     * @param user        The user creating the group. This user will be added as a OWNER of the group.
     * @param description
     */
    public Group createGroup(User user, String description);

    /**
     * Deletes a group from the system
     *
     * @param group
     */
    public void deleteGroup(Group group);

    /**
     * Returns the group for the given user and description
     *
     * @param user
     * @param description
     * @return
     */
    public Group getGroup(User user, String description);

    /**
     * Returns all groups for the specified user
     *
     * @param user
     * @return
     */
    public List<Group> getByUser(User user);

    /**
     * Adds a user w/ the specified role in the group;
     *
     * @param user
     * @param group
     * @param role
     */
    public void addUserToGroup(User user, Group group, Group.Role role);

    /**
     * Removes the user from the group
     *
     * @param user
     * @param group
     */
    public void removeUserFromGroup(User user, Group group);

    /**
     * Changes the role of the user
     *
     * @param user
     * @param group
     * @param role
     */
    public void changeUserRole(User user, Group group, Group.Role role);

    /**
     * Adds a Gateway to a specific group
     *
     * @param user
     * @param group
     * @param gateway
     */
    public void addGatewayToGroup(User user, Group group, Gateway gateway);


    /**
     * Removes a gateway from a specific group
     *
     * @param user
     * @param group
     * @param gateway
     */
    public void removeGatewayFromGroup(User user, Group group, Gateway gateway);


    /**
     * Saves the group
     *
     * @param group
     */
    public Group saveGroup(Group group);


}
