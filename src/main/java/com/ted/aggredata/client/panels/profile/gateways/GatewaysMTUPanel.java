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
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.ted.aggredata.model.Gateway;
import com.ted.aggredata.model.MTU;

import java.util.List;
import java.util.logging.Logger;

public class GatewaysMTUPanel extends Composite {

    static Logger logger = Logger.getLogger(GatewaysMTUPanel.class.toString());


    final MTUListRowHeader header = new MTUListRowHeader();

    interface MyUiBinder extends UiBinder<Widget, GatewaysMTUPanel> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    VerticalPanel mtuListPanel;
    @UiField
    CaptionPanel captionPanel;

    public GatewaysMTUPanel() {
        initWidget(uiBinder.createAndBindUi(this));
        captionPanel.setCaptionHTML("<span style='color:white'>MTUs</span>");

    }

    public void setEnabled(boolean enabled) {
        for (int i = 0; i < mtuListPanel.getWidgetCount(); i++) {
            MTUListRow row = (MTUListRow) mtuListPanel.getWidget(i);
            row.setEnabled(enabled);
        }
    }

    public void clear() {
        mtuListPanel.clear();
    }


    public void setMTUList(Gateway gateway, List<MTU> mtuList) {
        mtuListPanel.clear();
        mtuListPanel.add(header);

        //Build the selection rows
        int i = 0;
        for (MTU mtu : mtuList) {
            boolean oddRow = ((i++ % 2) == 0);
            MTUListRow row = new MTUListRow(gateway, mtu, oddRow);
            mtuListPanel.add(row);
        }
    }

}
