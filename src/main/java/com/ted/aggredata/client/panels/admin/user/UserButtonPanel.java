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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.ted.aggredata.client.panels.profile.gateways.MTUListRow;
import com.ted.aggredata.client.panels.profile.gateways.MTUListRowHeader;
import com.ted.aggredata.model.Gateway;
import com.ted.aggredata.model.MTU;

import java.util.List;
import java.util.logging.Logger;

public class UserButtonPanel extends Composite {

    static Logger logger = Logger.getLogger(UserButtonPanel.class.toString());

    interface MyUiBinder extends UiBinder<Widget, UserButtonPanel> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    VerticalPanel userButtonPanel;
    @UiField
    CaptionPanel captionPanel;

    public UserButtonPanel() {
        initWidget(uiBinder.createAndBindUi(this));
        captionPanel.setCaptionHTML("<span style='color:white'>Options</span>");

    }

}
