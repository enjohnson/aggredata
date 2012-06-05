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

package com.ted.aggredata.client.panels.join;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.ted.aggredata.client.Aggredata;
import com.ted.aggredata.client.guiService.TEDAsyncCallback;
import com.ted.aggredata.client.guiService.UserSessionService;
import com.ted.aggredata.client.guiService.UserSessionServiceAsync;
import com.ted.aggredata.client.resources.lang.JoinConstants;
import com.ted.aggredata.client.widgets.ClearImage;
import com.ted.aggredata.client.widgets.LargeButton;
import com.ted.aggredata.model.Enums;
import com.ted.aggredata.model.User;

import java.util.logging.Logger;


public class JoinPanelForm extends Composite {

    static Logger logger = Logger.getLogger(JoinPanelForm.class.toString());
    final JoinConstants joinConstants = JoinConstants.INSTANCE;

    final UserSessionServiceAsync userSessionService = (UserSessionServiceAsync) GWT.create(UserSessionService.class);

    interface MyUiBinder extends UiBinder<Widget, JoinPanelForm> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField
    TextBox usernameField;
    @UiField
    Label usernameFieldError;
    @UiField
    TextBox confirmUsernameField;
    @UiField
    Label confirmUsernameFieldError;
    @UiField
    PasswordTextBox passwordField;
    @UiField
    Label passwordFieldError;
    @UiField
    PasswordTextBox confirmPasswordField;
    @UiField
    Label confirmPasswordFieldError;
    @UiField
    TextBox firstNameField;
    @UiField
    Label firstNameFieldError;
    @UiField
    TextBox middleNameField;
    @UiField
    Label middleNameFieldError;
    @UiField
    TextBox lastNameField;
    @UiField
    Label lastNameFieldError;
    @UiField
    TextBox companyNameField;
    @UiField
    Label companyNameFieldError;
    @UiField
    TextBox addressField;
    @UiField
    Label addressFieldError;
    @UiField
    TextBox cityField;
    @UiField
    Label cityFieldError;
    @UiField
    TextBox stateField;
    @UiField
    Label stateFieldError;
    @UiField
    TextBox zipField;
    @UiField
    Label zipFieldError;
    @UiField
    TextBox phoneNumberField;
    @UiField
    Label phoneNumberFieldError;
    @UiField
    Label timeZoneError;

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
    @UiField
    ClearImage centeringImage;
    @UiField
    LargeButton submitButton;
    @UiField
    Label formErrorLabel;
    @UiField
    ListBox timeZoneField;
    @UiField
    CaptchaPanel captchaWidget;
    @UiField
    Label touPolicyLink;


    public JoinPanelForm() {
        initWidget(uiBinder.createAndBindUi(this));

        boolean useCenteringImage = true;
        if (Aggredata.GLOBAL.getUserCustomFields().getCustom1().trim().length() > 0) useCenteringImage = false;
        if (Aggredata.GLOBAL.getUserCustomFields().getCustom2().trim().length() > 0) useCenteringImage = false;
        if (Aggredata.GLOBAL.getUserCustomFields().getCustom3().trim().length() > 0) useCenteringImage = false;
        if (Aggredata.GLOBAL.getUserCustomFields().getCustom4().trim().length() > 0) useCenteringImage = false;
        if (Aggredata.GLOBAL.getUserCustomFields().getCustom5().trim().length() > 0) useCenteringImage = false;

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

        if (useCenteringImage) centeringImage.setSize("100px", "1px");

        for (String tz : Enums.timezones) {
            timeZoneField.addItem(tz);
        }
        timeZoneField.setSelectedIndex(5);

        touPolicyLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                Window.open("/aggredata/tos.html", "_blank", "");
            }
        });

        submitButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                logger.fine("New user submit button hit. Doing validation");

                if (doValidation()) {
                    logger.fine("Submitting form data");
                    User user = new User();
                    user.setUsername(usernameField.getValue());
                    user.setFirstName(firstNameField.getValue());
                    user.setMiddleName(middleNameField.getValue());
                    user.setLastName(lastNameField.getValue());
                    user.setCompanyName(companyNameField.getValue());
                    user.setAddress(addressField.getValue());
                    user.setCity(cityField.getValue());
                    user.setAddrState(stateField.getValue());
                    user.setPhoneNumber(phoneNumberField.getValue());
                    user.setTimezone(timeZoneField.getValue(timeZoneField.getSelectedIndex()));
                    user.setCustom1(custom1Field.getValue());
                    user.setCustom2(custom2Field.getValue());
                    user.setCustom3(custom3Field.getValue());
                    user.setCustom4(custom4Field.getValue());
                    user.setCustom5(custom5Field.getValue());

                    userSessionService.validateCaptcha(captchaWidget.getValue(), usernameField.getValue(), passwordField.getValue(), user, new TEDAsyncCallback<Integer>() {
                        @Override
                        public void onSuccess(Integer integer) {
                            if (integer.equals(UserSessionService.RESULT_FAIL_CAPTCHA)) {
                                formErrorLabel.setText(joinConstants.captchaError());
                                return;
                            } else if (integer.equals(UserSessionService.RESULT_DUPE_USERNAME)) {
                                formErrorLabel.setText(joinConstants.alreadyExists());
                                usernameFieldError.setText(joinConstants.required());
                                return;
                            } else if (integer.equals(UserSessionService.RESULT_SUCCESS)) {
                                logger.info("User creation successful. Redirecting to success link");
                                RootPanel.get(Aggredata.ROOT_PANEL).clear();
                                RootPanel.get(Aggredata.ROOT_PANEL).add(new ActivateLinkPanel());
                            } else {
                                logger.severe("Unexpected result code of " + integer);
                            }
                        }
                    });


                }
            }
        });
    }

    private boolean doValidation() {
        boolean passValidation = true;
        formErrorLabel.setText("");
        usernameFieldError.setText("");
        passwordFieldError.setText("");
        confirmUsernameFieldError.setText("");
        confirmPasswordFieldError.setText("");
        lastNameFieldError.setText("");
        firstNameFieldError.setText("");

        //Do the validation checks
        boolean passwordTooShort = checkLength(passwordField, passwordFieldError,5) && passValidation;;
        boolean passValidaion = passwordTooShort;
        passValidation = checkRequired(usernameField, usernameFieldError) && passValidation;
        passValidation = checkRequired(passwordField, passwordFieldError) && passValidation;
        passValidation = checkRequired(confirmUsernameField, confirmUsernameFieldError) && passValidation;
        passValidation = checkRequired(confirmPasswordField, confirmPasswordFieldError) && passValidation;
        passValidation = checkRequired(lastNameField, lastNameFieldError) && passValidation;
        passValidation = checkRequired(firstNameField, firstNameFieldError) && passValidation;
        passValidation = checkMatch(usernameField, confirmUsernameField, usernameFieldError, confirmUsernameFieldError) && passValidation;
        passValidation = checkMatch(passwordField, confirmPasswordField, passwordFieldError, confirmPasswordFieldError) && passValidation;

        passValidation = checkEmail(usernameField, usernameFieldError)  && passValidation;

        if (!passwordTooShort) formErrorLabel.setText(joinConstants.passwordShortError());
        if (!passValidation) logger.severe("Validation failed");
        return passValidation;
    }


    private boolean checkMatch(TextBox textBox1, TextBox textBox2, Label errorLabel, Label errorLabel2) {
        boolean isValid = true;
        if (!textBox1.getValue().equals(textBox2.getValue())) {
            errorLabel.setText(joinConstants.noMatch());
            errorLabel2.setText(joinConstants.noMatch());
            formErrorLabel.setText(joinConstants.formErrors());
            isValid = false;
        }
        return isValid;
    }

    private boolean checkLength(TextBox textBox, Label errorLabel, int minLength) {
        boolean isValid = true;
        if (textBox.getValue().trim().length() < minLength) {
            errorLabel.setText(joinConstants.tooShort());
            formErrorLabel.setText(joinConstants.formErrors());
            isValid = false;
        }
        return isValid;
    }


    private boolean checkRequired(TextBox textBox, Label errorLabel) {
        boolean isValid = true;
        if (textBox.getValue().trim().length() == 0) {
            errorLabel.setText(joinConstants.required());
            formErrorLabel.setText(joinConstants.formErrors());
            isValid = false;
        }
        return isValid;
    }

    private boolean checkEmail(TextBox textBox, Label errorLabel) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(?:[a-zA-Z]{2,6})$";
        boolean isValid = true;
        String value = textBox.getValue();
        if (!value.matches(emailPattern)) {
            isValid = false;
            errorLabel.setText(joinConstants.notEmailError());
            formErrorLabel.setText(joinConstants.formErrors());
        }
        return isValid;
    }

}
