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

package com.ted.aggredata.client.widgets.logo;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Image;

public class NavLogo extends Composite {

    final DeckPanel deckPanel = new DeckPanel();

    public NavLogo()
    {
        Image lowImage = new Image(NavLogoResources.INSTANCE.navLogo());
        Image highImage = new Image(NavLogoResources.INSTANCE.navLogoHigh()); 
        deckPanel.add(lowImage);
        deckPanel.add(highImage);
        deckPanel.setSize("297px", "78px");

        deckPanel.showWidget(0);

        lowImage.addMouseOverHandler(new MouseOverHandler() {
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

        highImage.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Window.open("http://www.theenergydetective.com", "_blank", "");
            }
        });

        initWidget(deckPanel);
    }

}
