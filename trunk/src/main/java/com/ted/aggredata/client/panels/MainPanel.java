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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.ted.aggredata.client.events.TabClickedEvent;
import com.ted.aggredata.client.events.TabClickedHandler;
import com.ted.aggredata.client.panels.graph.GraphPanel;

public class MainPanel extends Composite {

    @UiField
    DashboardTabPanel tabPanel;

    interface MyUiBinder extends UiBinder<Widget, MainPanel> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    AbsolutePanel contentPanel;

    public MainPanel() {
        initWidget(uiBinder.createAndBindUi(this));
        contentPanel.add(new GraphPanel("Monthly Graphing View"));

        tabPanel.addTabClickedHandler(new TabClickedHandler() {
            @Override
            public void onTabClicked(TabClickedEvent event) {
                contentPanel.clear();

                switch (event.getTabIndex()) {
                    case 0: {
                        contentPanel.add(new GraphPanel("Monthly Graphing View"));
                        break;
                    }
                    case 1: {
                        contentPanel.add(new GraphPanel("Daily Graphing View"));
                        break;
                    }
                    case 2: {
                        contentPanel.add(new GraphPanel("Hourly Graphing View"));
                        break;
                    }
                    case 3: {
                        contentPanel.add(new GraphPanel("Minute Graphing View"));
                        break;
                    }

                }

            }
        });

    }

}
