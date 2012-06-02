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
    final DashboardConstants dc = DashboardConstants.INSTANCE;
    interface MyUiBinder extends UiBinder<Widget, UserButtonPanel> {
    }

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
                enableUser();
            };
        });

        disableButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                disableUser();
            };
        });
    }

    /**
     * Disable the user if the button is clicked
     */
    private void disableUser()
    {
        //get the current index
        final int index = UserSelectionPanel.userListBox.getSelectedIndex();
        gwtUserService.changeUserStatus(UserSelectionPanel.getSelectedUser(), false, new TEDAsyncCallback<User>() {
            @Override
            public void onSuccess(User result) {
                //redraw the list on success
                UserSelectionPanel.updateList(index);
                final OKPopup okPopup = new OKPopup(dc.disableUser(), UserSelectionPanel.getSelectedUser().getUsername() + ": " + dc.disableUserMsg());
            }
        });
    }

    /**
     * Enable the user if the button is clicked
     */
    private void enableUser()
    {
        //Get the current index to redraw the list
        final int index = UserSelectionPanel.userListBox.getSelectedIndex();
        gwtUserService.changeUserStatus(UserSelectionPanel.getSelectedUser(), true, new TEDAsyncCallback<User>() {
            @Override
            public void onSuccess(User result) {
                //redraw the list
                UserSelectionPanel.updateList(index);
                final OKPopup okPopup = new OKPopup(dc.enableUser(), UserSelectionPanel.getSelectedUser().getUsername() + ": " + dc.enableUserMsg());
            }
        });
    }

    /**
     * Add the user if the button is clicked.
     */
    private void addUser()
    {
        //create a new user
        final User newUser = new User();
        final CreateUserPopup createUserPopup = new CreateUserPopup();
        createUserPopup.addCloseHandler(new CloseHandler<PopupPanel>() {
            @Override
            //if the data is filled in correctly on the popup, then create the new user.
            public void onClose(CloseEvent<PopupPanel> popupPanelCloseEvent) {
                final int index = UserSelectionPanel.userListBox.getSelectedIndex();
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
                                logger.info("User creation successful.");
                                //redraw the list
                                UserSelectionPanel.updateList(index);
                            } else {
                                logger.severe("Unexpected result. User was not created.");
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * Delete the user if the button is clicked.
     */
    private  void deleteUser()
    {
        user = UserSelectionPanel.getSelectedUser();
        // Send the user name to the verification screen so that the user can verify which one they will delete.
        final YesNoPopup popup = new YesNoPopup(dc.deleteUser(), user.getUsername() + ": " + dc.deleteUserVerification());
        popup.addCloseHandler(new CloseHandler<PopupPanel>() {
            @Override
            public void onClose(CloseEvent<PopupPanel> popupPanelCloseEvent) {
                final int index = UserSelectionPanel.userListBox.getSelectedIndex();
                if (popup.getValue() == YesNoPopup.YES) {
                    gwtUserService.deleteUser(user, new TEDAsyncCallback<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //update the list with the user in the list prior to the one deleted.
                            UserSelectionPanel.updateList(index - 1);
                        }
                    });
                }
            }
        });
    }

    /**
     * Change the user's email if the button is clicked.
     */
    private void changeEmail() {
        user = UserSelectionPanel.getSelectedUser();
        logger.fine("Change Email Clicked");
        final ChangeEmailPopup changeEmailPopup = new ChangeEmailPopup();
        changeEmailPopup.center();
        changeEmailPopup.setPopupPosition(changeEmailPopup.getAbsoluteLeft(), 100);
        changeEmailPopup.addCloseHandler(new CloseHandler<PopupPanel>() {

            //If the data is filled in correct, and the user verifies, then change the email.
            @Override
            public void onClose(CloseEvent<PopupPanel> popupPanelCloseEvent) {
                final int index = UserSelectionPanel.userListBox.getSelectedIndex();
                if (changeEmailPopup.getValue() == changeEmailPopup.OK){
                    logger.fine("Changing Email to  " + changeEmailPopup.getEmail());
                    final YesNoPopup popup = new YesNoPopup(dc.changeEmail(), dc.changeEmailVerification());
                    popup.addCloseHandler(new CloseHandler<PopupPanel>() {
                        @Override
                        public void onClose(CloseEvent<PopupPanel> popupPanelCloseEvent) {
                            if (popup.getValue() == YesNoPopup.YES) {
                                gwtUserService.changeUsername(user, changeEmailPopup.getEmail(), new TEDAsyncCallback<User>() {
                                    @Override
                                    public void onSuccess(User result) {

                                        //redraw the list
                                        UserSelectionPanel.updateList(index);
                                        final OKPopup okPopup = new OKPopup(dc.changeEmail(), dc.emailChanged());
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
        changeEmailPopup.show();
    }

    /**
     * Change the user's password if the button is clicked.
     */
    private void changePword() {
        //Get the current user
        user = UserSelectionPanel.getSelectedUser();
        logger.fine("Change Password Clicked");

        //Set up the change password popup
        final ChangePasswordPopup changePasswordPopup = new ChangePasswordPopup();
        changePasswordPopup.center();
        changePasswordPopup.setPopupPosition(changePasswordPopup.getAbsoluteLeft(), 100);
        changePasswordPopup.addCloseHandler(new CloseHandler<PopupPanel>() {
            @Override
            public void onClose(CloseEvent<PopupPanel> popupPanelCloseEvent) {
                final int index = UserSelectionPanel.userListBox.getSelectedIndex();

                //If the password is of the right length and the user clicks OK then change the password
                if (changePasswordPopup.getValue() == changePasswordPopup.OK){
                    logger.fine("Changing Password to  " + changePasswordPopup.getPassword());
                    final YesNoPopup popup = new YesNoPopup(dc.changePassword(), dc.changePassVerification());
                    popup.addCloseHandler(new CloseHandler<PopupPanel>() {
                        @Override
                        public void onClose(CloseEvent<PopupPanel> popupPanelCloseEvent) {
                            if (popup.getValue() == YesNoPopup.YES) {
                                gwtUserService.changePassword(user, changePasswordPopup.getPassword(), new TEDAsyncCallback<User>() {
                                    @Override
                                    public void onSuccess(User result) {

                                        //redraw the list
                                        UserSelectionPanel.updateList(index);
                                        final OKPopup okPopup = new OKPopup(dc.changePassword(), dc.passwordChanged());
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
        changePasswordPopup.show();
    }

    /**
     * call for the refresh button to update the current user list.
     */
    private void refreshList(){
        final int index = UserSelectionPanel.userListBox.getSelectedIndex();
        UserSelectionPanel.updateList(index);
    }


}
