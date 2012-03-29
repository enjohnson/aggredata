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
import com.ted.aggredata.client.widgets.LargeButton;
import com.ted.aggredata.model.User;
import java.util.logging.Logger;

public class SettingsPanel extends Composite {

    static Logger logger = Logger.getLogger(SettingsPanel.class.toString());
    private User user = Aggredata.GLOBAL.getSessionUser();
    interface MyUiBinder extends UiBinder<Widget, SettingsPanel> {
    }
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    DashboardConstants dashboardConstants = GWT.create(DashboardConstants.class);
    final UserSessionServiceAsync userSessionService = (UserSessionServiceAsync) GWT.create(UserSessionService.class);
    @UiField Label titleLabel;
    @UiField Label instructionLabel;
    @UiField Label lastNameLabel;
    @UiField TextBox lastNameField;
    @UiField Label lastNameFieldError;
    @UiField Label firstNameLabel;
    @UiField TextBox firstNameField;
    @UiField Label firstNameFieldError;
    @UiField Label companyNameLabel;
    @UiField TextBox companyNameField;
    @UiField Label companyNameFieldError;
    @UiField Label middleNameLabel;
    @UiField TextBox middleNameField;
    @UiField Label middleNameFieldError;
    @UiField Label addressLabel;
    @UiField TextBox addressField;
    @UiField Label addressFieldError;
    @UiField Label cityLabel;
    @UiField TextBox cityField;
    @UiField Label cityFieldError;
    @UiField Label stateLabel;
    @UiField TextBox stateField;
    @UiField Label stateFieldError;
    @UiField Label zipLabel;
    @UiField TextBox zipField;
    @UiField Label zipFieldError;
    @UiField Label phoneNumberLabel;
    @UiField TextBox phoneNumberField;
    @UiField Label phoneNumberFieldError;
    @UiField Label custom1Label;
    @UiField TextBox custom1Field;
    @UiField Label custom1FieldError;
    @UiField Label custom2Label;
    @UiField TextBox custom2Field;
    @UiField Label custom2FieldError;
    @UiField Label custom3Label;
    @UiField TextBox custom3Field;
    @UiField Label custom3FieldError;
    @UiField Label custom4Label;
    @UiField TextBox custom4Field;
    @UiField Label custom4FieldError;
    @UiField Label custom5Label;
    @UiField TextBox custom5Field;
    @UiField Label custom5FieldError;
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
        lastNameLabel.setText(dashboardConstants.profileSettingsLastName());
        firstNameLabel.setText(dashboardConstants.profileSettingsFirstName());
        companyNameLabel.setText(dashboardConstants.profileSettingsCompanyName());
        middleNameLabel.setText(dashboardConstants.profileSettingsMiddleName());
        addressLabel.setText(dashboardConstants.profileSettingsAddress());
        cityLabel.setText(dashboardConstants.profileSettingsCity());
        stateLabel.setText(dashboardConstants.profileSettingState());
        zipLabel.setText(dashboardConstants.profileSettingsZip());
        phoneNumberLabel.setText(dashboardConstants.profileSettingsPhoneNumber());
        custom1Label.setText(dashboardConstants.profileSettingsCustom1());
        custom2Label.setText(dashboardConstants.profileSettingsCustom2());
        custom3Label.setText(dashboardConstants.profileSettingsCustom3());
        custom4Label.setText(dashboardConstants.profileSettingsCustom4());
        custom5Label.setText(dashboardConstants.profileSettingsCustom5());

        //set text to current text fields

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
                commitUserData();
                //Window.alert("Save clicked!");
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

    private void commitUserData()
    {
        firstNameFieldError.setText("");
        lastNameFieldError.setText("");
        companyNameFieldError.setText("");
        middleNameFieldError.setText("");
        addressFieldError.setText("");
        cityFieldError.setText("");
        zipFieldError.setText("");
        stateFieldError.setText("");
        custom1FieldError.setText("");
        phoneNumberFieldError.setText("");
        custom2FieldError.setText("");
        custom3FieldError.setText("");
        custom4FieldError.setText("");
        custom5FieldError.setText("");

        boolean error = false;
        if (firstNameField.getText().trim().length()==0)
        {
            error = true;
            firstNameFieldError.setText("Required");
        }

        if (lastNameField.getText().trim().length()==0)
        {
            error = true;
            lastNameFieldError.setText("Required");
        }

        if(!error)
        {
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

        }
    }
    /**
     * Performs the field validation. Returns false if any of the fields fail validation
     * @return
     */
//    private boolean doValidation()
//    {
//        boolean isValid = true;
//
//        firstNameFieldError.setText("");
//        lastNameFieldError.setText("");
//        companyNameFieldError.setText("");
//
//        if (firstNameField.getText().trim().length()==0)
//        {
//            isValid = false;
//            firstNameFieldError.setText("Required");
//        }
//
//        if (lastNameField.getText().trim().length()==0)
//        {
//            isValid = false;
//            lastNameFieldError.setText("Required");
//        }
//
//        return isValid;
//
//    }

