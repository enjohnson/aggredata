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

package com.ted.aggredata.client.panels.profile.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.ted.aggredata.client.Aggredata;
import com.ted.aggredata.client.guiService.TEDAsyncCallback;
import com.ted.aggredata.client.guiService.UserSessionService;
import com.ted.aggredata.client.guiService.UserSessionServiceAsync;
import com.ted.aggredata.client.resources.lang.DashboardConstants;
import com.ted.aggredata.client.widgets.HugeButton;
import com.ted.aggredata.client.widgets.LargeButton;
import com.ted.aggredata.model.User;
//import sun.font.TrueTypeFont;

import java.util.logging.Logger;

public class SettingsPanel extends Composite {
    private boolean isValid;
    static Logger logger = Logger.getLogger(SettingsPanel.class.toString());
    private User user = Aggredata.GLOBAL.getSessionUser();
    interface MyUiBinder extends UiBinder<Widget, SettingsPanel> {
    }
    private String uname = "";
    private String password = "";
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    DashboardConstants dashboardConstants = GWT.create(DashboardConstants.class);
    final UserSessionServiceAsync userSessionService = (UserSessionServiceAsync) GWT.create(UserSessionService.class);
    @UiField Label titleLabel;
    @UiField Label instructionLabel;
    @UiField TextBox lastNameField;
    @UiField Label lastNameFieldError;
    @UiField Label firstNameFieldError;
    @UiField TextBox firstNameField;
    @UiField TextBox companyNameField;
    @UiField TextBox middleNameField;
    @UiField TextBox addressField;
    @UiField TextBox cityField;
    @UiField TextBox stateField;
    @UiField TextBox zipField;
    @UiField TextBox phoneNumberField;
    @UiField TextBox custom1Field;
    @UiField TextBox custom2Field;
    @UiField TextBox custom3Field;
    @UiField TextBox custom4Field;
    @UiField TextBox custom5Field;
    @UiField
    HugeButton changeUname;
    @UiField
    HugeButton changePassword;
    @UiField
    LargeButton saveButton;
    @UiField
    LargeButton resetButton;
    @UiField
    HorizontalPanel buttonPanel;
    @UiField
    VerticalPanel mainPanel;

    public SettingsPanel()
    {
        initWidget(uiBinder.createAndBindUi(this));
        titleLabel.setText(dashboardConstants.settingsTitle());
        instructionLabel.setText(dashboardConstants.settingsInstructions());

        //set text to current text fields and set max length to the current database field lengths
        firstNameField.setMaxLength(50);
        lastNameField.setMaxLength(50);
        middleNameField.setMaxLength(50);
        addressField.setMaxLength(50);
        cityField.setMaxLength(50);
        stateField.setMaxLength(15);
        companyNameField.setMaxLength(100);
        phoneNumberField.setMaxLength(25);
        zipField.setMaxLength(15);
        custom1Field.setMaxLength(100);
        custom2Field.setMaxLength(100);
        custom3Field.setMaxLength(100);
        custom4Field.setMaxLength(100);
        custom5Field.setMaxLength(100);

        custom5Field.setText(user.getCustom5());
        custom4Field.setText(user.getCustom4());
        custom2Field.setText(user.getCustom3());
        custom3Field.setText(user.getCustom2());
        custom1Field.setText(user.getCustom1());
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        middleNameField.setText(user.getMiddleName());
        addressField.setText(user.getAddress());
        stateField.setText(user.getAddrState());
        cityField.setText(user.getCity());
        phoneNumberField.setText(user.getPhoneNumber());
        zipField.setText(user.getZip());
        companyNameField.setText(user.getCompanyName());


        
        saveButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                //Save the object
                isValid = doValidation();
                if (isValid)
                {
                    saveUserData();
                    commitUserData();
                }
                //Window.alert("Save clicked!");
             }
        });

        changePassword.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                changePword();
                //Window.alert("Reset clicked!");
            }
        });

        changeUname.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                changeUname();
                //Window.alert("Reset clicked!");
            }
        });

        resetButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                resetUserFormData();
                //Window.alert("Reset clicked!");
            }
        });
        
        mainPanel.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_CENTER);

    }

    private void changeUname()
    {
        boolean confirm;
        uname = Window.prompt("Please enter in a new username", "");
        confirm = Window.confirm("Are you sure?");
        if (confirm) 
        {
            user.setUsername(uname);
            commitUserData();
        }
    }

    private void changePword()
    {
        boolean confirm;
        password = Window.prompt("Please enter in a new password", "");
        confirm = Window.confirm("Are you sure?");
        if (confirm)
        {
            userSessionService.changePassword(user, password, new TEDAsyncCallback<User>() {
                @Override
                public void onSuccess(User result) {
                    user = result;
                    if (result != null)
                    {
                        Window.alert("User account updated.");
                    }
                }
            });
        }
    }

    private void resetUserFormData()
    {
        custom5Field.setText(user.getCustom5());
        custom4Field.setText(user.getCustom4());
        custom3Field.setText(user.getCustom3());
        custom2Field.setText(user.getCustom2());
        custom1Field.setText(user.getCustom1());
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        middleNameField.setText(user.getMiddleName());
        addressField.setText(user.getAddress());
        stateField.setText(user.getAddrState());
        cityField.setText(user.getCity());
        phoneNumberField.setText(user.getPhoneNumber());
        zipField.setText(user.getZip());
        companyNameField.setText(user.getCompanyName());
    }

    private void saveUserData(){
        user.setAddress(addressField.getText().trim());
        user.setAddrState(stateField.getText().trim());
        user.setCity(cityField.getText().trim());
        user.setZip(zipField.getText().trim());
        user.setFirstName(firstNameField.getText().trim());
        user.setLastName(lastNameField.getText().trim());
        user.setCompanyName(companyNameField.getText().trim());
        user.setCustom1(custom1Field.getText().trim());
        user.setCustom2(custom2Field.getText().trim());
        user.setCustom3(custom3Field.getText().trim());
        user.setCustom4(custom4Field.getText().trim());
        user.setCustom5(custom5Field.getText().trim());
        user.setPhoneNumber(phoneNumberField.getText().trim());
        user.setMiddleName(middleNameField.getText().trim());
    }

    private void commitUserData()
    {
        userSessionService.saveUser(user, new TEDAsyncCallback<User>() {
        @Override
        public void onSuccess(User result) {
            user = result;
            if (result != null)
            {
                Window.alert("User account updated.");
            }
        }
            });
        }

        /**
         * Performs the field validation. Returns false if any of the fields fail validation
         * @return
         */
    private boolean doValidation()
    {
        boolean isValid = true;

        firstNameFieldError.setText("");
        lastNameFieldError.setText("");

        if (firstNameField.getText().trim().length()==0)
        {
            isValid = false;
            firstNameFieldError.setText("Required");
        }

        if (lastNameField.getText().trim().length()==0)
        {
            isValid = false;
            lastNameFieldError.setText("Required");
        }

        return isValid;

    }
    }


