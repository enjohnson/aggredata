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


import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.ted.aggredata.client.resources.DashboardImageResource;

/**
 * Simple widget to provide flexible padding/spacing on layouts
 */
public class ClearImage extends Composite {

    final Image spaceImage = new Image(DashboardImageResource.INSTANCE.clearImage());

    private final String width;
    private final String height;

    AbsolutePanel absolutePanel = new AbsolutePanel();

    public
    @UiConstructor
    ClearImage(String width, String height) {

        if (width == null) this.width = "1px";
        else this.width = width;

        if (height == null) this.height = "1px";
        else this.height = height;

        spaceImage.setSize(width, height);
        absolutePanel.setSize(width, height);
        spaceImage.getElement().getStyle().setMargin(0, Style.Unit.PX);
        spaceImage.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
        absolutePanel.add(spaceImage, 0,0);
        initWidget(absolutePanel);
    }


    public void addClickHandler(ClickHandler clickHandler) {
        spaceImage.addClickHandler(clickHandler);
    }
}
