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

package com.ted.aggredata.client.panels;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.ted.aggredata.client.resources.DashboardImageResource;

/**
 * Top level frame panel
 */
public class AggredataPanel extends Composite {

    final AbsolutePanel mainPanel = new AbsolutePanel();

    public AggredataPanel() {
        mainPanel.setSize("1018px", "720px");
        mainPanel.add(new Image(DashboardImageResource.INSTANCE.contentBackground()), 0, 0);
        mainPanel.add(new MainPanel(), 0, 0);
        initWidget(mainPanel);
    }

}
