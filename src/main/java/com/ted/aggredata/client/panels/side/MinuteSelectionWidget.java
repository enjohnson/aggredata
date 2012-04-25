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
import com.ted.aggredata.client.resources.lang.DashboardConstants;
import com.ted.aggredata.client.util.StringUtil;
import com.ted.aggredata.client.widgets.TimePicker;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Side Panel Widget that allows a group to be selected.
 */
public class MinuteSelectionWidget extends Composite {


    final DashboardConstants dashboardConstants = DashboardConstants.INSTANCE;

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
    int lastInterval = 0;




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

        //Build the duration drop down
        String minutes = StringUtil.toTitleCase(dashboardConstants.minutes());
        durationListBox.addItem("15 " +  minutes, "15");
        durationListBox.addItem("5 " + minutes , "5");
        durationListBox.addItem("1 " + minutes , "1");

        //Register the handlers
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
        int interval = getInterval();

        if (logger.isLoggable(Level.FINE)) logger.fine("Minute Date Range Selected:" + startDate + " to " + endDate + " interval:" + interval);

        if (!startDate.equals(lastStartDate) || !endDate.equals(lastEndDate) || lastInterval != interval){
            lastStartDate = startDate;
            lastEndDate = endDate;
            lastInterval = interval;
            if (logger.isLoggable(Level.FINE)) logger.fine("Firing Date Range Selector for " + lastStartDate + " to " + lastEndDate + " interval:" + interval);
            handlerManager.fireEvent(new DateRangeSelectedEvent(lastStartDate, lastEndDate, interval));
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

    public void setInterval(int i) {
        lastInterval = i;
        String intervalString = Integer.toString(i);
        for (int x=0; x < durationListBox.getItemCount(); x++) {
            if (intervalString.equals(durationListBox.getValue(x))){
                durationListBox.setSelectedIndex(x);
                break;
            }
        }
        if (durationListBox.getSelectedIndex() == -1) durationListBox.setSelectedIndex(0);
    }

    public int getInterval() {
        int index = durationListBox.getSelectedIndex();
        String valueString = durationListBox.getValue(index);
        int interval = Integer.parseInt(valueString);
        return interval;
    }

    public HandlerRegistration addDateRangeSelectedHandler(DateRangeSelectedHandler handler) {
        return handlerManager.addHandler(DateRangeSelectedEvent.TYPE, handler);
    }

    public void setEnabled(boolean enabled) {
        durationListBox.setEnabled(enabled);
        startDateBox.setEnabled(enabled);
        endTimePicker.setEnabled(enabled);
        startTimePicker.setEnabled(enabled);
    }
}
