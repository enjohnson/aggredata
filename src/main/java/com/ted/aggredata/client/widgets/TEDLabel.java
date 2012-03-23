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

/**
 * Wrapper for the GWT label class. Wraps basic formatting into the constructor.
 */
public class TEDLabel extends com.google.gwt.user.client.ui.Label {

    /**
     * Contructs a new TEDLabel
     *
     * @param text                the text being displayed
     * @param width               the width of the label
     * @param style               the style to apply to the label
     * @param horizontalAlignment the horizontal alignment of the label
     */
    public TEDLabel(String text, String width, String style, HorizontalAlignmentConstant horizontalAlignment) {
        super(text);
        if (!width.equals("")) setWidth(width);
        if (!style.equals("")) setStylePrimaryName(style);
        setHorizontalAlignment(horizontalAlignment);
    }

    /**
     * Contructs a new TEDLabel
     *
     * @param text  the text being displayed
     * @param width the width of the label
     * @param style the style to apply to the label
     */
    public TEDLabel(String text, String width, String style) {
        super(text);
        if (!width.equals("")) setWidth(width);
        if (!style.equals("")) setStylePrimaryName(style);
    }

}
