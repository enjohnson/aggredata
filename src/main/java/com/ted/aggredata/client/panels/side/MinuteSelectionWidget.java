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
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.ted.aggredata.client.events.DateRangeSelectedEvent;
import com.ted.aggredata.client.events.DateRangeSelectedHandler;
import com.ted.aggredata.client.widgets.TimePicker;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Side Panel Widget that allows a group to be selected.
 */
public class MinuteSelectionWidget extends Composite {

    interface MyUiBinder extends UiBinder<Widget, MinuteSelectionWidget> {
    }

    static Logger logger = Logger.getLogger(GraphSidePanel.class.toString());
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    private static DateBox.DefaultFormat dateBoxFormat = new DateBox.DefaultFormat(DateTimeFormat.getFormat("MM/dd/yyyy"));

    final private HandlerManager handlerManager;

    @UiField
    ListBox durationListBox;
    @UiField
    DateBox startDateBox;
    @UiField
    TimePicker endTimePicker;
    @UiField
    TimePicker startTimePicker;

    Date lastStartDate;
    Date lastEndDate;
    int interval;




    ChangeHandler dateChangeHandler = new ChangeHandler() {
        @Override
        public void onChange(ChangeEvent changeEvent) {
             handleChangedDate();
        }
    };





    public MinuteSelectionWidget() {
        initWidget(uiBinder.createAndBindUi(this));
        handlerManager = new HandlerManager(this);
        startDateBox.setFormat(dateBoxFormat);

        durationListBox.addItem("15 minutes");
        durationListBox.addItem("5 minutes");
        durationListBox.addItem("1 minute");

        //Register the hanlders
        durationListBox.addChangeHandler(dateChangeHandler);
        startTimePicker.addChangeHandler(dateChangeHandler);
        endTimePicker.addChangeHandler(dateChangeHandler);
        startDateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
            @Override
            public void onValueChange(ValueChangeEvent<Date> dateValueChangeEvent) {
                 handleChangedDate();
            }
        });

    }

    private void handleChangedDate() {
        Date startDate = getStartDate();
        Date endDate = getEndDate();

        if (logger.isLoggable(Level.FINE)) logger.fine("MONTH Date Range Selected:" + startDate + " to " + endDate);


        if (startDate.after(endDate)) {
            startDate = endDate;
            setStartDate(startDate);
            setEndDate(endDate);
            if (logger.isLoggable(Level.FINE)) logger.fine("Firing Date Range Selector for " + lastStartDate + " to " + lastEndDate);
            handlerManager.fireEvent(new DateRangeSelectedEvent(lastStartDate, lastEndDate));

        }

        if (!startDate.equals(lastStartDate) || !endDate.equals(lastEndDate)){
            lastStartDate = startDate;
            lastEndDate = endDate;
            if (logger.isLoggable(Level.FINE)) logger.fine("Firing Date Range Selector for " + lastStartDate + " to " + lastEndDate);
            handlerManager.fireEvent(new DateRangeSelectedEvent(lastStartDate, lastEndDate));
        }
    }



    public void setStartDate(Date startDate) {
        startDateBox.setValue(startDate);
        startTimePicker.setSelectedItem(startDate.getHours(), startDate.getMinutes());
        lastStartDate = startDate;
    }

    public Date getStartDate() {
        Date d = startDateBox.getValue();
        d.setHours(startTimePicker.getSelectedHour());
        d.setMinutes(startTimePicker.getSelectedMinute());
        d.setSeconds(0);
        long t = d.getTime()/1000;
        return new Date(t*1000);
    }

    public void setEndDate(Date endDate) {
        startDateBox.setValue(endDate);
        endTimePicker.setSelectedItem(endDate.getHours(), endDate.getMinutes());
        lastEndDate = endDate;
    }

    public Date getEndDate() {
        Date d = startDateBox.getValue();
        d.setHours(endTimePicker.getSelectedHour());
        d.setMinutes(endTimePicker.getSelectedMinute());
        d.setSeconds(0);
        long t = d.getTime()/1000;
        return new Date(t*1000);
    }


    public HandlerRegistration addDateRangeSelectedHandler(DateRangeSelectedHandler handler) {
        return handlerManager.addHandler(DateRangeSelectedEvent.TYPE, handler);
    }
}
