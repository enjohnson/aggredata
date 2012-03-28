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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.ted.aggredata.client.Aggredata;
import com.ted.aggredata.client.Globals;
import com.ted.aggredata.client.guiService.TEDAsyncCallback;
import com.ted.aggredata.client.guiService.UserSessionService;
import com.ted.aggredata.client.guiService.UserSessionServiceAsync;
import com.ted.aggredata.client.panels.AggredataPanel;
import com.ted.aggredata.client.resources.lang.DashboardConstants;
import com.ted.aggredata.client.widgets.LargeButton;
import com.ted.aggredata.model.GlobalPlaceholder;
import com.ted.aggredata.model.User;

import java.util.logging.Level;
import java.util.logging.Logger;


public class LoginFormPanel extends Composite {

    @UiField FocusPanel mainPanel;
    @UiField TextBox loginBox;
    @UiField PasswordTextBox passwordTextBox;
    @UiField Label completionLabel1;
    @UiField Label completionLabel2;
    @UiField LargeButton submitButton;
    @UiField Label formErrorLabel;
    @UiField Label usernameLabel;
    @UiField Label passwordLabel;
    
    static Logger logger = Logger.getLogger(LoginFormPanel.class.toString());
    final UserSessionServiceAsync userSessionService = (UserSessionServiceAsync) GWT.create(UserSessionService.class);
    DashboardConstants dashboardConstants = GWT.create(DashboardConstants.class);

    interface MyUiBinder extends UiBinder<Widget, LoginFormPanel> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    public LoginFormPanel() {
        initWidget(uiBinder.createAndBindUi(this));

        formErrorLabel.setText(" ");
        usernameLabel.setText(dashboardConstants.userNameLabel());
        passwordLabel.setText(dashboardConstants.passwordLabel());

        mainPanel.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                int nativeCode = event.getNativeEvent().getKeyCode();
                if (nativeCode == KeyCodes.KEY_ENTER) {
                    attemptLogon();
                    event.preventDefault();
                }
            }
        });




        submitButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                attemptLogon();
            }
        });

    }


    private void attemptLogon()
    {
//Reset any old messages
        completionLabel1.setText("");
        completionLabel2.setText("");
        formErrorLabel.setText(" ");

        boolean error = false;
        logger.log(Level.FINE, "Submit button clicked");
        if (loginBox.getText().trim().length()==0){
            logger.warning("Missing username");
            completionLabel1.setText("Required");
            error = true;
        }

        if (passwordTextBox.getText().trim().length()==0){
            logger.warning("Missing password");
            completionLabel2.setText("Required");
            error = true;
        }

        //Don't bother to submit if there is an error.
        if (!error)
        {
            logger.info("Submitting authentication request");
            userSessionService.logon(loginBox.getText(), passwordTextBox.getText(), new TEDAsyncCallback<GlobalPlaceholder>() {
                @Override
                public void onSuccess(GlobalPlaceholder result) {
                    Globals.user = result.getSessionUser();
                    Globals.serverInfo = result.getServerInfo();

                    if (result != null)
                    {
                        if (logger.isLoggable(Level.FINE)) logger.fine("Login Successful: " + Globals.user);
                        if (logger.isLoggable(Level.FINE)) logger.fine("Server Info: " + Globals.serverInfo);
                        RootPanel.get(Aggredata.ROOT_PANEL).clear();
                        RootPanel.get(Aggredata.ROOT_PANEL).add(new AggredataPanel());
                    }   else
                    {

                        formErrorLabel.setText("Invalid Username or Password. Please try again.");
                    }
                }
            });

        }
    }

}
