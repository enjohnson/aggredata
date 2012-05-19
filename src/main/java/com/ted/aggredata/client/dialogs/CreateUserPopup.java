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
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.ted.aggredata.client.resources.lang.DashboardConstants;
import com.ted.aggredata.client.widgets.SmallButton;
import org.w3c.css.sac.ElementSelector;

import java.util.logging.Logger;


public class CreateUserPopup extends PopupPanel {

    static Logger logger = Logger.getLogger(CreateUserPopup.class.toString());


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

    public CreateUserPopup() {
        setWidget(uiBinder.createAndBindUi(this));
        this.getElement().getStyle().setBorderColor("#c3c1c1");
        this.getElement().getStyle().setBackgroundColor("#1c1c1c");
        captionPanel.setCaptionHTML("<span style='color:white'>" + DashboardConstants.INSTANCE.createUser() + "</span>");


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
                if (getPassword().trim().length() == 0 || getEmail().trim().length() == 0 || getFirstName().trim().length() == 0 || getLastName().trim().length() == 0)
                    if (getPassword().trim().length() == 0) {
                        logger.fine("OK clicked w/ no password specified");
                        value = 0;
                        hide();
                    }
                    if (getEmail().trim().length() == 0){
                        logger.fine("OK Clicked w/ no email specified");
                        value = 0;
                        hide();
                    }
                    if (getFirstName().trim().length() == 0){
                        logger.fine("OK clicked w/ no first name specified");
                        value = 0;
                        hide();
                    }
                    if (getLastName().trim().length() == 0){
                        logger.fine("OK clicked w/ no last name specified");
                        value = 0;
                        hide();
                    }
                else {
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