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

package com.ted.aggredata.client.guiService;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ted.aggredata.model.*;

import java.util.List;

/**
 * Client Side Interface for the service to check user sessions.
 */
@RemoteServiceRelativePath("GWTGroupService")
public interface GWTGroupService extends RemoteService {

    /**
     * Finds groups for the specific user
     *
     * @return
     */
    public List<Group> findGroups();

    /**
     * Creates a new group
     *
     * @param description
     */
    public Group createGroup(String description);

    /**
     * Saves a group
     *
     * @param group
     */
    public Group saveGroup(Group group);

    /**
     * Delete Group
     */
    public void deleteGroup(Group group);


    /**
     * Returns the history for the given type
     * @param historyType
     * @param group
     * @param startTime
     * @param endTime
     * @param interval used for MINUTE history, this groups the results int 1, 5, or 15 minute intervals.
     * @return
     */
    public EnergyDataHistoryQueryResult getHistory(Enums.HistoryType historyType, Group group, long startTime, long endTime, int interval);


    /**
     * Prepares the history for export and returns a key that can be used to access history
     * @param historyType
     * @param group
     * @param startTime
     * @param endTime
     * @param interval
     * @return
     */
    public String exportHistory(Enums.HistoryType historyType, Group group, long startTime, long endTime, int interval);

    /**
     * Adds a user to the specified group w/ READ_ONLY privileges
     * @param group
     * @param user
     */
    public void addUserToGroup(Group group, User user);

    /**
     * Removes the user from the specified group (only if READ_ONLY privileges exist)
     * @param group
     * @param user
     */
    public void removeUserFromGroup(Group group, User user);

    /**
     * Returns a list of users for the given group
     * @param group
     * @return
     */
    public List<User> getGroupMembers(Group group);

}
