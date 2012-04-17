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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import com.ted.aggredata.client.events.DateRangeSelectedEvent;
import com.ted.aggredata.client.events.DateRangeSelectedHandler;


import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Side Panel Widget that allows a group to be selected.
 */
public class DateSelectionWidget extends Composite {

    interface MyUiBinder extends UiBinder<Widget, DateSelectionWidget> {
    }

    static Logger logger = Logger.getLogger(GraphSidePanel.class.toString());
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    private static DateBox.DefaultFormat dateBoxFormat = new DateBox.DefaultFormat(DateTimeFormat.getFormat("MM/dd/yyyy"));

    final private HandlerManager handlerManager;
    @UiField
    DateBox startDateBox;
    @UiField
    DateBox endDateBox;

    Date lastStartDate;
    Date lastEndDate;

    public DateSelectionWidget() {
        initWidget(uiBinder.createAndBindUi(this));
        handlerManager = new HandlerManager(this);
        startDateBox.setFormat(dateBoxFormat);
        endDateBox.setFormat(dateBoxFormat);


        startDateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
            @Override
            public void onValueChange(ValueChangeEvent<Date> dateValueChangeEvent) {
                lastStartDate = dateValueChangeEvent.getValue();

                if (lastStartDate.after(lastEndDate)) {
                    lastEndDate = lastStartDate;
                    endDateBox.setValue(lastEndDate);
                }

                if (logger.isLoggable(Level.FINE)) logger.fine("Firing Date Range Selector for " + lastStartDate + " to " + lastEndDate);
                handlerManager.fireEvent(new DateRangeSelectedEvent(lastStartDate, lastEndDate));
            }
        });

        endDateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
            @Override
            public void onValueChange(ValueChangeEvent<Date> dateValueChangeEvent) {
                lastEndDate = dateValueChangeEvent.getValue();
                if (lastStartDate.after(lastEndDate)) {
                    lastEndDate = lastStartDate;
                    endDateBox.setValue(lastEndDate);
                }
                if (logger.isLoggable(Level.FINE)) logger.fine("Firing Date Range Selector for " + lastStartDate + " to " + lastEndDate);
                handlerManager.fireEvent(new DateRangeSelectedEvent(lastStartDate, lastEndDate));
            }
        });

    }

    public void setStartDate(Date startDate) {
        startDateBox.setValue(startDate);
        lastStartDate = startDate;
    }

    public Date getStartDate() {
        return lastStartDate;
    }

    public void setEndDate(Date endDate) {
        endDateBox.setValue(endDate);
        lastEndDate = endDate;
    }

    public Date getEndDate() {
        return lastEndDate;
    }


    public HandlerRegistration addDateRangeSelectedHandler(DateRangeSelectedHandler handler) {
        return handlerManager.addHandler(DateRangeSelectedEvent.TYPE, handler);
    }
}
