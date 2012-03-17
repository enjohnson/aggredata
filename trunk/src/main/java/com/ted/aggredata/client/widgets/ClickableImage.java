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

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * Generic widget that implements a clickable image.
 */
public class ClickableImage extends Composite {

    final Image baseImage;
    final Image highImage;
    final DeckPanel deckPanel;


    /**
     * Constructs a clickable image that also has a mouseover effect (image highlight)
     *
     * @param baseImage
     * @param highImage
     * @param width
     * @param height
     */
    public ClickableImage(Image baseImage, Image highImage, String width, String height) {
        this.baseImage = baseImage;
        this.highImage = highImage;
        this.deckPanel = new DeckPanel();
        deckPanel.add(baseImage);
        deckPanel.add(highImage);
        deckPanel.setSize(width, height);
        deckPanel.showWidget(0);

        baseImage.addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                deckPanel.showWidget(1);
            }
        });

        highImage.addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                deckPanel.showWidget(0);
            }
        });
        initWidget(deckPanel);
    }

    /**
     * Constructs a clickable image
     *
     * @param baseImage
     * @param width
     * @param height
     */
    public ClickableImage(Image baseImage, String width, String height) {
        this.baseImage = baseImage;
        this.highImage = null;
        this.deckPanel = new DeckPanel();
        deckPanel.add(baseImage);
        deckPanel.setSize(width, height);
        deckPanel.showWidget(0);
        initWidget(deckPanel);
    }

    /**
     * Catches the event when the button is clicked
     *
     * @param clickHandler
     */
    public void addClickHandler(ClickHandler clickHandler) {
        if (highImage != null) highImage.addClickHandler(clickHandler);
        baseImage.addClickHandler(clickHandler);
    }

}
