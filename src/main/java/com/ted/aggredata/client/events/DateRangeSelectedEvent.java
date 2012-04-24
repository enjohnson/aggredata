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

import java.util.Date;


/**
 * Event fired off when a date range is selected
 */
public class DateRangeSelectedEvent extends GwtEvent<DateRangeSelectedHandler> {
    public static Type<DateRangeSelectedHandler> TYPE = new Type<DateRangeSelectedHandler>();

    final Date startDate;
    final Date endDate;
    final Integer interval;


    /**
     * Constructor for the DateRange Selected Invent
     * @param startDate
     * @param endDate
     */
    public DateRangeSelectedEvent(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.interval = 0;
    }

    /***
     * Constructor for the DateRange Selected Invent
     * @param startDate
     * @param endDate
     * @param interval used by minute graphing only. the interval grouping for the returned resuls (e.g. 15 minutes)
     */
    public DateRangeSelectedEvent(Date startDate, Date endDate, Integer interval) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.interval = interval;
    }

    @Override
    public Type<DateRangeSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DateRangeSelectedHandler handler) {
        handler.onDateRangeSelected(this);
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Integer getInterval() {
        return interval;
    }
}
