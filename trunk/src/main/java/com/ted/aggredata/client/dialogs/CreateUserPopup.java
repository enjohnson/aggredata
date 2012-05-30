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

package com.ted.aggredata.client.dialogs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.ted.aggredata.client.guiService.GWTUserService;
import com.ted.aggredata.client.guiService.GWTUserServiceAsync;
import com.ted.aggredata.client.guiService.TEDAsyncCallback;
import com.ted.aggredata.client.panels.admin.user.UserSelectionPanel;
import com.ted.aggredata.client.resources.lang.DashboardConstants;
import com.ted.aggredata.client.widgets.SmallButton;
import com.ted.aggredata.model.Enums;
import com.ted.aggredata.model.User;
import org.w3c.css.sac.ElementSelector;
import com.google.gwt.user.client.Window;

import java.util.logging.Logger;


public class CreateUserPopup extends PopupPanel {

    static Logger logger = Logger.getLogger(CreateUserPopup.class.toString());

    final GWTUserServiceAsync gwtUserService = (GWTUserServiceAsync) GWT.create(GWTUserService.class);

    interface MyUiBinder extends UiBinder<Widget, CreateUserPopup> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    int value = 0;
    public static int OK = 1;

    @UiField
    CaptionPanel captionPanel;


    @UiField
    SmallButton okButton;
    @UiField
    SmallButton cancelButton;
    @UiField
    TextBox email;
    @UiField
    TextBox password;
    @UiField
    TextBox firstName;
    @UiField
    TextBox lastName;
    @UiField
    Label firstNameFieldError;
    @UiField
    Label lastNameFieldError;
    @UiField
    Label emailFieldError;
    @UiField
    Label passwordFieldError;
    @UiField
    ListBox timeZoneField;

    public String getEmail() {
      return email.getText().trim();
    }

    public String getPassword() {
        return password.getText().trim();
    }

    public String getFirstName() {
        return firstName.getText().trim();
    }

    public String getLastName() {
        return lastName.getText().trim();
    }
    
    public void setEmailFieldError(String Error){
        emailFieldError.setText(Error);
    }
    
    public String getTimezone(){
        int si = timeZoneField.getSelectedIndex();
        String tz = timeZoneField.getItemText(si);
        return tz;
    }

    public CreateUserPopup() {
        setWidget(uiBinder.createAndBindUi(this));
        this.getElement().getStyle().setBorderColor("#c3c1c1");
        this.getElement().getStyle().setBackgroundColor("#1c1c1c");
        captionPanel.setCaptionHTML("<span style='color:white'>" + DashboardConstants.INSTANCE.createUser() + "</span>");

        int index = 0;
        for (String tz: Enums.timezones) {
            timeZoneField.addItem(tz);
            if (tz.equals("US/Eastern")) {
                timeZoneField.setSelectedIndex(index);
            }
            index++;
        }


        this.center();
        int top = this.getAbsoluteTop() - 100;
        int left = this.getAbsoluteLeft();
        this.setPopupPosition(left, top);

        cancelButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                    logger.fine("Cancel clicked");
                    value = 0;
                    hide();
            }
        });

        okButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                passwordFieldError.setText("");
                emailFieldError.setText("");
                firstNameFieldError.setText("");
                lastNameFieldError.setText("");

                if (getPassword().trim().length() <= 4 || getEmail().trim().length() <= 4 || getFirstName().trim().length() == 0 || getLastName().trim().length() == 0)
                {
                    if (getPassword().trim().length() <= 4) {
                        logger.fine("OK clicked w/ no password specified");
                        passwordFieldError.setText("Password must be at least 5 characters in length.");
                    }
                    if (getEmail().trim().length() <= 4){
                        logger.fine("OK Clicked w/ no email specified");
                        emailFieldError.setText("Email must be at least 5 characters in length.");
                    }
                    if (getFirstName().trim().length() == 0){
                        logger.fine("OK clicked w/ no first name specified");
                        firstNameFieldError.setText("Field is required.");
                    }
                    if (getLastName().trim().length() == 0){
                        logger.fine("OK clicked w/ no last name specified");
                        lastNameFieldError.setText("Field is required.");
                    }
                }

                else {
                    gwtUserService.getUserByUserName(getEmail(), new TEDAsyncCallback<User>() {
                        @Override
                        public void onSuccess(User result) {

                        }
                    });
                    logger.fine("YES clicked");
                    value = OK;
                    hide();
                }
            }
        });

    }


    public int getValue() {
        return value;
    }
}