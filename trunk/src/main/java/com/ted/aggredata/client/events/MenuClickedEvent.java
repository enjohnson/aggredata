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

/**
 * Event fired off when a menu item is clicked
 */
public class MenuClickedEvent extends GwtEvent<MenuClickedHandler> {
    public static Type<MenuClickedHandler> TYPE = new Type<MenuClickedHandler>();

    public enum MenuOptions {PROFILE, ENERGY, ADMIN, LOGOUT}

    ;

    private final MenuOptions menuOption;

    public MenuClickedEvent(MenuOptions menuOption) {
        this.menuOption = menuOption;
    }

    @Override
    public Type<MenuClickedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(MenuClickedHandler handler) {
        handler.onMenuClicked(this);
    }

    public MenuOptions getMenuSelection() {
        return menuOption;
    }
}
