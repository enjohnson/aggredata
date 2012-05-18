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

package com.ted.aggredata.client.panels.profile.groups;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.ted.aggredata.client.resources.lang.DashboardConstants;
import com.ted.aggredata.client.widgets.SmallButton;
import com.ted.aggredata.client.widgets.oracles.AggreDataUserSuggestOracle;
import com.ted.aggredata.model.Group;

import java.util.logging.Logger;


/***
 * Popup used to show the shares of the current group and allows users to be added or removed from it.
 */
public class GroupSharePopup extends PopupPanel {

    static Logger logger = Logger.getLogger(GroupSharePopup.class.toString());

    static DashboardConstants dashboardConstants = DashboardConstants.INSTANCE;

    interface MyUiBinder extends UiBinder<Widget, GroupSharePopup> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);


    final Group group;
    @UiField
    CaptionPanel captionPanel;


    @UiField  (provided = true) //Provided since we have to set an Oracle for it before binding.
    SuggestBox userSuggestBox;

    public GroupSharePopup(Group group) {
        userSuggestBox = new SuggestBox(new AggreDataUserSuggestOracle());
        setWidget(uiBinder.createAndBindUi(this));

        this.group = group;

        this.getElement().getStyle().setBorderColor("#c3c1c1");
        this.getElement().getStyle().setBackgroundColor("#1c1c1c");
        SafeHtmlBuilder  title = new SafeHtmlBuilder();
        title.appendHtmlConstant("<span style='color:white'>");
        title.appendHtmlConstant(dashboardConstants.groupShareHeader());
        title.appendHtmlConstant(" '").appendEscaped(group.getDescription()).appendEscaped("'");
        title.appendHtmlConstant("</span>");
        captionPanel.setCaptionHTML(title.toSafeHtml());

    }

}