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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.ted.aggredata.client.Aggredata;
import com.ted.aggredata.client.resources.DashboardImageResource;
import com.ted.aggredata.client.widgets.ClearImage;


public class JoinPanelForm extends Composite {


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


    public JoinPanelForm() {
        initWidget(uiBinder.createAndBindUi(this));

        boolean useCenteringImage = true;
        if (Aggredata.GLOBAL.getUserCustomFields().getCustom1().trim().length() > 0) useCenteringImage=false;
        if (Aggredata.GLOBAL.getUserCustomFields().getCustom2().trim().length() > 0) useCenteringImage=false;
        if (Aggredata.GLOBAL.getUserCustomFields().getCustom3().trim().length() > 0) useCenteringImage=false;
        if (Aggredata.GLOBAL.getUserCustomFields().getCustom4().trim().length() > 0) useCenteringImage=false;
        if (Aggredata.GLOBAL.getUserCustomFields().getCustom5().trim().length() > 0) useCenteringImage=false;

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
    }

}
