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

package com.ted.aggredata.client.panels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.ted.aggredata.client.events.TabClickedEvent;
import com.ted.aggredata.client.events.TabClickedHandler;
import com.ted.aggredata.client.widgets.DashboardTab;

/**
 * Implements the tabbed interface for dashboard navigation
 */
public class DashboardTabPanel extends Composite implements HasHandlers {

    final DashboardTab monthTab = new DashboardTab("Month", true);
    final DashboardTab dayTab = new DashboardTab("Day", false);
    final DashboardTab hourTab = new DashboardTab("Hour", false);
    final DashboardTab minuteTab = new DashboardTab("Minute", false);
    final private HandlerManager handlerManager;

    public DashboardTabPanel() {
        handlerManager = new HandlerManager(this);
        monthTab.setSelected(true, false);
        dayTab.setSelected(false, false);
        hourTab.setSelected(false, true);
        minuteTab.setSelected(false, true);

        monthTab.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!monthTab.isSelected()) {
                    monthTab.setSelected(true, false);
                    dayTab.setSelected(false, false);
                    hourTab.setSelected(false, true);
                    minuteTab.setSelected(false, true);
                    handlerManager.fireEvent(new TabClickedEvent(0));
                }
            }
        });


        dayTab.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!dayTab.isSelected()) {
                    monthTab.setSelected(false, false);
                    dayTab.setSelected(true, false);
                    hourTab.setSelected(false, false);
                    minuteTab.setSelected(false, true);
                    handlerManager.fireEvent(new TabClickedEvent(1));
                }
            }
        });

        hourTab.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!hourTab.isSelected()) {
                    monthTab.setSelected(false, false);
                    dayTab.setSelected(false, true);
                    hourTab.setSelected(true, false);
                    minuteTab.setSelected(false, false);
                    handlerManager.fireEvent(new TabClickedEvent(2));
                }
            }
        });

        minuteTab.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!minuteTab.isSelected()) {
                    monthTab.setSelected(false, false);
                    dayTab.setSelected(false, true);
                    hourTab.setSelected(false, true);
                    minuteTab.setSelected(true, false);
                    handlerManager.fireEvent(new TabClickedEvent(3));
                }
            }
        });

        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.add(monthTab);
        horizontalPanel.add(dayTab);
        horizontalPanel.add(hourTab);
        horizontalPanel.add(minuteTab);

        initWidget(horizontalPanel);
    }

    public HandlerRegistration addTabClickedHandler(TabClickedHandler handler) {
        return handlerManager.addHandler(TabClickedEvent.TYPE, handler);
    }
}

