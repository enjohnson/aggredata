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

package com.ted.aggredata.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Resource bundle for images used on the AggreData dashboard.
 */
public interface DashboardImageResource extends ClientBundle {

    public static final DashboardImageResource INSTANCE = GWT.create(DashboardImageResource.class);

    @Source("images/tedlogo.png")
    ImageResource tedLogo();

    @Source("images/clear.png")
    ImageResource clearImage();

    @Source("images/lefttabbutton.png")
    ImageResource lefttabButton();

    @Source("images/tabbutton.png")
    ImageResource tabButton();

    @Source("images/tabdivider.png")
    ImageResource tabDivider();

    @Source("images/contentBackground.png")
    ImageResource contentBackground();

    @Source("images/menuDivider.png")
    ImageResource menuDivider();

}
