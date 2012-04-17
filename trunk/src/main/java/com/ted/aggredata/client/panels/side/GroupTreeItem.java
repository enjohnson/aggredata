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

package com.ted.aggredata.client.panels.side;

import com.google.gwt.user.client.ui.TreeItem;
import com.ted.aggredata.model.Group;

/**
 * Represents a group tree item in the group tree
 */
public class GroupTreeItem extends TreeItem {

    final Group group;

    public GroupTreeItem(Group group) {
        super(group.getDescription());
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }
}
