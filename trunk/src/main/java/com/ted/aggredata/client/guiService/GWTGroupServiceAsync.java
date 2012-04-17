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
import com.ted.aggredata.model.Group;

import java.util.List;

public interface GWTGroupServiceAsync {
    void findGroups(AsyncCallback<List<Group>> async);

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


}
