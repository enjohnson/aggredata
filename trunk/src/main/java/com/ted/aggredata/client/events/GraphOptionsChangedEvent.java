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
import com.ted.aggredata.model.Group;

import java.util.Date;


/**
 * Event fired off when a graphing option is changed
 */
public class GraphOptionsChangedEvent extends GwtEvent<GraphOptionsChangedHandler> {
    public static Type<GraphOptionsChangedHandler> TYPE = new Type<GraphOptionsChangedHandler>();



    final Date startDate;
    final Date endDate;
    final Group group;
    final Enums.GraphType graphType;

    public GraphOptionsChangedEvent(Group group, Date startDate, Date endDate, Enums.GraphType graphType) {
        this.group = group;
        this.startDate = startDate;
        this.endDate = endDate;
        this.graphType = graphType;
    }

    @Override
    public Type<GraphOptionsChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(GraphOptionsChangedHandler handler) {
        handler.onGraphingOptionsChanged(this);
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Group getGroup() {
        return group;
    }

    public Enums.GraphType getGraphType() {
        return graphType;
    }
}


