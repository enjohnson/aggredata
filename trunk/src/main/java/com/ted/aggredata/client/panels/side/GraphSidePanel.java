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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.ted.aggredata.client.events.*;
import com.ted.aggredata.client.guiService.GWTGroupService;
import com.ted.aggredata.client.guiService.GWTGroupServiceAsync;
import com.ted.aggredata.client.guiService.TEDAsyncCallback;
import com.ted.aggredata.client.util.DateUtil;
import com.ted.aggredata.client.widgets.SmallButton;
import com.ted.aggredata.model.Enums;
import com.ted.aggredata.model.Group;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * This is a side panel used for graphing that contains group selection and graphing options.
 */
public class GraphSidePanel extends Composite {

    interface MyUiBinder extends UiBinder<Widget, GraphSidePanel> {
    }

    static Logger logger = Logger.getLogger(GraphSidePanel.class.toString());
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField
    GroupSelectionWidget groupSelectionWidget;
    @UiField
    TypeSelectionWidget typeSelectionWidget;

    MonthSelectionWidget monthSelectionWidget = new MonthSelectionWidget();
    DateSelectionWidget dateSelectionWidget = new DateSelectionWidget();
    MinuteSelectionWidget minuteSelectionWidget = new MinuteSelectionWidget();

    @UiField
    VerticalPanel dateSelectionPanel;
    @UiField
    SmallButton exportButton;

    final GWTGroupServiceAsync groupService = (GWTGroupServiceAsync) GWT.create(GWTGroupService.class);
    List<Group> groupList;
    Group group;
    Date startDate;
    Date endDate;
    Integer interval;
    Enums.GraphType graphType;
    Enums.HistoryType historyType;
    Boolean showTotals;



    final private HandlerManager handlerManager;


    //Handler for when a new date range is selected
    private DateRangeSelectedHandler dateRangeSelectedHandler = new DateRangeSelectedHandler() {
        @Override
        public void onDateRangeSelected(DateRangeSelectedEvent event) {
            logger.fine("Date range Changed to " + event.getStartDate() + " to " + event.getEndDate());
            startDate = event.getStartDate();
            endDate = event.getEndDate();
            interval = event.getInterval();
            fireEvent();
        }
    };

    public GraphSidePanel() {
        initWidget(uiBinder.createAndBindUi(this));
        handlerManager = new HandlerManager(this);

        //Set up the various handlers
        monthSelectionWidget.addDateRangeSelectedHandler(dateRangeSelectedHandler);
        dateSelectionWidget.addDateRangeSelectedHandler(dateRangeSelectedHandler);
        minuteSelectionWidget.addDateRangeSelectedHandler(dateRangeSelectedHandler);

        groupSelectionWidget.addGroupSelectedHandler(new GroupSelectedHandler() {
            @Override
            public void onGroupSelected(GroupSelectedEvent event) {
                logger.fine("Group Changed to " + event.getGroup());
                group = event.getGroup();
                fireEvent();

            }
        });

        typeSelectionWidget.addGraphTypeSelectedHandler(new GraphTypeSelectedHandler() {
            @Override
            public void onGraphTypeSelected(GraphTypeSelectedEvent event) {
                logger.fine("Type Changed to " + event.getSelectedGraphType());
                graphType = event.getSelectedGraphType();
                showTotals = event.getShowTotals();
                fireEvent();
            }
        });


        exportButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                logger.fine("Export Button Clicked");
                //Go ahead and disable the panel while loading to prevent double clicking
                setEnabled(false);

                startDate = DateUtil.adjustToSearchStart(historyType, startDate);
                endDate = DateUtil.adjustToSearchEnd(historyType, endDate);

                groupService.exportHistory(historyType,group, startDate.getTime()/1000, endDate.getTime()/1000, interval, new TEDAsyncCallback<String>() {
                    @Override
                    public void onSuccess(String s) {
                         logger.fine("Redirecting to servlet using key " + s);
                         String url = "/aggredata/historyexport?key=" + s;
                         logger.fine("OPENING URL" + url);
                         setEnabled(true);
                         Window.open(url, "_BLANK", "");
                    }
                });
            }
        });


    }

    public HandlerRegistration addGraphOptionsChangedHandler(GraphOptionsChangedHandler handler) {
        return handlerManager.addHandler(GraphOptionsChangedEvent.TYPE, handler);
    }

    public void fireEvent()
    {
            logger.fine("Graph Side Panel Firing Event");
            if (getHistoryType().equals(Enums.HistoryType.MINUTE)) {
                handlerManager.fireEvent(new GraphOptionsChangedEvent(group, startDate, endDate, graphType, interval, showTotals));
            } else {
                handlerManager.fireEvent(new GraphOptionsChangedEvent(group, startDate, endDate, graphType, showTotals));
            }
    }

    public void setEnabled(boolean enabled)
    {
        exportButton.setVisible(enabled);
        monthSelectionWidget.setEnabled(enabled);
        dateSelectionWidget.setEnabled(enabled);
        minuteSelectionWidget.setEnabled(enabled);
    }

    /**
     * Resets all the data on the selection widget (groups, dates, etc);
     */
    public void reset() {
        //Load the list of groups and set the default values for graphing.
        groupService.findGroupsWithGateways(new TEDAsyncCallback<List<Group>>() {
            @Override
            public void onSuccess(List<Group> groups) {

                logger.fine("Group List Loaded. Setting default values");
                //Set the default values
                if (groups.size() > 0) {
                    groupList = groups;
                    groupSelectionWidget.setGroups(groupList, groupList.get(0));
                    group = groupList.get(0);
                }

                setHistoryType(Enums.HistoryType.MONTHLY);
                resetDate();
                graphType = Enums.GraphType.ENERGY;
                showTotals = true;
                typeSelectionWidget.setType(graphType);
                typeSelectionWidget.setShowTotals(true);

                fireEvent();

            }
        });
    }

    /**
     * Resets the date (usually called on tab selection).  This is also where the default drawing ranges are set.
     */
    private void resetDate()
    {
        startDate = new Date();
        startDate.setHours(0);
        startDate.setMinutes(0);
        startDate.setSeconds(0);
        interval = 1;
        endDate = new Date(startDate.getTime());

        if (getHistoryType().equals(Enums.HistoryType.MONTHLY)) {
            CalendarUtil.addMonthsToDate(startDate, -12);
            monthSelectionWidget.setStartDate(startDate);
            monthSelectionWidget.setEndDate(endDate);

            //We do this to get the local date in case its out of the range for the widget start date
            startDate= monthSelectionWidget.getStartDate();
        }
        else if (getHistoryType().equals(Enums.HistoryType.DAILY)) {
            CalendarUtil.addDaysToDate(startDate, -7);
            dateSelectionWidget.setStartDate(startDate);
            dateSelectionWidget.setEndDate(endDate);
        } else  if (getHistoryType().equals(Enums.HistoryType.MINUTE)){
            //For minute we just focus ont he last hour

            long currentTime = (new Date()).getTime();
            //Get rid of ms
            currentTime = (currentTime / 1000) * 1000;
            //Round up to the next 15 minute mark.
            currentTime += (900000-1);
            currentTime = (currentTime/900000) * 900000;



            startDate = new Date(currentTime);
            startDate.setSeconds(0);


            if (startDate.getHours() == 0)
            {
                startDate.setMinutes(0);
                //Special case for dealing with midnight.
                endDate = new Date(startDate.getTime() +  3600000);
                interval = 5;
            } else {
                endDate = new Date(startDate.getTime());
                startDate = new Date(startDate.getTime() - 3600000);
                interval = 5;
            }

            minuteSelectionWidget.setStartDate(startDate);
            minuteSelectionWidget.setEndDate(endDate);
            minuteSelectionWidget.setInterval(interval);
        } else  {
            CalendarUtil.addDaysToDate(startDate, -1);
            dateSelectionWidget.setStartDate(startDate);
            dateSelectionWidget.setEndDate(endDate);
        }

    }

    public Enums.HistoryType getHistoryType() {

        return historyType;
    }

    /**
     * Changes which date range selection widget is shown.
     * @param historyType
     */
    public void setHistoryType(Enums.HistoryType historyType) {
        this.historyType = historyType;
        dateSelectionPanel.clear();
        if (historyType.equals(Enums.HistoryType.MONTHLY)) {
            dateSelectionPanel.add(monthSelectionWidget);
        } else if (historyType.equals(Enums.HistoryType.MINUTE)) {
            dateSelectionPanel.add(minuteSelectionWidget);
        }
        else dateSelectionPanel.add(dateSelectionWidget);
        resetDate();
    }
}
