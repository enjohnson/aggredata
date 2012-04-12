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
import com.ted.aggredata.client.widgets.SmallButton;

import java.util.logging.Logger;

public class GroupGatewaysPanel extends Composite {

    static Logger logger = Logger.getLogger(GroupGatewaysPanel.class.toString());

    interface MyUiBinder extends UiBinder<Widget, GroupGatewaysPanel> {
    }
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    ListBox gatewayListBox;
    @UiField
    SmallButton addButton;
    @UiField
    SmallButton deleteButton;


    public GroupGatewaysPanel()
    {
        initWidget(uiBinder.createAndBindUi(this));

    }

    /**
     * Sets all the fields of this panel to be enabled or disabled
     * @param enabled
     */
    public void setEnabled(boolean enabled)
    {

        gatewayListBox.setEnabled(enabled);
        addButton.setVisible(!enabled);
        deleteButton.setVisible(!enabled);
    }

}
