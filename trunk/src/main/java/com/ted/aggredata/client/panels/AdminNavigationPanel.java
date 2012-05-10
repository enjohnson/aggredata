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
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.ted.aggredata.client.Aggredata;
import com.ted.aggredata.client.events.MenuClickedEvent;
import com.ted.aggredata.client.events.MenuClickedHandler;
import com.ted.aggredata.client.resources.lang.DashboardConstants;
import com.ted.aggredata.client.widgets.MenuItem;
import com.ted.aggredata.model.User;

import java.util.logging.Logger;

public class AdminNavigationPanel extends Composite implements HasHandlers {

    interface MyUiBinder extends UiBinder<Widget, AdminNavigationPanel> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    static Logger logger = Logger.getLogger(MainPanel.class.toString());

    @UiField
    Label usernameLabel;
    @UiField
    Label helloLabel;
    @UiField
    HorizontalPanel menuOptionsPanel;

    final MenuItem accountMenuItem;
    final MenuItem adminMenuItem;
    final MenuItem energyMenuItem;
    final MenuItem logoutMenuItem;
    final private HandlerManager handlerManager;

    DashboardConstants myConstants = GWT.create(DashboardConstants.class);

    public AdminNavigationPanel() {


        initWidget(uiBinder.createAndBindUi(this));
        handlerManager = new HandlerManager(this);

        helloLabel.setText(myConstants.hello());
        usernameLabel.setText(Aggredata.GLOBAL.getSessionUser().getFirstName());


        String width = "65px";

        accountMenuItem = new MenuItem(myConstants.profile(), true, width, "12px");
        adminMenuItem = new MenuItem(myConstants.admin(), false, width, "12px");
        energyMenuItem = new MenuItem(myConstants.energyData(), false, width, "12px");
        logoutMenuItem = new MenuItem(myConstants.logout(), false, width, "12px");

        menuOptionsPanel.add(accountMenuItem);
        menuOptionsPanel.add(energyMenuItem);

        if (Aggredata.GLOBAL != null && Aggredata.GLOBAL.getSessionUser() != null && Aggredata.GLOBAL.getSessionUser().getRole().equals(User.ROLE_ADMIN)){
            logger.fine("User is an admin");
            menuOptionsPanel.add(adminMenuItem);
        }

        menuOptionsPanel.add(logoutMenuItem);


        accountMenuItem.setSelected(false);
        energyMenuItem.setSelected(true);
        adminMenuItem.setSelected(false);
        logoutMenuItem.setSelected(false);


        accountMenuItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!accountMenuItem.isSelected()) {
                    accountMenuItem.setSelected(true);
                    energyMenuItem.setSelected(false);
                    adminMenuItem.setSelected(false);
                    logoutMenuItem.setSelected(false);
                    handlerManager.fireEvent(new MenuClickedEvent(MenuClickedEvent.MenuOptions.PROFILE));
                }
            }
        });

        energyMenuItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!energyMenuItem.isSelected()) {
                    accountMenuItem.setSelected(false);
                    energyMenuItem.setSelected(true);
                    adminMenuItem.setSelected(false);
                    logoutMenuItem.setSelected(false);
                    handlerManager.fireEvent(new MenuClickedEvent(MenuClickedEvent.MenuOptions.ENERGY));
                }
            }
        });

        adminMenuItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!adminMenuItem.isSelected()) {
                    accountMenuItem.setSelected(false);
                    energyMenuItem.setSelected(false);
                    adminMenuItem.setSelected(true);
                    logoutMenuItem.setSelected(false);
                    handlerManager.fireEvent(new MenuClickedEvent(MenuClickedEvent.MenuOptions.ADMIN));
                }

            }
        });

        logoutMenuItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!logoutMenuItem.isSelected()) {
                    accountMenuItem.setSelected(false);
                    energyMenuItem.setSelected(false);
                    adminMenuItem.setSelected(false);
                    logoutMenuItem.setSelected(true);
                    handlerManager.fireEvent(new MenuClickedEvent(MenuClickedEvent.MenuOptions.LOGOUT));
                }
            }
        });


    }

    public HandlerRegistration addMenuClickedHandler(MenuClickedHandler handler) {
        return handlerManager.addHandler(MenuClickedEvent.TYPE, handler);
    }
}
