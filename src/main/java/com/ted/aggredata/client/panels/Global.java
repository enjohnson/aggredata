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

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface Global extends ClientBundle {


    @Source("Global.css")
    Style style();

    public interface Style extends CssResource {
        String gatewayListLabel();

        String sideBarFormField();

        String sideBarInstructions();

        String gridHeader();

        String captionHeader();

        String formLabel();

        String mtuTypeField();

        String activateInfoText();

        String joinHeaderText();

        String panelTitleText();

        String formLabelLeft();

        String panelText();

        String sideBarLabel();

        String formError();

        String sideBarHeader();

        String graphTitle();

        String formFieldError();

        String panelInstructionText();

        String mainBody();

        String formErrorCentered();

        String activateTitleText();

        String formListBox();

        String formField();

        String clickableRedLink();

        String userSelectionPopup();

        @ClassName("item-selected")
        String itemSelected();

        String largeButtonTextStyle();

        String menuText();

        String menuTextSelected();

        String dashboardTabText();

        String smallButtonTextStyle();
    }
}

