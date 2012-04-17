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
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.ted.aggredata.client.dialogs.YesNoPopup;
import com.ted.aggredata.client.events.GatewaySelectedEvent;
import com.ted.aggredata.client.events.GatewaySelectedHandler;
import com.ted.aggredata.client.guiService.GWTGatewayService;
import com.ted.aggredata.client.guiService.GWTGatewayServiceAsync;
import com.ted.aggredata.client.guiService.TEDAsyncCallback;
import com.ted.aggredata.client.resources.lang.DashboardConstants;
import com.ted.aggredata.client.widgets.SmallButton;
import com.ted.aggredata.model.Gateway;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GatewaySelectionPanel extends Composite {

    static Logger logger = Logger.getLogger(GatewaySelectionPanel.class.toString());


    interface MyUiBinder extends UiBinder<Widget, GatewaySelectionPanel> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField
    SmallButton deleteButton;
    @UiField
    ListBox gatewayListBox;
    @UiField
    CaptionPanel captionPanel;

    final private HandlerManager handlerManager;

    final GWTGatewayServiceAsync gatewayService = (GWTGatewayServiceAsync) GWT.create(GWTGatewayService.class);

    List<Gateway> gatewayList = new ArrayList<Gateway>();
    Gateway selectedGateway;

    public GatewaySelectionPanel() {
        initWidget(uiBinder.createAndBindUi(this));
        handlerManager = new HandlerManager(this);

        captionPanel.setCaptionHTML("<span style='color:white'>" + DashboardConstants.INSTANCE.yourGateways() + "</span>");

        gatewayListBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                fireSelectedGroup();
            }
        });


        deleteButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (gatewayListBox.getSelectedIndex() == -1) return;

                final DashboardConstants dc = DashboardConstants.INSTANCE;
                deleteButton.setEnabled(false);

                final YesNoPopup popup = new YesNoPopup(dc.deleteGatewayTitle(), dc.deleteGatewayMessage());
                popup.addCloseHandler(new CloseHandler<PopupPanel>() {
                    @Override
                    public void onClose(CloseEvent<PopupPanel> popupPanelCloseEvent) {
                        deleteButton.setEnabled(true);
                        if (popup.getValue() == YesNoPopup.YES) {
                            logger.info("deleting gateway " + selectedGateway);
                            gatewayService.deleteGateway(selectedGateway, new TEDAsyncCallback<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    gatewayList.remove(selectedGateway);
                                    if (gatewayList.size() == 0) gatewayListBox.setSelectedIndex(-1);
                                    else gatewayListBox.setSelectedIndex(0);
                                    redrawGatewayList();
                                    fireSelectedGroup();
                                }
                            });
                        }

                    }
                });
            }
        });

    }

    /**
     * Sets the current gateway list
     */
    public void setGatewayList(List<Gateway> gatewayList) {
        this.gatewayList = gatewayList;
        redrawGatewayList();
        if (gatewayListBox.getItemCount() > 0) {
            gatewayListBox.setSelectedIndex(0);
            fireSelectedGroup();
        }
    }

    /**
     * Redraws/refreshes the groupListBox. the selected index is not lost.
     */
    public void redrawGatewayList() {
        int selectedIndex = gatewayListBox.getSelectedIndex();
        gatewayListBox.clear();
        for (Gateway gateway : gatewayList) {
            if (logger.isLoggable(Level.FINE)) logger.fine("Adding gateway " + gateway + " to list box");
            gatewayListBox.addItem(gateway.getDescription(), gateway.getId().toString());
        }
        gatewayListBox.setSelectedIndex(selectedIndex);
    }

    /**
     * Method to fire an event w/ the selected gateway
     */
    private void fireSelectedGroup() {
        int index = gatewayListBox.getSelectedIndex();
        if (logger.isLoggable(Level.FINE)) logger.fine("Row " + index + " selected");
        Long gatewayId = new Long(gatewayListBox.getValue(index));


        for (Gateway gateway : gatewayList) {
            if (gateway.getId().equals(gatewayId)) {
                this.selectedGateway = gateway;
                break;
            }
        }

        if (selectedGateway != null) {
            if (logger.isLoggable(Level.FINE)) logger.fine("Gateway selected: " + selectedGateway);
            handlerManager.fireEvent(new GatewaySelectedEvent(selectedGateway));
        }
    }

    public HandlerRegistration addGatewaySelectedHandler(GatewaySelectedHandler handler) {
        return handlerManager.addHandler(GatewaySelectedEvent.TYPE, handler);
    }

}
