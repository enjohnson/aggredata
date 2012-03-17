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
 * Event fired off when a tab is clicked
 */
public class TabClickedEvent extends GwtEvent<TabClickedHandler> {
    public static Type<TabClickedHandler> TYPE = new Type<TabClickedHandler>();


    private final int tabIndex;

    public TabClickedEvent(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    @Override
    public Type<TabClickedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TabClickedHandler handler) {
        handler.onTabClicked(this);
    }


    public int getTabIndex() {
        return tabIndex;
    }
}
