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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.ted.aggredata.client.Aggredata;
import com.ted.aggredata.client.guiService.GWTUserService;
import com.ted.aggredata.client.guiService.GWTUserServiceAsync;
import com.ted.aggredata.client.guiService.TEDAsyncCallback;
import com.ted.aggredata.client.panels.profile.gateways.MTUListRow;
import com.ted.aggredata.client.panels.profile.gateways.MTUListRowHeader;
import com.ted.aggredata.client.widgets.HugeButton;
import com.ted.aggredata.client.widgets.LargeButton;
import com.ted.aggredata.model.Gateway;
import com.ted.aggredata.model.MTU;
import com.ted.aggredata.model.User;

import java.util.List;
import java.util.logging.Logger;

public class UserButtonPanel extends Composite {
    User user;
    static Logger logger = Logger.getLogger(UserButtonPanel.class.toString());

    interface MyUiBinder extends UiBinder<Widget, UserButtonPanel> {
    }
    List<User> userList = UserPanel.getUserList();
    private String uname = "";
    private int unameLength = 5;
    private String password = "";
    private int passLength = 5;
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    final GWTUserServiceAsync gwtUserService = (GWTUserServiceAsync) GWT.create(GWTUserService.class);
    @UiField
    HorizontalPanel userButtonPanel;
    @UiField
    CaptionPanel captionPanel;
    @UiField
    LargeButton deleteUser;
    @UiField
    LargeButton createUser;
    @UiField
    LargeButton changePassword;
    @UiField
    LargeButton changeUsername;

    public UserButtonPanel() {
        initWidget(uiBinder.createAndBindUi(this));
        captionPanel.setCaptionHTML("<span style='color:white'>Options</span>");
        changePassword.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                changePword();
            }
        });

        changeUsername.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                user = UserSelectionPanel.getSelectedUser();
                changeUname();
            }
        });

        createUser.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                addUser();
            }
        });

        deleteUser.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                //deleteUser();
            }
        });
    }

    private  void addUser()
    {
        user = UserSelectionPanel.getSelectedUser();
        gwtUserService.createUser(user, new TEDAsyncCallback<User>() {
            @Override
            public void onSuccess(User result) {
                Window.alert("User added.");
            }
        });
    }

    private  void deleteUser()
    {
        user = UserSelectionPanel.getSelectedUser();
        gwtUserService.deleteUser(user, new TEDAsyncCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                UserPanel.userList.remove(user);
                if (userList.size() == 0) UserSelectionPanel.userListBox.setSelectedIndex(-1);
                else UserSelectionPanel.userListBox.setSelectedIndex(0);
            }
        });
    }
    
    private void changeUname() {
        boolean confirm;
        user = UserSelectionPanel.getSelectedUser();
        uname = Window.prompt("Please enter in a new username", "");
        if (uname.length() >= unameLength){
            confirm = Window.confirm("Are you sure?");
            if (confirm) {
                gwtUserService.changeUsername(user, uname, new TEDAsyncCallback<User>() {
                    @Override
                    public void onSuccess(User result) {
                        Window.alert("Username Changed.");
                    }
                });
            }
        }
        else if (uname.length() < unameLength & uname.length() > 0 )
        {
            Window.alert("Username must be " + unameLength + " characters or greater.");
        }
        else {
            Window.alert("No Username was entered.");
        }
    }

    private void changePword() {
        boolean confirm;
        user = UserSelectionPanel.getSelectedUser();
        password = Window.prompt("Please enter in a new password", "");
        if (password.length() >= passLength){
            confirm = Window.confirm("Are you sure?");
            if (confirm) {
                gwtUserService.changePassword(user, password, new TEDAsyncCallback<User>() {
                    @Override
                    public void onSuccess(User result) {
                        Window.alert("Password Changed.");
                    }
                });
            }
        }
        else if (password.length() < passLength & password.length() > 0 )
        {
            Window.alert("Password must be " + passLength + " characters or greater.");
        }
        else
        {
            Window.alert("No Password was entered.");
        }
    }


}
