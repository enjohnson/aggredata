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

package com.ted.aggredata.client.widgets;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.ted.aggredata.client.resources.DashboardImageResource;

/**
 * Generic widget wrapper for a large graphical button
 */
public class SmallButton extends Composite {

    final ClearImage clearImage = new ClearImage("65px", "20px");
    final Image buttonImage = new Image(DashboardImageResource.INSTANCE.smallButton());

    public @UiConstructor
    SmallButton(String buttonString)
    {
        AbsolutePanel mainPanel = new AbsolutePanel();
        mainPanel.setSize("65px", "20px");


        TEDLabel label = new TEDLabel(buttonString, "65px", "smallButtonTextStyle", HasHorizontalAlignment.ALIGN_CENTER);
        label.setHeight("20px");
        mainPanel.add(buttonImage, 0, 0);
        mainPanel.add(label, 0,4);
        mainPanel.add(clearImage, 0,0);
        initWidget(mainPanel);
    }

    public void addClickHandler(ClickHandler clickHandler){
        clearImage.addClickHandler(clickHandler);
    }

    public void setVisible(boolean visible)
    {
         buttonImage.setVisible(visible);
         clearImage.setVisible(visible);
    }
}
