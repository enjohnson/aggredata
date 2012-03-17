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
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.ted.aggredata.client.resources.DashboardImageResource;

/**
 * Widget that represents a single clickable tab
 */
public class DashboardTab extends Composite {

    final AbsolutePanel tabPanel = new AbsolutePanel();
    final Image selectedImage;
    final Image dividerImage = new Image(DashboardImageResource.INSTANCE.tabDivider());
    final TEDLabel label;
    final ClearImage clickImage = new ClearImage("95px", "36px");
    final Boolean isLeftButton;

    /**
     * Constructs a tab for the DashboardTabPanel
     *
     * @param text         the text to be displayed on the tab
     * @param isLeftButton whether or not this button is the leftmost button
     */
    public DashboardTab(String text, Boolean isLeftButton) {
        this.isLeftButton = isLeftButton;
        if (isLeftButton) selectedImage = new Image(DashboardImageResource.INSTANCE.lefttabButton());
        else selectedImage = new Image(DashboardImageResource.INSTANCE.tabButton());

        label = new TEDLabel(text, "95px", "dashboardTabText", HasHorizontalAlignment.ALIGN_CENTER);
        label.setHeight("36px");

        clickImage.setSize("95px", "36px");
        tabPanel.setSize("95px", "36px");

        tabPanel.add(selectedImage, 0, 0);
        tabPanel.add(dividerImage, 0, 0);
        tabPanel.add(label, 0, 3);
        tabPanel.add(clickImage, 0, 0);

        selectedImage.setVisible(false);
        initWidget(tabPanel);
    }

    public void setSelected(Boolean isSelected, Boolean showDivider) {
        selectedImage.setVisible(isSelected);
        dividerImage.setVisible(!isSelected && !isLeftButton && showDivider);
    }


    public Boolean isSelected() {
        return selectedImage.isVisible();
    }

    public void addClickHandler(ClickHandler clickHandler) {
        clickImage.addClickHandler(clickHandler);
    }
}
