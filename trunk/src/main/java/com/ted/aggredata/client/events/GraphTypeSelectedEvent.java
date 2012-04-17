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
import com.ted.aggredata.model.Enums;

import java.util.Date;


/**
 * Event fired off when a date range is selected
 */
public class GraphTypeSelectedEvent extends GwtEvent<GraphTypeSelectedHandler> {
    public static Type<GraphTypeSelectedHandler> TYPE = new Type<GraphTypeSelectedHandler>();




    final Enums.GraphType selectedGraphType;


    public GraphTypeSelectedEvent(Enums.GraphType selectedGraphType) {
        this.selectedGraphType = selectedGraphType;
    }

    @Override
    public Type<GraphTypeSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(GraphTypeSelectedHandler handler) {
        handler.onGraphTypeSelected(this);
    }

    public Enums.GraphType getSelectedGraphType() {
        return selectedGraphType;
    }
}
