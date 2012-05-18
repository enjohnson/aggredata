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

package com.ted.aggredata.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.User;

import java.util.Date;


/**
 * Event fired off when a user is added or removed from a group.
 */
public class GroupMemberRemovedEvent extends GwtEvent<GroupMemberRemovedHandler> {
    public static Type<GroupMemberRemovedHandler> TYPE = new Type<GroupMemberRemovedHandler>();

    final User user;
    final Group group;

    public GroupMemberRemovedEvent(Group group, User user) {
        this.group = group;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Group getGroup() {
        return group;
    }

    @Override
    public Type<GroupMemberRemovedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(GroupMemberRemovedHandler handler) {
        handler.onGroupMemberRemoved(this);
    }


}
