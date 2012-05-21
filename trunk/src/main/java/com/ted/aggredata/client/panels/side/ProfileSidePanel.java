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

package com.ted.aggredata.client.panels.side;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.ted.aggredata.client.events.*;
import com.ted.aggredata.client.guiService.GWTGroupService;
import com.ted.aggredata.client.guiService.GWTGroupServiceAsync;
import com.ted.aggredata.client.guiService.TEDAsyncCallback;
import com.ted.aggredata.client.resources.lang.SidepanelConstants;
import com.ted.aggredata.client.util.DateUtil;
import com.ted.aggredata.client.widgets.SmallButton;
import com.ted.aggredata.model.Enums;
import com.ted.aggredata.model.Group;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * This is a side panel used for graphing that contains group selection and graphing options.
 */
public class ProfileSidePanel extends Composite {

    interface MyUiBinder extends UiBinder<Widget, ProfileSidePanel> {
    }

    static Logger logger = Logger.getLogger(ProfileSidePanel.class.toString());
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField
    Label instructionsLabel2;
    @UiField
    Label instructionsLabel1;
    @UiField
    Label instructionsLabel3;

    public ProfileSidePanel() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setTabInstructions(int tabIndex){
        switch (tabIndex){
            case 0:
                instructionsLabel1.setText(SidepanelConstants.INSTANCE.settingInstructions());
                instructionsLabel2.setText(SidepanelConstants.INSTANCE.settingInstructions2());
                instructionsLabel3.setText(SidepanelConstants.INSTANCE.settingInstructions3());
                break;
            case 1:
                instructionsLabel1.setText(SidepanelConstants.INSTANCE.groupInstructions());
                instructionsLabel2.setText(SidepanelConstants.INSTANCE.groupInstructions2());
                instructionsLabel3.setText(SidepanelConstants.INSTANCE.groupInstructions3());

                break;
            case 2:
                instructionsLabel1.setText(SidepanelConstants.INSTANCE.gatewaysInstructions());
                instructionsLabel2.setText(SidepanelConstants.INSTANCE.gatewaysInstructions2());
                instructionsLabel3.setText(SidepanelConstants.INSTANCE.gatewaysInstructions3());

                break;

            default:
                instructionsLabel1.setText(SidepanelConstants.INSTANCE.activateInstructions());
                instructionsLabel2.setText(SidepanelConstants.INSTANCE.activateInstructions2());
                instructionsLabel3.setText(SidepanelConstants.INSTANCE.activateInstructions3());
        }

    }
}
