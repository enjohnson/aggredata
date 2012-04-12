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

public class GroupDetailsPanel extends Composite {

    static Logger logger = Logger.getLogger(GroupDetailsPanel.class.toString());

    interface MyUiBinder extends UiBinder<Widget, GroupDetailsPanel> {
    }
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField
    TextBox descriptionField;
    @UiField
    Label descriptionFieldError;
    @UiField
    TextBox custom1Field;
    @UiField
    Label custom1FieldError;
    @UiField
    TextBox custom2Field;
    @UiField
    Label custom2FieldError;
    @UiField
    TextBox custom3Field;
    @UiField
    Label custom3FieldError;
    @UiField
    TextBox custom4Field;
    @UiField
    Label custom4FieldError;
    @UiField
    TextBox custom5Field;
    @UiField
    Label custom5FieldError;
    @UiField
    GroupGatewaysPanel groupGatewaysPanel;


    public GroupDetailsPanel()
    {
        initWidget(uiBinder.createAndBindUi(this));

    }

    /**
     * Sets all the fields of this panel to be enabled or disabled
     * @param enabled
     */
    public void setEnabled(boolean enabled)
    {

        descriptionField.setEnabled(enabled);
        custom1Field.setEnabled(enabled);
        custom2Field.setEnabled(enabled);
        custom3Field.setEnabled(enabled);
        custom4Field.setEnabled(enabled);
        custom5Field.setEnabled(enabled);
        groupGatewaysPanel.setEnabled(enabled);

    }
}
