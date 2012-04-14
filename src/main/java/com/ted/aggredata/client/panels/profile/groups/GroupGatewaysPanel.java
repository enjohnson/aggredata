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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.ted.aggredata.client.resources.lang.DashboardConstants;
import com.ted.aggredata.client.widgets.SmallButton;
import com.ted.aggredata.model.Gateway;
import com.ted.aggredata.model.Group;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class GroupGatewaysPanel extends Composite {

    static Logger logger = Logger.getLogger(GroupGatewaysPanel.class.toString());


    interface MyUiBinder extends UiBinder<Widget, GroupGatewaysPanel> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    VerticalPanel gatewayListPanel;
    @UiField
    CaptionPanel captionPanel;


    //A list of gateways currently in this group
    final HashMap<Long, Gateway> groupGatewayMap = new HashMap<Long, Gateway>();


    public GroupGatewaysPanel() {
        initWidget(uiBinder.createAndBindUi(this));
        captionPanel.setCaptionHTML("<span style='color:white'>Gateways</span>");

    }

    public void setEnabled(boolean enabled) {
        for (int i = 0; i < gatewayListPanel.getWidgetCount(); i++) {
            GatewayListRow row = (GatewayListRow) gatewayListPanel.getWidget(i);
            row.setEnabled(enabled);
        }
    }

    public void clear() {
        groupGatewayMap.clear();
        gatewayListPanel.clear();
    }

    public void setMap(Group selectedGroup, List<Gateway> userGateways, List<Gateway> groupGateways) {
        groupGatewayMap.clear();
        gatewayListPanel.clear();

        //Build a list of group gateways.
        for (Gateway gateway : groupGateways) {

            groupGatewayMap.put(gateway.getId(), gateway);
        }


        //Build the selection rows
        int i=0;
        for (Gateway gateway : userGateways) {
            boolean isGroupMember = (null != groupGatewayMap.get(gateway.getId()));
            boolean oddRow = ((i++ % 2) == 0);
            GatewayListRow row = new GatewayListRow(selectedGroup, gateway, isGroupMember, oddRow);
            gatewayListPanel.add(row);
        }

    }

}
