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

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Side Panel Widget that allows a group to be selected.
 */
public class MonthSelectionWidget extends Composite {

    interface MyUiBinder extends UiBinder<Widget, MonthSelectionWidget> {
    }

    static Logger logger = Logger.getLogger(GraphSidePanel.class.toString());
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    private static DateBox.DefaultFormat dateBoxFormat = new DateBox.DefaultFormat(DateTimeFormat.getFormat("MM/dd/yyyy"));

    final private HandlerManager handlerManager;
    @UiField
    ListBox endMonthListBox;
    @UiField
    ListBox endYearListBox;
    @UiField
    ListBox startMonthListBox;
    @UiField
    ListBox startYearListBox;

    Date lastStartDate;
    Date lastEndDate;

    private static String[] MONTHS = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    public static int START_YEAR = 112; //2012
    public static int END_YEAR = START_YEAR + 25; //25 years


    private void populateMonth(ListBox listBox){
        for (int i=0; i < 12; i++)
        {
            listBox.addItem(MONTHS[i], i+"");
        }
    }

    private void populateYear(ListBox listBox){

        for (int i=START_YEAR; i < END_YEAR; i++)
        {
            listBox.addItem((1900+i)+"", (i) + "");
        }
    }


    ChangeHandler dateChangeHandler = new ChangeHandler() {
        @Override
        public void onChange(ChangeEvent changeEvent) {
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
    };


    public MonthSelectionWidget() {
        initWidget(uiBinder.createAndBindUi(this));
        handlerManager = new HandlerManager(this);

        populateMonth(startMonthListBox);
        populateMonth(endMonthListBox);
        populateYear(startYearListBox);
        populateYear(endYearListBox);

        startMonthListBox.addChangeHandler(dateChangeHandler);
        endMonthListBox.addChangeHandler(dateChangeHandler);
        startYearListBox.addChangeHandler(dateChangeHandler);
        endYearListBox.addChangeHandler(dateChangeHandler);

    }

    public void setStartDate(Date startDate) {
        int month = startDate.getMonth();
        int year = startDate.getYear();
        if (year < START_YEAR) {
            year = START_YEAR;
            month = 0;
        }
        startMonthListBox.setSelectedIndex(month);
        startYearListBox.setSelectedIndex(year - START_YEAR);
        lastStartDate = startDate;
    }

    public Date getStartDate() {
        Date d = new Date();
        d.setYear(startYearListBox.getSelectedIndex()+START_YEAR);
        d.setMonth(startMonthListBox.getSelectedIndex());
        d.setHours(0);
        d.setDate(1);
        d.setMinutes(0);
        long t = d.getTime()/1000;
        return new Date(t*1000);    }

    public void setEndDate(Date endDate) {
        int month = endDate.getMonth();

        int year = endDate.getYear();
        if (year < START_YEAR) { year = START_YEAR;
            month = 0;
        }

        endMonthListBox.setSelectedIndex(month);
        endYearListBox.setSelectedIndex(year-START_YEAR);
        lastEndDate = endDate;
    }

    public Date getEndDate() {
        Date d = new Date();
        d.setYear(endYearListBox.getSelectedIndex()+START_YEAR);
        d.setMonth(endMonthListBox.getSelectedIndex());
        d.setHours(0);
        d.setDate(1);
        d.setMinutes(0);
        long t = d.getTime()/1000;
        return new Date(t*1000);
    }


    public HandlerRegistration addDateRangeSelectedHandler(DateRangeSelectedHandler handler) {
        return handlerManager.addHandler(DateRangeSelectedEvent.TYPE, handler);
    }

    public void setEnabled(boolean enabled) {
        endMonthListBox.setEnabled(enabled);
        startMonthListBox.setEnabled(enabled);
        endYearListBox.setEnabled(enabled);
        startYearListBox.setEnabled(enabled);
    }
}
