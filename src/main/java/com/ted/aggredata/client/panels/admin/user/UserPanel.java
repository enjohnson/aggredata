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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.ted.aggredata.client.resources.lang.DashboardConstants;
import com.google.gwt.user.client.ui.FlexTable;

import java.util.logging.Logger;

public class UserPanel extends Composite {
    @UiField FlexTable table;
    static Logger logger = Logger.getLogger(UserPanel.class.toString());
    interface MyUiBinder extends UiBinder<Widget, UserPanel> {
    }
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    public UserPanel()
    {
        setupTable();
        initWidget(uiBinder.createAndBindUi(this));
    }

    private void setupTable()
    {
        table = new FlexTable();
        table.setText(0, 0, "Header 1");
        table.setText(0, 1, "Header 2");
        table.insertRow(1);
    }
}
