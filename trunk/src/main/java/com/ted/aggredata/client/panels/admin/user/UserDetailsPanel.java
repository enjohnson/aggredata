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
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.ted.aggredata.client.Aggredata;
import com.ted.aggredata.client.guiService.*;
import com.ted.aggredata.client.resources.lang.DashboardConstants;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.ted.aggredata.client.guiService.GWTUserService;
import com.ted.aggredata.client.guiService.GWTUserServiceAsync;
import com.ted.aggredata.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDetailsPanel extends Composite {

    final GWTUserServiceAsync userService = (GWTUserServiceAsync) GWT.create(GWTUserService.class);

    static Logger logger = Logger.getLogger(UserDetailsPanel.class.toString());

    interface MyUiBinder extends UiBinder<Widget, UserDetailsPanel> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField
    TextBox firstNameField;
    @UiField
    TextBox lastNameField;
    @UiField
    TextBox custom1Field;
    @UiField
    TextBox custom2Field;
    @UiField
    TextBox custom4Field;
    @UiField
    TextBox custom5Field;
    @UiField
    TextBox custom3Field;
    @UiField
    TextBox usernameField;
    @UiField
    UserButtonPanel UserButtonPanel;
    @UiField
    CaptionPanel captionPanel;
    @UiField
    Label custom5Label;
    @UiField
    Label custom4Label;
    @UiField
    Label custom2Label;
    @UiField
    HorizontalPanel custom1Panel;
    @UiField
    HorizontalPanel custom2Panel;
    @UiField
    HorizontalPanel custom5Panel;
    @UiField
    HorizontalPanel custom4Panel;
    @UiField
    HorizontalPanel custom3Panel;
    @UiField
    Label custom1Label;
    @UiField
    Label custom3Label;

    User user;
    Integer userHashCode = 0;
    List<User> userList = new ArrayList<User>();


    ChangeHandler saveChangeHanlder = new ChangeHandler() {
        @Override
        public void onChange(ChangeEvent changeEvent) {
            if (user != null) doSave();
        }
    };

    public UserDetailsPanel() {
        initWidget(uiBinder.createAndBindUi(this));

        captionPanel.setCaptionHTML("<span style='color:white'>" + DashboardConstants.INSTANCE.userDetails() + "</span>");

        usernameField.addChangeHandler(saveChangeHanlder);
        firstNameField.addChangeHandler(saveChangeHanlder);
        lastNameField.addChangeHandler(saveChangeHanlder);
        custom1Field.addChangeHandler(saveChangeHanlder);
        custom3Field.addChangeHandler(saveChangeHanlder);
        custom4Field.addChangeHandler(saveChangeHanlder);
        custom2Field.addChangeHandler(saveChangeHanlder);
        custom5Field.addChangeHandler(saveChangeHanlder);

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

    }

    /**
     * Sets all the fields of this panel to be enabled or disabled
     * Make the email, first name, and last name fields non-editable.
     * @param enabled
     */
    public void setEnabled(boolean enabled) {

        firstNameField.setEnabled(false);
        custom1Field.setEnabled(enabled);
        lastNameField.setEnabled(false);
        custom3Field.setEnabled(enabled);
        usernameField.setEnabled(false);
        custom2Field.setEnabled(enabled);
        custom4Field.setEnabled(enabled);
        custom5Field.setEnabled(enabled);
    }

    /**
     * Save the user custom fields as the user changes them. 
     */
    private void doSave() {
        user.setCustom3(custom3Field.getText().trim());
        user.setCustom1(custom1Field.getText().trim());
        user.setCustom2(custom2Field.getText().trim());
        user.setCustom4(custom4Field.getText().trim());
        user.setCustom5(custom5Field.getText().trim());
        if (user.hashCode() != userHashCode) {
            logger.info("user is dirty. Saving " + user);
            userService.saveUser(user, new TEDAsyncCallback<User>() {
                @Override
                public void onSuccess(User user) {
                    userHashCode = user.hashCode();
                }
            });
        }
    }

    /**
     * Set the current user's info in the panels.
     * @param user
     */
    public void setUser(final User user) {
        if (logger.isLoggable(Level.FINE)) logger.fine("Setting user " + user);
        setEnabled(user != null);
        firstNameField.setValue(user.getFirstName());
        lastNameField.setValue(user.getLastName());
        usernameField.setValue(user.getUsername());
        custom1Field.setValue(user.getCustom1());
        custom2Field.setValue(user.getCustom2());
        custom4Field.setValue(user.getCustom4());
        custom5Field.setValue(user.getCustom5());
        custom3Field.setValue(user.getCustom3());
        this.user = user;
        userHashCode = user.hashCode();
    }


}
