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

package com.ted.aggredata.client.panels.profile.groups;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.ted.aggredata.client.guiService.GWTGatewayService;
import com.ted.aggredata.client.guiService.GWTGatewayServiceAsync;
import com.ted.aggredata.client.guiService.TEDAsyncCallback;
import com.ted.aggredata.model.Gateway;
import com.ted.aggredata.model.Group;

import java.util.logging.Logger;

/**
 * Represents a single gateway that can be added or removed from a group.
 */
public class GatewayListRow extends Composite {

    final GWTGatewayServiceAsync gatewayService = (GWTGatewayServiceAsync) GWT.create(GWTGatewayService.class);

    static Logger logger = Logger.getLogger(GroupGatewaysPanel.class.toString());

    interface MyUiBinder extends UiBinder<Widget, GatewayListRow> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField
    CheckBox groupMemberCheckBox;
    @UiField
    Label gatewayDescription;
    @UiField
    HorizontalPanel mainPanel;


    final Group selectedGroup;
    final Gateway gateway;


    public GatewayListRow(final Group selectedGroup, final Gateway gateway, boolean isSelected, boolean isOdd) {
        initWidget(uiBinder.createAndBindUi(this));
        this.selectedGroup = selectedGroup;
        this.gateway = gateway;
        if (isOdd) {
            mainPanel.getElement().getStyle().setBackgroundColor("#222222");
        }

        groupMemberCheckBox.setValue(isSelected);
        gatewayDescription.setText(gateway.getDescription());

        groupMemberCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> booleanValueChangeEvent) {
                if (booleanValueChangeEvent.getValue()) {
                    logger.info("Adding " + gateway + " to " + selectedGroup);
                    gatewayService.addGatewayToGroup(selectedGroup, gateway, new TEDAsyncCallback<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            logger.fine(gateway + " addeed to " + selectedGroup);
                        }
                    });

                } else {
                    logger.info("Removing " + gateway + " to " + selectedGroup);
                    gatewayService.removeGatewayFromGroup(selectedGroup, gateway, new TEDAsyncCallback<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            logger.fine(gateway + " addeed to " + selectedGroup);
                        }
                    });

                }
            }
        });
    }

    public void setEnabled(boolean enabled) {
        groupMemberCheckBox.setEnabled(enabled);
    }
}
