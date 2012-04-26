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
import com.ted.aggredata.model.User;


/**
 * Event fired off when a gateway is selected
 */
public class UserSelectedEvent extends GwtEvent<UserSelectedHandler> {
    public static Type<UserSelectedHandler> TYPE = new Type<UserSelectedHandler>();

    final User user;


    public UserSelectedEvent(User user) {
        this.user = user;
    }

    @Override
    public Type<UserSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(UserSelectedHandler handler) {
        handler.onUserSelected(this);
    }

    public User getUser() {
        return user;
    }
}
