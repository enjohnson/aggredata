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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.ted.aggredata.client.events.GatewaySelectedEvent;
import com.ted.aggredata.client.events.GatewaySelectedHandler;
import com.ted.aggredata.client.guiService.GWTGatewayService;
import com.ted.aggredata.client.guiService.GWTGatewayServiceAsync;
import com.ted.aggredata.client.guiService.TEDAsyncCallback;
import com.ted.aggredata.model.Gateway;
import com.ted.aggredata.model.Group;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GatewaysPanel extends Composite {

    static Logger logger = Logger.getLogger(GatewaysPanel.class.toString());

    final GWTGatewayServiceAsync gatewayService = (GWTGatewayServiceAsync) GWT.create(GWTGatewayService.class);

    interface MyUiBinder extends UiBinder<Widget, GatewaysPanel> {
    }
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField
    GatewayDetailsPanel gatewayDetailsPanel;
    @UiField
    GatewaySelectionPanel gatewaySelectionPanel;

    List<Gateway> gatewayList;

    public GatewaysPanel()
    {
        initWidget(uiBinder.createAndBindUi(this));

        gatewayDetailsPanel.setEnabled(false);

        //Add a handler to update the details section when a gateway is selected
        gatewaySelectionPanel.addGatewaySelectedHandler(new GatewaySelectedHandler() {
            @Override
            public void onGatewaySelected(GatewaySelectedEvent event) {
                logger.fine("Gateway Selected: " + event.getGateway());
                final Gateway selectedGateway = event.getGateway();
                gatewayDetailsPanel.setGateway(selectedGateway);
            }
        });

        logger.fine("Looking up gateways");
        gatewayService.findGateways(new TEDAsyncCallback<List<Gateway>>() {
            @Override
            public void onSuccess(List<Gateway> gateways) {
                gatewayList = gateways;
                gatewayDetailsPanel.setGatewayList(gateways);
                gatewaySelectionPanel.setGatewayList(gateways);
            }
        });

    }

}
