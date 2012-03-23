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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.ted.aggredata.client.events.MenuClickedEvent;
import com.ted.aggredata.client.events.MenuClickedHandler;
import com.ted.aggredata.client.events.TabClickedEvent;
import com.ted.aggredata.client.events.TabClickedHandler;
import com.ted.aggredata.client.guiService.TEDAsyncCallback;
import com.ted.aggredata.client.guiService.UserSessionService;
import com.ted.aggredata.client.guiService.UserSessionServiceAsync;
import com.ted.aggredata.client.panels.graph.GraphPanel;
import com.ted.aggredata.client.panels.login.LoginPanel;
import com.ted.aggredata.client.resources.lang.DashboardConstants;

import java.util.logging.Logger;

public class MainPanel extends Composite {


    final UserSessionServiceAsync userSessionService = (UserSessionServiceAsync) GWT.create(UserSessionService.class);

    interface MyUiBinder extends UiBinder<Widget, MainPanel> {
    }


    static Logger logger = Logger.getLogger(MainPanel.class.toString());
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    AbsolutePanel contentPanel;
    @UiField
    AbsolutePanel titlePanel;
    @UiField
    AdminNavigationPanel adminNavigationPanel;

    final DashboardTabPanel graphDashboardPanel;
    final DashboardTabPanel profileDashboardPanel;

    DashboardConstants dashboardConstants = GWT.create(DashboardConstants.class);

    public MainPanel() {
        initWidget(uiBinder.createAndBindUi(this));

        graphDashboardPanel = new DashboardTabPanel(new String[]{dashboardConstants.month(), dashboardConstants.day(), dashboardConstants.hour(), dashboardConstants.minute()});
        profileDashboardPanel = new DashboardTabPanel(new String[]{dashboardConstants.accountSettings(), dashboardConstants.accountGroups(), dashboardConstants.accountTEDS()});

        //Set default view
        graphDashboardPanel.setSelectedTab(0);
        titlePanel.add(graphDashboardPanel, 0, 0);
        contentPanel.add(new GraphPanel("Monthly Graphing View"));


        //Add handlers
        adminNavigationPanel.addMenuClickedHandler(new MenuClickedHandler() {
            @Override
            public void onMenuClicked(MenuClickedEvent event) {
                titlePanel.clear();
                contentPanel.clear();
                if (event.getMenuSelection() == MenuClickedEvent.MenuOptions.ADMIN) {
                    titlePanel.add(new Label("System Administration"));
                    contentPanel.add(new GraphPanel("OWNER View"));
                } else if (event.getMenuSelection() == MenuClickedEvent.MenuOptions.ENERGY) {
                    titlePanel.add(graphDashboardPanel, 0, 0);
                    contentPanel.add(new GraphPanel("Monthly Graphing View"));
                    graphDashboardPanel.setSelectedTab(0);
                } else if (event.getMenuSelection() == MenuClickedEvent.MenuOptions.PROFILE) {
                    titlePanel.add(profileDashboardPanel, 0, 0);
                    profileDashboardPanel.setSelectedTab(0);
                    contentPanel.add(new GraphPanel("USER ACCOUNT SETTINGS"));
                } else {
                    logger.info("User has slected to logout");
                    userSessionService.logoff(new TEDAsyncCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            logger.info("User has sucessfully logged out");
                            RootPanel.get("aggreDataSlot").clear();
                            RootPanel.get("aggreDataSlot").add(new LoginPanel());
                        }
                    });


                }

            }
        });

        profileDashboardPanel.addTabClickedHandler(new TabClickedHandler() {
            @Override
            public void onTabClicked(TabClickedEvent event) {
                contentPanel.clear();

                switch (event.getTabIndex()) {
                    case 0: {
                        contentPanel.add(new GraphPanel("USER ACCOUNT SETTINGS"));
                        break;
                    }
                    case 1: {
                        contentPanel.add(new GraphPanel("GROUPS OF GATEWAYS"));
                        break;
                    }
                    case 2: {
                        contentPanel.add(new GraphPanel("GATEWAYS"));
                        break;
                    }
                }
            }
        });

        graphDashboardPanel.addTabClickedHandler(new TabClickedHandler() {
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
