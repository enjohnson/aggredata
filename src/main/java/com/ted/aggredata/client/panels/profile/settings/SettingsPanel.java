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
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.ted.aggredata.client.Aggredata;
import com.ted.aggredata.client.guiService.*;
import com.ted.aggredata.client.panels.login.LoginPanel;
import com.ted.aggredata.client.resources.lang.DashboardConstants;
import com.ted.aggredata.client.widgets.HugeButton;
import com.ted.aggredata.model.Enums;
import com.ted.aggredata.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.css.sac.ElementSelector;

import java.util.logging.Logger;

//import sun.font.TrueTypeFont;

public class SettingsPanel extends Composite {
    private boolean isValid;
    static Logger logger = Logger.getLogger(SettingsPanel.class.toString());
    final User user;

    interface MyUiBinder extends UiBinder<Widget, SettingsPanel> {
    }

    private String uname = "";
    private int unameLength = 5;
    private String password = "";
    private int passLength = 5;
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    DashboardConstants dashboardConstants = GWT.create(DashboardConstants.class);

    final GWTUserServiceAsync gwtUserService = (GWTUserServiceAsync) GWT.create(GWTUserService.class);
    final UserSessionServiceAsync userSessionService = (UserSessionServiceAsync) GWT.create(UserSessionService.class);

    @UiField
    Label titleLabel;
    @UiField
    Label instructionLabel;
    @UiField
    TextBox lastNameField;
    @UiField
    Label lastNameFieldError;
    @UiField
    Label firstNameFieldError;
    @UiField
    TextBox firstNameField;
    @UiField
    TextBox companyNameField;
    @UiField
    TextBox middleNameField;
    @UiField
    TextBox addressField;
    @UiField
    TextBox cityField;
    @UiField
    TextBox stateField;
    @UiField
    TextBox zipField;
    @UiField
    TextBox phoneNumberField;
    @UiField
    TextBox custom1Field;
    @UiField
    TextBox custom2Field;
    @UiField
    TextBox custom3Field;
    @UiField
    TextBox custom4Field;
    @UiField
    TextBox custom5Field;
    @UiField
    HugeButton changeUname;
    @UiField
    HugeButton changePassword;
    @UiField
    VerticalPanel mainPanel;
    @UiField
    ListBox timeZoneField;
    @UiField
    HorizontalPanel custom1Panel;
    @UiField
    Label custom1Label;
    @UiField
    HorizontalPanel custom2Panel;
    @UiField
    Label custom2Label;
    @UiField
    HorizontalPanel custom3Panel;
    @UiField
    Label custom3Label;
    @UiField
    HorizontalPanel custom4Panel;
    @UiField
    Label custom4Label;
    @UiField
    HorizontalPanel custom5Panel;
    @UiField
    Label custom5Label;

    int userHashCode = 0;

    ChangeHandler saveChangeHanlder = new ChangeHandler() {
        @Override
        public void onChange(ChangeEvent changeEvent) {
            doSave();
        }
    };

    public SettingsPanel() {
        initWidget(uiBinder.createAndBindUi(this));
        user = Aggredata.GLOBAL.getSessionUser();

        titleLabel.setText(dashboardConstants.settingsTitle());
        instructionLabel.setText(dashboardConstants.settingsInstructions());

        //Hide the panels that are not being used for custom fields
        custom1Panel.setVisible(Aggredata.GLOBAL.getUserCustomFields().getCustom1().trim().length() > 0);
        custom2Panel.setVisible(Aggredata.GLOBAL.getUserCustomFields().getCustom2().trim().length() > 0);
        custom3Panel.setVisible(Aggredata.GLOBAL.getUserCustomFields().getCustom3().trim().length() > 0);
        custom4Panel.setVisible(Aggredata.GLOBAL.getUserCustomFields().getCustom4().trim().length() > 0);
        custom5Panel.setVisible(Aggredata.GLOBAL.getUserCustomFields().getCustom5().trim().length() > 0);
        custom1Label.setText(Aggredata.GLOBAL.getUserCustomFields().getCustom1());
        custom2Label.setText(Aggredata.GLOBAL.getUserCustomFields().getCustom2());
        custom3Label.setText(Aggredata.GLOBAL.getUserCustomFields().getCustom3());
        custom4Label.setText(Aggredata.GLOBAL.getUserCustomFields().getCustom4());
        custom5Label.setText(Aggredata.GLOBAL.getUserCustomFields().getCustom5());


        userHashCode = user.hashCode();

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




        firstNameField.addChangeHandler(saveChangeHanlder);
        middleNameField.addChangeHandler(saveChangeHanlder);
        lastNameField.addChangeHandler(saveChangeHanlder);
        companyNameField.addChangeHandler(saveChangeHanlder);
        addressField.addChangeHandler(saveChangeHanlder);
        cityField.addChangeHandler(saveChangeHanlder);
        stateField.addChangeHandler(saveChangeHanlder);
        zipField.addChangeHandler(saveChangeHanlder);
        phoneNumberField.addChangeHandler(saveChangeHanlder);
        custom1Field.addChangeHandler(saveChangeHanlder);
        custom2Field.addChangeHandler(saveChangeHanlder);
        custom3Field.addChangeHandler(saveChangeHanlder);
        custom4Field.addChangeHandler(saveChangeHanlder);
        custom5Field.addChangeHandler(saveChangeHanlder);
        timeZoneField.addChangeHandler(saveChangeHanlder);



        int index = 0;
        for (String tz: Enums.timezones) {
            timeZoneField.addItem(tz);
            if (tz.equals(user.getTimezone())) {
                timeZoneField.setSelectedIndex(index);
            }
            index++;
        }





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


    }


    //TODO: Make the username/password a dialog. Also use resource strings.

    private void changeUname() {
        boolean confirm;
        uname = Window.prompt("Please enter in a new username", "");
        if (uname.length() >= unameLength){
            confirm = Window.confirm("Are you sure?");
            if (confirm) {
                    gwtUserService.changeUsername(user, uname, new TEDAsyncCallback<User>() {
                    @Override
                    public void onSuccess(User result) {
                        Window.alert("Username Changed. You will have to log back into Aggredata.");
                        logout();
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

    //TODO: Make the username/password a dialog. Also use resource strings.

    private void changePword() {
        boolean confirm;
        password = Window.prompt("Please enter in a new password", "");
        if (password.length() >= passLength){
            confirm = Window.confirm("Are you sure?");
            if (confirm) {
                 gwtUserService.changePassword(user, password, new TEDAsyncCallback<User>() {
                    @Override
                    public void onSuccess(User result) {
                        Window.alert("Password Changed. You will have to log back into Aggredata.");
                        logout();
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

    private void logout() {


        userSessionService.logoff(new TEDAsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                logger.info("User has sucessfully logged out");
                RootPanel.get(Aggredata.ROOT_PANEL).clear();
                RootPanel.get(Aggredata.ROOT_PANEL).add(new LoginPanel());
            }
        });


    }


    private void doSave() {
        logger.fine("settings panel doSave called");
        if (doValidation()) {
            logger.fine("validation passed");
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

            //Save the timezone
            int si = timeZoneField.getSelectedIndex();
            String tz = timeZoneField.getItemText(si);
            user.setTimezone(tz);


            logger.fine("hashcode check: " + user.hashCode() + " = " + userHashCode);
            if (user.hashCode() != userHashCode) {
                logger.fine("Calling save service");
                gwtUserService.saveUser(user, new TEDAsyncCallback<User>() {
                    @Override
                    public void onSuccess(User user) {
                        userHashCode = user.hashCode();
                    }
                });
            }
        }
    }


    /**
     * Performs the field validation. Returns false if any of the fields fail validation
     *
     * @return
     */
    private boolean doValidation() {
        boolean isValid = true;

        firstNameFieldError.setText("");
        lastNameFieldError.setText("");

        if (firstNameField.getText().trim().length() == 0) {
            isValid = false;
            firstNameFieldError.setText("Required");
        }

        if (lastNameField.getText().trim().length() == 0) {
            isValid = false;
            lastNameFieldError.setText("Required");
        }

        return isValid;

    }
}


