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
package com.ted.aggredata.client.panels.admin.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.ted.aggredata.client.events.UserSelectedEvent;
import com.ted.aggredata.client.events.UserSelectedHandler;
import com.ted.aggredata.client.guiService.GWTUserService;
import com.ted.aggredata.client.guiService.GWTUserServiceAsync;
import com.ted.aggredata.client.panels.admin.user.UserSelectionPanel;
import com.ted.aggredata.client.panels.admin.user.UserDetailsPanel;
import com.ted.aggredata.model.User;

import java.util.List;
import java.util.logging.Logger;

public class UserPanel extends Composite {

    static Logger logger = Logger.getLogger(UserPanel.class.toString());

    final GWTUserServiceAsync userService = (GWTUserServiceAsync) GWT.create(GWTUserService.class);
    interface MyUiBinder extends UiBinder<Widget, UserPanel> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    UserDetailsPanel UserDetailsPanel;
    @UiField
    UserSelectionPanel UserSelectionPanel;
    List<User> userList;

    public UserPanel() {
        initWidget(uiBinder.createAndBindUi(this));

//        UserDetailsPanel.setEnabled(false);
//
//        //Add a handler to update the details section when a gateway is selected
//        UserSelectionPanel.addUserSelectedHandler(new UserSelectedHandler() {
//            @Override
//            public void onGatewaySelected(UserSelectedEvent event) {
//                logger.fine("User Selected: " + event.getUser());
//                final User selectedUser = event.getUser();
//                UserDetailsPanel.setUser(selectedUser);
//            }
//        });

    }

}