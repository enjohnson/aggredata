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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;

/**
 * Generic image button implementation
 */
public class TEDButton extends Composite {


    final ClearImage clearImage;
    final Image buttonImage;
    final AbsolutePanel mainPanel = new AbsolutePanel();
    final TEDLabel label;

    boolean enabled = true;

    public
    @UiConstructor
    TEDButton(ImageResource imgResource, String text, String width, String height, String style, int textOffset) {
        clearImage = new ClearImage(width, height);
        buttonImage = new Image(imgResource);
        buttonImage.setSize(width, height);
        mainPanel.setSize(width, height);

        label = new TEDLabel(text, width, style, HasHorizontalAlignment.ALIGN_CENTER);
        mainPanel.add(buttonImage, 0, 0);
        mainPanel.add(label, 0, textOffset);
        mainPanel.add(clearImage, 0, 0);
        enabled = true;
        initWidget(mainPanel);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        mainPanel.remove(clearImage);
        if (enabled) {
            mainPanel.add(clearImage, 0, 0);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void addClickHandler(ClickHandler clickHandler) {
        clearImage.addClickHandler(clickHandler);
    }

    public void setVisible(boolean visible) {
        buttonImage.setVisible(visible);
        clearImage.setVisible(visible);
        label.setVisible(visible);
    }


}
