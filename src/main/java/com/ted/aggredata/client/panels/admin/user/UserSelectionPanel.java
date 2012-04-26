package com.ted.aggredata.client.panels.admin.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.ted.aggredata.client.events.UserSelectedEvent;
import com.ted.aggredata.client.events.UserSelectedHandler;
import com.ted.aggredata.client.events.UserSelectedEvent;
import com.ted.aggredata.client.resources.lang.DashboardConstants;
import com.ted.aggredata.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
public class UserSelectionPanel extends Composite {

    static Logger logger = Logger.getLogger(UserSelectionPanel.class.toString());

    interface MyUiBinder extends UiBinder<Widget, UserSelectionPanel> {
    }
    
    final private HandlerManager handlerManager;
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    
    @UiField
    ListBox userListBox;
    @UiField
    CaptionPanel captionPanel;

    List<User> userList = new ArrayList<User>();
    User selectedUser;

    public UserSelectionPanel() {
        initWidget(uiBinder.createAndBindUi(this));
        captionPanel.setCaptionHTML("<span style='color:white'>" + DashboardConstants.INSTANCE.yourUsers() + "</span>");

        handlerManager = new HandlerManager(this);
        
        userListBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                fireSelectedGroup();
            }
        });
    }

    public void setGatewayList(List<User> userList) {
        this.userList = userList;
        redrawUserList();
        if (userListBox.getItemCount() > 0) {
            userListBox.setSelectedIndex(0);
            fireSelectedGroup();
        }
    }

    public void redrawUserList() {
        int selectedIndex = userListBox.getSelectedIndex();
        userListBox.clear();
        for (User user : userList) {
            if (logger.isLoggable(Level.FINE)) logger.fine("Adding gateway " + user + " to list box");
            userListBox.addItem(user.getUsername(), user.getId().toString());
        }
        userListBox.setSelectedIndex(selectedIndex);
    }
    
    private void fireSelectedGroup() {
        int index = userListBox.getSelectedIndex();
        if (logger.isLoggable(Level.FINE)) logger.fine("Row " + index + " selected");
        Long userID = new Long(userListBox.getValue(index));

        for (User user : userList) {
            if (user.getId().equals(userID)) {
                this.selectedUser = user;
                break;
            }
        }

        if (selectedUser != null) {
            if (logger.isLoggable(Level.FINE)) logger.fine("Gateway selected: " + selectedUser);
            handlerManager.fireEvent(new UserSelectedEvent(selectedUser));
        }
    }

    public HandlerRegistration addUserSelectedHandler(UserSelectedHandler handler) {
        return handlerManager.addHandler(UserSelectedEvent.TYPE, handler);
    }
}
