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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.ted.aggredata.client.events.TabClickedEvent;
import com.ted.aggredata.client.events.TabClickedHandler;
import com.ted.aggredata.client.resources.lang.DashboardConstants;
import com.ted.aggredata.client.widgets.DashboardTab;

/**
 * Implements the tabbed interface for dashboard navigation
 */
public class DashboardTabPanel extends Composite implements HasHandlers {


    final DashboardTab dashboardTab[] = new DashboardTab[4]; 
    final private HandlerManager handlerManager;
    final Integer numberOfTabs;

    public DashboardTabPanel(String...tabs) {

        numberOfTabs = tabs.length;
        GWT.log("NUMBER OF TABS:" + numberOfTabs);

        final HorizontalPanel horizontalPanel = new HorizontalPanel();
        handlerManager = new HandlerManager(this);
        
        for (int i=0; i < numberOfTabs; i++)
        {
            final int index = i;
            dashboardTab[i] = new DashboardTab(tabs[i], (i==0));
            horizontalPanel.add(dashboardTab[i]);
            dashboardTab[i].setSelected((i==0), i>1);
            dashboardTab[i].addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (!dashboardTab[index].isSelected()){
                        for (int j=0; j < numberOfTabs; j++)
                        {
                            //If the index of the tab is the same that is selected, we mark the select flag as true;
                            //if the index is at least 2 higher than the selected index, we mark the div flag as true
                            dashboardTab[j].setSelected(j==index, j>(index+1));
                        }
                        handlerManager.fireEvent(new TabClickedEvent(index));
                    }
                }
            });
        }
        initWidget(horizontalPanel);
    }

    public HandlerRegistration addTabClickedHandler(TabClickedHandler handler) {
        return handlerManager.addHandler(TabClickedEvent.TYPE, handler);
    }

    public void setSelectedTab(int index) {
        for (int j=0; j < numberOfTabs; j++)
        {
            //If the index of the tab is the same that is selected, we mark the select flag as true;
            //if the index is at least 2 higher than the selected index, we mark the div flag as true
            dashboardTab[j].setSelected(j==index, j>(index+1));
        }
    }
}

