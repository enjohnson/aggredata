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
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.ted.aggredata.client.Aggredata;
import com.ted.aggredata.client.dialogs.LoadingPopup;
import com.ted.aggredata.client.events.*;
import com.ted.aggredata.client.guiService.TEDAsyncCallback;
import com.ted.aggredata.client.guiService.UserSessionService;
import com.ted.aggredata.client.guiService.UserSessionServiceAsync;
import com.ted.aggredata.client.panels.admin.user.UserPanel;
import com.ted.aggredata.client.panels.graph.GraphOptionChangeable;
import com.ted.aggredata.client.panels.graph.day.DayPanel;
import com.ted.aggredata.client.panels.graph.hour.HourPanel;
import com.ted.aggredata.client.panels.graph.minute.MinutePanel;
import com.ted.aggredata.client.panels.graph.month.MonthPanel;
import com.ted.aggredata.client.panels.login.LoginPanel;
import com.ted.aggredata.client.panels.profile.activate.ActivationPanel;
import com.ted.aggredata.client.panels.profile.gateways.GatewaysPanel;
import com.ted.aggredata.client.panels.profile.groups.GroupsPanel;
import com.ted.aggredata.client.panels.profile.settings.SettingsPanel;
import com.ted.aggredata.client.panels.side.GraphSidePanel;
import com.ted.aggredata.client.resources.lang.DashboardConstants;
import com.ted.aggredata.model.Enums;

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
    final GraphSidePanel graphSidePanel;



    final LoadingPopup loadingPopup = new LoadingPopup();


    DashboardConstants dashboardConstants = GWT.create(DashboardConstants.class);

    //Event handler if any of the left hand side graphing options have changed
    GraphOptionsChangedHandler graphOptionsChangedHandler = new GraphOptionsChangedHandler() {
        @Override
        public void onGraphingOptionsChanged(GraphOptionsChangedEvent event) {
               if (contentPanel.getWidget(0) instanceof GraphOptionChangeable) {
                   GraphOptionChangeable graphOptionChangeable = (GraphOptionChangeable) contentPanel.getWidget(0);
                   graphSidePanel.setEnabled(false);
                   loadingPopup.show();
                   graphOptionChangeable.onGraphOptionChange(event);
               }
        }
    };

    GraphLoadedHandler graphLoadedHandler = new GraphLoadedHandler() {
        @Override
        public void onGraphLoaded(GraphLoadedEvent event) {
            logger.fine("Graph Loaded Event Caught");
            graphSidePanel.setEnabled(true);
            loadingPopup.hide();
        }
    };




    public MainPanel() {
        initWidget(uiBinder.createAndBindUi(this));

        graphDashboardPanel = new DashboardTabPanel(new String[]{dashboardConstants.month(), dashboardConstants.day(), dashboardConstants.hour(), dashboardConstants.minute()});
        profileDashboardPanel = new DashboardTabPanel(new String[]{dashboardConstants.accountSettings(), dashboardConstants.accountGroups(), dashboardConstants.accountTEDS(), dashboardConstants.accountActivate()});
        systemAdministrationDashboardPanel = new DashboardTabPanel(new String[]{dashboardConstants.systemUsers()});

        graphSidePanel = new GraphSidePanel();
        graphSidePanel.addGraphOptionsChangedHandler(graphOptionsChangedHandler);




        //Add handlers
        adminNavigationPanel.addMenuClickedHandler(new MenuClickedHandler() {
            @Override
            public void onMenuClicked(MenuClickedEvent event) {
                tabNavigationPanel.clear();
                contentPanel.clear();
                sidePanel.clear();
                loadingPopup.hide();
                if (event.getMenuSelection() == MenuClickedEvent.MenuOptions.ADMIN) {
                    tabNavigationPanel.add(systemAdministrationDashboardPanel);
                    contentPanel.add(new UserPanel());
                    systemAdministrationDashboardPanel.setSelectedTab(0);
                } else if (event.getMenuSelection() == MenuClickedEvent.MenuOptions.ENERGY) {
                    tabNavigationPanel.add(graphDashboardPanel, 0, 0);
                    contentPanel.add(new MonthPanel(graphLoadedHandler));
                    sidePanel.add(graphSidePanel, 0, 0);
                    graphSidePanel.reset();
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
                            RootPanel.get(Aggredata.ROOT_PANEL).clear();
                            RootPanel.get(Aggredata.ROOT_PANEL).add(new LoginPanel());
                        }
                    });


                }

            }
        });

        profileDashboardPanel.addTabClickedHandler(new TabClickedHandler() {
            @Override
            public void onTabClicked(TabClickedEvent event) {
                contentPanel.clear();
                loadingPopup.hide();

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
                    case 3: {
                        contentPanel.add(new ActivationPanel());
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
                        contentPanel.add(new MonthPanel(graphLoadedHandler));
                        graphSidePanel.setHistoryType(Enums.HistoryType.MONTHLY);
                        graphSidePanel.fireEvent();
                        break;
                    }
                    case 1: {
                        contentPanel.add(new DayPanel(graphLoadedHandler));
                        graphSidePanel.setHistoryType(Enums.HistoryType.DAILY);
                        graphSidePanel.fireEvent();
                        break;
                    }
                    case 2: {
                        contentPanel.add(new HourPanel(graphLoadedHandler));
                        graphSidePanel.setHistoryType(Enums.HistoryType.HOURLY);
                        graphSidePanel.fireEvent();
                        break;
                    }
                    case 3: {
                        contentPanel.add(new MinutePanel(graphLoadedHandler));
                        graphSidePanel.setHistoryType(Enums.HistoryType.MINUTE);
                        graphSidePanel.fireEvent();
                        break;
                    }

                }

            }
        });

        systemAdministrationDashboardPanel.addTabClickedHandler(new TabClickedHandler() {
            @Override
            public void onTabClicked(TabClickedEvent event) {
                contentPanel.clear();
                loadingPopup.hide();
                switch (event.getTabIndex()) {
                    case 0: {
                        contentPanel.add(new UserPanel());
                        break;
                    }
                }

            }
        });


        //Redirect the user to the gateway page if there are not gateways assigned to the system
        if (Aggredata.GLOBAL.getShowActivation()) {
            profileDashboardPanel.setSelectedTab(3);
            adminNavigationPanel.setSelectedValue(0);
            tabNavigationPanel.add(profileDashboardPanel, 0, 0);
            contentPanel.add(new ActivationPanel());

            loadingPopup.hide();
        } else {
            //Go to the month page by default
            graphDashboardPanel.setSelectedTab(0);
            adminNavigationPanel.setSelectedValue(1);
            tabNavigationPanel.add(graphDashboardPanel, 0, 0);
            contentPanel.add(new MonthPanel(graphLoadedHandler));
            sidePanel.add(graphSidePanel, 0, 0);
            graphSidePanel.setHistoryType(Enums.HistoryType.MONTHLY);
            graphSidePanel.reset();
        }


    }

}
