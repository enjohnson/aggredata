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
import com.google.gwt.user.client.ui.*;
import com.ted.aggredata.client.resources.lang.DashboardConstants;
import com.ted.aggredata.client.resources.lang.LogonConstants;
import com.ted.aggredata.client.widgets.SmallButton;

import java.util.logging.Logger;


public class ChangePasswordPopup extends PopupPanel {

    static Logger logger = Logger.getLogger(ChangePasswordPopup.class.toString());
    public static int PASSWORD_LENGTH = 5;

    interface MyUiBinder extends UiBinder<Widget, ChangePasswordPopup> {
    }
    final DashboardConstants dc = DashboardConstants.INSTANCE;
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    int value = 0;
    public static int OK = 1;

    @UiField
    CaptionPanel captionPanel;
    @UiField
    Label passwordFieldError;
    @UiField
    SmallButton noButton;
    @UiField
    SmallButton yesButton;
    @UiField
    TextBox passwordBox;


    public String getPassword() {
      return passwordBox.getText().trim();
    }


    public ChangePasswordPopup() {
        setWidget(uiBinder.createAndBindUi(this));
        this.getElement().getStyle().setBorderColor("#c3c1c1");
        this.getElement().getStyle().setBackgroundColor("#1c1c1c");
        captionPanel.setCaptionHTML("<span style='color:white'>" + DashboardConstants.INSTANCE.changePassword() + "</span>");


        this.center();
        int top = this.getAbsoluteTop() - 100;
        int left = this.getAbsoluteLeft();
        this.setPopupPosition(left, top);


        noButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                    logger.fine("NO clicked");
                    value = 0;
                    hide();
            }
        });

        yesButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if(getPassword().trim().length() < PASSWORD_LENGTH){
                    passwordFieldError.setText(dc.passwordError());
                    logger.fine("Length of the password did not meet length requirements.");
                    value = 0;
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