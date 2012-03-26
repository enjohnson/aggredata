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
import com.google.gwt.user.client.ui.*;
import com.ted.aggredata.client.events.MenuClickedEvent;
import com.ted.aggredata.client.events.MenuClickedHandler;
import com.ted.aggredata.client.events.TabClickedEvent;
import com.ted.aggredata.client.events.TabClickedHandler;
import com.ted.aggredata.client.guiService.TEDAsyncCallback;
import com.ted.aggredata.client.guiService.UserSessionService;
import com.ted.aggredata.client.guiService.UserSessionServiceAsync;
import com.ted.aggredata.client.panels.admin.system.ServerPanel;
import com.ted.aggredata.client.panels.admin.user.UserPanel;
import com.ted.aggredata.client.panels.graph.day.DayPanel;
import com.ted.aggredata.client.panels.graph.hour.HourPanel;
import com.ted.aggredata.client.panels.graph.minute.MinutePanel;
import com.ted.aggredata.client.panels.graph.month.MonthPanel;
import com.ted.aggredata.client.panels.login.LoginPanel;
import com.ted.aggredata.client.panels.profile.gateways.GatewaysPanel;
import com.ted.aggredata.client.panels.profile.groups.GroupsPanel;
import com.ted.aggredata.client.panels.profile.settings.SettingsPanel;
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
    AbsolutePanel tabNavigationPanel;
    @UiField
    AdminNavigationPanel adminNavigationPanel;
    @UiField
    AbsolutePanel sidePanel;

    final DashboardTabPanel graphDashboardPanel;
    final DashboardTabPanel profileDashboardPanel;
    final DashboardTabPanel systemAdministrationDashboardPanel;

    DashboardConstants dashboardConstants = GWT.create(DashboardConstants.class);

    public MainPanel() {
        initWidget(uiBinder.createAndBindUi(this));

        graphDashboardPanel = new DashboardTabPanel(new String[]{dashboardConstants.month(), dashboardConstants.day(), dashboardConstants.hour(), dashboardConstants.minute()});
        profileDashboardPanel = new DashboardTabPanel(new String[]{dashboardConstants.accountSettings(), dashboardConstants.accountGroups(), dashboardConstants.accountTEDS()});
        systemAdministrationDashboardPanel = new DashboardTabPanel(new String[]{dashboardConstants.systemUsers(), dashboardConstants.systemServer()});
        //Set default view
        graphDashboardPanel.setSelectedTab(0);
        tabNavigationPanel.add(graphDashboardPanel, 0, 0);
        contentPanel.add(new MonthPanel());


        //Add handlers
        adminNavigationPanel.addMenuClickedHandler(new MenuClickedHandler() {
            @Override
            public void onMenuClicked(MenuClickedEvent event) {
                tabNavigationPanel.clear();
                contentPanel.clear();
                if (event.getMenuSelection() == MenuClickedEvent.MenuOptions.ADMIN) {
                    tabNavigationPanel.add(systemAdministrationDashboardPanel);
                    contentPanel.add(new UserPanel());
                    systemAdministrationDashboardPanel.setSelectedTab(0);

                } else if (event.getMenuSelection() == MenuClickedEvent.MenuOptions.ENERGY) {
                    tabNavigationPanel.add(graphDashboardPanel, 0, 0);
                    contentPanel.add(new MonthPanel());
                    graphDashboardPanel.setSelectedTab(0);
                } else if (event.getMenuSelection() == MenuClickedEvent.MenuOptions.PROFILE) {
                    tabNavigationPanel.add(profileDashboardPanel, 0, 0);
                    profileDashboardPanel.setSelectedTab(0);
                    contentPanel.add(new SettingsPanel());
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
                        contentPanel.add(new SettingsPanel());
                        break;
                    }
                    case 1: {
                        contentPanel.add(new GroupsPanel());
                        break;
                    }
                    case 2: {
                        contentPanel.add(new GatewaysPanel());
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
                        contentPanel.add(new MonthPanel());
                        break;
                    }
                    case 1: {
                        contentPanel.add(new DayPanel());
                        break;
                    }
                    case 2: {
                        contentPanel.add(new HourPanel());
                        break;
                    }
                    case 3: {
                        contentPanel.add(new MinutePanel());
                        break;
                    }

                }

            }
        });

        systemAdministrationDashboardPanel.addTabClickedHandler(new TabClickedHandler() {
            @Override
            public void onTabClicked(TabClickedEvent event) {
                contentPanel.clear();

                switch (event.getTabIndex()) {
                    case 0: {
                        contentPanel.add(new UserPanel());
                        break;
                    }
                    case 1: {
                        contentPanel.add(new ServerPanel());
                        break;
                    }
                }

            }
        });

    }

}
