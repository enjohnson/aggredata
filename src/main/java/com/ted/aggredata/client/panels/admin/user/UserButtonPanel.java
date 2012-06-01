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
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.ted.aggredata.client.Aggredata;
import com.ted.aggredata.client.dialogs.ChangeEmailPopup;
import com.ted.aggredata.client.dialogs.ChangePasswordPopup;
import com.ted.aggredata.client.dialogs.OKPopup;
import com.ted.aggredata.client.dialogs.YesNoPopup;
import com.ted.aggredata.client.dialogs.CreateUserPopup;
import com.ted.aggredata.client.guiService.*;
import com.ted.aggredata.client.resources.lang.DashboardConstants;
import com.ted.aggredata.client.widgets.LargeButton;
import com.ted.aggredata.model.User;

import java.util.List;
import java.util.logging.Logger;

public class UserButtonPanel extends Composite {
    User user;
    static Logger logger = Logger.getLogger(UserButtonPanel.class.toString());

    interface MyUiBinder extends UiBinder<Widget, UserButtonPanel> {
    }
    //List<User> userList = UserPanel.getUserList();
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
    LargeButton changeEmail;
    @UiField
    LargeButton enableButton;
    @UiField
    LargeButton disableButton;

    public UserButtonPanel() {
        initWidget(uiBinder.createAndBindUi(this));
        captionPanel.setCaptionHTML("<span style='color:white'>Options</span>");
        changePassword.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                changePword();
            }
        });

        changeEmail.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                changeEmail();
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
                deleteUser();
            };
        });

        enableButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                gwtUserService.changeUserStatus(UserSelectionPanel.getSelectedUser(), true, new TEDAsyncCallback<User>() {
                    @Override
                    public void onSuccess(User result) {
                        final OKPopup okPopup = new OKPopup("Enable User", UserSelectionPanel.getSelectedUser().getUsername() + " has been enabled.");
                    }
                });
            };
        });

        disableButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                gwtUserService.changeUserStatus(UserSelectionPanel.getSelectedUser(), false, new TEDAsyncCallback<User>() {
                    @Override
                    public void onSuccess(User result) {
                        final OKPopup okPopup = new OKPopup("Disable User", UserSelectionPanel.getSelectedUser().getUsername() + " has been disabled.");
                    }
                });
            };
        });
    }

    private void addUser()
    {
        final DashboardConstants dc = DashboardConstants.INSTANCE;
        final User newUser = new User();
        final CreateUserPopup createUserPopup = new CreateUserPopup();
        createUserPopup.addCloseHandler(new CloseHandler<PopupPanel>() {
            @Override
            public void onClose(CloseEvent<PopupPanel> popupPanelCloseEvent) {
                if (createUserPopup.getValue() == CreateUserPopup.OK){
                    newUser.setFirstName(createUserPopup.getFirstName());
                    newUser.setLastName(createUserPopup.getLastName());
                    newUser.setUsername(createUserPopup.getEmail());
                    newUser.setId(UserSelectionPanel.userList.get(UserSelectionPanel.userList.size() - 1).getId() + 1);
                    newUser.setTimezone(createUserPopup.getTimezone());
                    gwtUserService.newUser(createUserPopup.getEmail(), createUserPopup.getPassword(), newUser, new TEDAsyncCallback<User>() {
                        @Override
                        public void onSuccess(User result) {
                            if (result.getUsername() != "") {
                                logger.info("User creation successful. Redirecting to success link");
                                UserSelectionPanel.userList.add(result);
                                if (UserSelectionPanel.userList.size() == 0) UserSelectionPanel.userListBox.setSelectedIndex(-1);
                                else UserSelectionPanel.userListBox.setSelectedIndex(0);
                                UserSelectionPanel.redrawUserList();
                                UserSelectionPanel.fireSelectedGroup();
                            } else {
                                logger.severe("Unexpected result. User was not created.");
                            }
                        }
                    });
                }
            }
        });
    }

    private  void deleteUser()
    {
        final DashboardConstants dc = DashboardConstants.INSTANCE;
        user = UserSelectionPanel.getSelectedUser();
        final YesNoPopup popup = new YesNoPopup(dc.deleteUser(), user.getUsername() + ": " + dc.deleteUserVerification());
        popup.addCloseHandler(new CloseHandler<PopupPanel>() {
            @Override
            public void onClose(CloseEvent<PopupPanel> popupPanelCloseEvent) {
                if (popup.getValue() == YesNoPopup.YES) {
                    gwtUserService.deleteUser(user, new TEDAsyncCallback<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            for (int i = 0; i < UserSelectionPanel.userList.size(); i++) {
                                if (UserSelectionPanel.userList.get(i) == user)
                                    UserSelectionPanel.userList.remove(i);
                            }
                            if (UserSelectionPanel.userList.size() == 0) UserSelectionPanel.userListBox.setSelectedIndex(-1);
                            else UserSelectionPanel.userListBox.setSelectedIndex(0);
                            UserSelectionPanel.redrawUserList();
                            UserSelectionPanel.fireSelectedGroup();
                        }
                    });
                }
            }
        });
    }

    private void changeEmail() {
        user = UserSelectionPanel.getSelectedUser();
        logger.fine("Change Email Clicked");
        final ChangeEmailPopup changeEmailPopup = new ChangeEmailPopup();
        final DashboardConstants dc = DashboardConstants.INSTANCE;
        changeEmailPopup.center();
        changeEmailPopup.setPopupPosition(changeEmailPopup.getAbsoluteLeft(), 100);
        changeEmailPopup.addCloseHandler(new CloseHandler<PopupPanel>() {
            @Override
            public void onClose(CloseEvent<PopupPanel> popupPanelCloseEvent) {
                if (changeEmailPopup.getValue() == changeEmailPopup.OK){
                    logger.fine("Changing Email to  " + changeEmailPopup.getEmail());
                    if (changeEmailPopup.getEmail().length() >= passLength){
                        final YesNoPopup popup = new YesNoPopup(dc.changeEmail(), dc.changeEmailVerification());
                        popup.addCloseHandler(new CloseHandler<PopupPanel>() {
                            @Override
                            public void onClose(CloseEvent<PopupPanel> popupPanelCloseEvent) {
                                if (popup.getValue() == YesNoPopup.YES) {
                                    gwtUserService.changeUsername(user, changeEmailPopup.getEmail(), new TEDAsyncCallback<User>() {
                                        @Override
                                        public void onSuccess(User result) {
                                            for (int i = 0; i < UserSelectionPanel.userList.size(); i++) {
                                                if (UserSelectionPanel.userList.get(i).getId() == user.getId()){
                                                    UserSelectionPanel.userList.remove(i);
                                                    UserSelectionPanel.userList.add(result);
                                                }
                                            }
                                            if (UserSelectionPanel.userList.size() == 0) UserSelectionPanel.userListBox.setSelectedIndex(-1);
                                            else UserSelectionPanel.userListBox.setSelectedIndex(0);
                                            UserSelectionPanel.redrawUserList();
                                            UserSelectionPanel.fireSelectedGroup();
                                            final OKPopup okPopup = new OKPopup(dc.changeEmail(), "Email has been changed");
                                        }
                                    });
                                }
                            }
                        });
                    }
                    else if (changeEmailPopup.getEmail().length() < unameLength & changeEmailPopup.getEmail().length() > 0 )
                    {
                        final OKPopup okPopup = new OKPopup(dc.changeEmail(), "Email must be " + unameLength + " characters long.");
                    }
                    else
                    {
                        final OKPopup okPopup = new OKPopup(dc.changeEmail(), "No email was entered.");
                    }
                }
            }
        });
        changeEmailPopup.show();
    }

    private void changePword() {
        user = UserSelectionPanel.getSelectedUser();
        logger.fine("Change Password Clicked");
        final ChangePasswordPopup changePasswordPopup = new ChangePasswordPopup();
        changePasswordPopup.center();
        changePasswordPopup.setPopupPosition(changePasswordPopup.getAbsoluteLeft(), 100);
        changePasswordPopup.addCloseHandler(new CloseHandler<PopupPanel>() {
            @Override
            public void onClose(CloseEvent<PopupPanel> popupPanelCloseEvent) {
                if (changePasswordPopup.getValue() == changePasswordPopup.OK){
                    logger.fine("Changing Password to  " + changePasswordPopup.getPassword());
                    if (changePasswordPopup.getPassword().length() >= passLength){
                        final DashboardConstants dc = DashboardConstants.INSTANCE;
                        final YesNoPopup popup = new YesNoPopup(dc.changePassword(), dc.changePassVerification());
                        popup.addCloseHandler(new CloseHandler<PopupPanel>() {
                            @Override
                            public void onClose(CloseEvent<PopupPanel> popupPanelCloseEvent) {
                                if (popup.getValue() == YesNoPopup.YES) {
                                    gwtUserService.changePassword(user, changePasswordPopup.getPassword(), new TEDAsyncCallback<User>() {
                                        @Override
                                        public void onSuccess(User result) {
                                            final OKPopup okPopup = new OKPopup("Change Password", "Password has been changed.");
                                        }
                                    });
                                }
                            }
                        });
                    }
                    else if (changePasswordPopup.getPassword().length() < passLength & changePasswordPopup.getPassword().length() > 0 )
                    {
                        final OKPopup okPopup = new OKPopup("Change Password", "Password must be " + passLength + " characters long.");
                    }
                    else
                    {
                        final OKPopup okPopup = new OKPopup("Change Password", "No password was entered.");
                    }
                }
            }
        });
        changePasswordPopup.show();
    }


}
