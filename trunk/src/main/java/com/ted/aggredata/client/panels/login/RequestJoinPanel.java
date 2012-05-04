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

package com.ted.aggredata.client.panels.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.ted.aggredata.client.Aggredata;
import com.ted.aggredata.client.dialogs.PasswordResetPopup;
import com.ted.aggredata.client.guiService.TEDAsyncCallback;
import com.ted.aggredata.client.guiService.UserSessionService;
import com.ted.aggredata.client.guiService.UserSessionServiceAsync;
import com.ted.aggredata.client.panels.join.JoinPanel;
import com.ted.aggredata.client.widgets.SmallButton;

import java.util.logging.Logger;


public class RequestJoinPanel extends Composite {


    static Logger logger = Logger.getLogger(RequestJoinPanel.class.toString());
    final UserSessionServiceAsync userSessionService = (UserSessionServiceAsync) GWT.create(UserSessionService.class);


    interface MyUiBinder extends UiBinder<Widget, RequestJoinPanel> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField
    SmallButton joinButton;
    @UiField
    VerticalPanel mainPanel;

    public RequestJoinPanel() {
        initWidget(uiBinder.createAndBindUi(this));

        if (Aggredata.GLOBAL==null || Aggredata.GLOBAL.getServerInfo() == null || !Aggredata.GLOBAL.getServerInfo().isAllowRegistration()) {
            logger.fine("Password Reset is disabled.");
            mainPanel.setVisible(false);
        }

        joinButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                RootPanel.get(Aggredata.ROOT_PANEL).clear();
                RootPanel.get(Aggredata.ROOT_PANEL).add(new JoinPanel());  //Redirect the user to the join panel.
            }
        });

    }

    /**
     * Sets the visibility of this widget
     * @param isVisible
     */
    public void setVisible(boolean isVisible){
        mainPanel.setVisible(isVisible);
    }



}
