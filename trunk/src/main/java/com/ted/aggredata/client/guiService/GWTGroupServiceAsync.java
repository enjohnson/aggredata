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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ted.aggredata.model.*;

import java.util.List;

public interface GWTGroupServiceAsync {

    /**
     * Creates a new group
     *
     * @param description
     */
    void createGroup(String description, AsyncCallback<Group> async);

    /**
     * Saves a group
     *
     * @param group
     */
    void saveGroup(Group group, AsyncCallback<Group> async);

    /**
     * Delete Group
     */
    void deleteGroup(Group group, AsyncCallback<Void> async);


    /**
     * Returns the history for the given type
     *
     * @param historyType
     * @param group
     * @param startTime
     * @param endTime
     * @param interval    used for MINUTE history, this groups the results int 1, 5, or 15 minute intervals.
     * @return
     */
    void getHistory(Enums.HistoryType historyType, Group group, long startTime, long endTime, int interval, AsyncCallback<EnergyDataHistoryQueryResult> async);


    /**
     * Generates a key allowing the history to be exported.
     *
     * @param historyType
     * @param group
     * @param startTime
     * @param endTime
     * @param interval
     * @return
     */
    void exportHistory(Enums.HistoryType historyType, Group group, long startTime, long endTime, int interval, AsyncCallback<String> async);

    /**
     * Adds a user to the specified group w/ READ_ONLY privileges
     *
     * @param group
     * @param user
     */
    void addUserToGroup(Group group, User user, AsyncCallback<Void> async);

    /**
     * Removes the user from the specified group (only if READ_ONLY privileges exist)
     *
     * @param group
     * @param user
     */
    void removeUserFromGroup(Group group, User user, AsyncCallback<Void> async);

    /**
     * Returns a list of users for the given group
     *
     * @param group
     * @return
     */
    void getGroupMembers(Group group, AsyncCallback<List<User>> async);

    /**
     * Finds groups with gateways the specific user
     *
     * @return
     */
    void findGroupsWithGateways(AsyncCallback<List<Group>> async);

    /**
     * Finds owned groups for the specific user
     *
     * @return
     */
    void findOwnedGroups(AsyncCallback<List<Group>> async);
}

