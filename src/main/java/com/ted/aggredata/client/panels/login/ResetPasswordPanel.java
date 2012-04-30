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

package com.ted.aggredata.client.panels.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.ted.aggredata.client.Aggredata;
import com.ted.aggredata.client.dialogs.PasswordResetPopup;
import com.ted.aggredata.client.guiService.TEDAsyncCallback;
import com.ted.aggredata.client.guiService.UserSessionService;
import com.ted.aggredata.client.guiService.UserSessionServiceAsync;

import java.util.logging.Logger;


public class ResetPasswordPanel extends Composite {


    static Logger logger = Logger.getLogger(ResetPasswordPanel.class.toString());
    final UserSessionServiceAsync userSessionService = (UserSessionServiceAsync) GWT.create(UserSessionService.class);


    interface MyUiBinder extends UiBinder<Widget, ResetPasswordPanel> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField
    Label promptLabel;

    public ResetPasswordPanel() {
        initWidget(uiBinder.createAndBindUi(this));

        if (Aggredata.GLOBAL==null || Aggredata.GLOBAL.getServerInfo() == null || !Aggredata.GLOBAL.getServerInfo().isAllowPasswordReset()) {
            logger.fine("Password Reset is disabled.");
            promptLabel.setVisible(false);
        }

        //Handle the label being clicked
        promptLabel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                logger.fine("Reset Password Clicked");
                final PasswordResetPopup passwordResetPopup = new PasswordResetPopup();
                passwordResetPopup.center();
                passwordResetPopup.setPopupPosition(passwordResetPopup.getAbsoluteLeft(), 100);
                passwordResetPopup.addCloseHandler(new CloseHandler<PopupPanel>() {
                    @Override
                    public void onClose(CloseEvent<PopupPanel> popupPanelCloseEvent) {
                        if (passwordResetPopup.getValue() == PasswordResetPopup.OK){
                            logger.fine("Resetting the password for " + passwordResetPopup.getUsername());
                            userSessionService.resetPassword(passwordResetPopup.getUsername(), new TEDAsyncCallback<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    logger.fine("Password Reset");
                                }
                            });
                        }
                    }
                });
                passwordResetPopup.show();


            }
        });

    }



}
