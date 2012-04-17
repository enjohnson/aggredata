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

package com.ted.aggredata.client.panels.profile.gateways;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.ted.aggredata.client.dialogs.YesNoPopup;
import com.ted.aggredata.client.guiService.GWTGatewayService;
import com.ted.aggredata.client.guiService.GWTGatewayServiceAsync;
import com.ted.aggredata.client.guiService.TEDAsyncCallback;
import com.ted.aggredata.client.panels.profile.groups.GroupGatewaysPanel;
import com.ted.aggredata.client.resources.lang.DashboardConstants;
import com.ted.aggredata.client.widgets.SmallButton;
import com.ted.aggredata.model.Gateway;
import com.ted.aggredata.model.MTU;

import java.util.logging.Logger;

/**
 * Represents a single gateway that can be added or removed from a group.
 */
public class MTUListRow extends Composite {

    final GWTGatewayServiceAsync gatewayService = (GWTGatewayServiceAsync) GWT.create(GWTGatewayService.class);

    static Logger logger = Logger.getLogger(GroupGatewaysPanel.class.toString());

    interface MyUiBinder extends UiBinder<Widget, MTUListRow> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    final MTU mtu;
    @UiField
    HorizontalPanel mainPanel;
    @UiField
    TextBox serialNumberField;
    @UiField
    TextBox descriptionField;
    @UiField
    TextBox typeField;
    @UiField
    SmallButton deleteButton;


    public MTUListRow(final Gateway gateway, final MTU mtu, boolean isOdd) {
        initWidget(uiBinder.createAndBindUi(this));
        this.mtu = mtu;

        if (isOdd) {
            //    mainPanel.getElement().getStyle().setBackgroundColor("#222222");
        }

        serialNumberField.setReadOnly(true);
        serialNumberField.setEnabled(false);
        serialNumberField.setValue(Long.toHexString(mtu.getId()).toUpperCase());
        typeField.setReadOnly(true);
        typeField.setEnabled(false);

        if (mtu.getType().equals(MTU.MTUType.STAND_ALONE)) typeField.setText("STD ALONE");
        else if (mtu.getType().equals(MTU.MTUType.LOAD)) typeField.setText("LOAD");
        else if (mtu.getType().equals(MTU.MTUType.ADJUSTED_NET)) typeField.setText("ADJ LOAD");
        else if (mtu.getType().equals(MTU.MTUType.GENERATION)) typeField.setText("GEN");
        else if (mtu.getType().equals(MTU.MTUType.STAND_ALONE_NET)) typeField.setText("STD ALONE");


        descriptionField.setValue(mtu.getDescription());

        descriptionField.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                if (descriptionField.getText().trim().length() == 0) {
                    mtu.setDescription("MTU " + Long.toHexString(mtu.getId()).toUpperCase());
                } else {
                    mtu.setDescription(descriptionField.getText().trim());
                }

                gatewayService.saveMTU(gateway, mtu, new TEDAsyncCallback<MTU>() {
                    @Override
                    public void onSuccess(MTU mtu) {
                        logger.fine("mtu " + mtu + " saved");
                    }
                });
            }
        });

        final DashboardConstants dc = DashboardConstants.INSTANCE;

        deleteButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                deleteButton.setEnabled(false);
                String title = dc.deleteMTUTitle() + " " + Long.toHexString(mtu.getId()).toUpperCase();

                YesNoPopup popup = new YesNoPopup(title, dc.deleteMTUMessage());
                popup.addCloseHandler(new CloseHandler<PopupPanel>() {
                    @Override
                    public void onClose(CloseEvent<PopupPanel> popupPanelCloseEvent) {
                        deleteButton.setEnabled(true);
                        gatewayService.deleteMTU(gateway, mtu, new TEDAsyncCallback<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                logger.fine(mtu + " deleted");
                            }
                        });
                    }
                });
            }
        });
    }

    public void setEnabled(boolean enabled) {
        descriptionField.setEnabled(enabled);
    }
}
