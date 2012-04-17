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
import com.google.gwt.user.client.ui.*;
import com.ted.aggredata.client.resources.DashboardImageResource;

/**
 * Wrapper for a single menu item on the navigation menu.
 */
public class MenuItem extends Composite {

    final Boolean isLeftMost;
    final TEDLabel label;
    Boolean selected;


    public MenuItem(String text, boolean isLeftMost, String width, String height) {
        HorizontalPanel mainPanel = new HorizontalPanel();
        this.isLeftMost = isLeftMost;
        label = new TEDLabel(text, width, "menuText", HasHorizontalAlignment.ALIGN_CENTER);
        label.setHeight(height);
        selected = false;

        if (!isLeftMost) {
            final Image divImage = new Image(DashboardImageResource.INSTANCE.menuDivider());
            mainPanel.add(divImage);
            mainPanel.setCellVerticalAlignment(divImage, HasVerticalAlignment.ALIGN_BOTTOM);
        }
        mainPanel.add(label);
        mainPanel.setCellVerticalAlignment(label, HasVerticalAlignment.ALIGN_BOTTOM);

        initWidget(mainPanel);
    }


    public Boolean isSelected() {
        return selected;
    }


    public void setSelected(Boolean selected) {
        if (selected) {
            this.selected = true;
            label.setStylePrimaryName("menuTextSelected");
        } else {
            this.selected = false;
            label.setStylePrimaryName("menuText");
        }

    }

    public void addClickHandler(ClickHandler handler) {
        label.addClickHandler(handler);
    }
}
