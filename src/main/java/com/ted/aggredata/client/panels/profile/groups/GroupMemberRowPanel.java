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
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.ted.aggredata.client.events.DateRangeSelectedEvent;
import com.ted.aggredata.client.events.DateRangeSelectedHandler;
import com.ted.aggredata.client.events.GroupMemberRemovedEvent;
import com.ted.aggredata.client.events.GroupMemberRemovedHandler;
import com.ted.aggredata.client.widgets.SmallButton;
import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.User;

import java.util.logging.Logger;

public class GroupMemberRowPanel extends Composite {

    static Logger logger = Logger.getLogger(GroupMemberRowPanel.class.toString());



    interface MyUiBinder extends UiBinder<Widget, GroupMemberRowPanel> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    final Group group;
    final User user;
    @UiField
    Label userLabel;
    @UiField
    SmallButton removeButton;
    @UiField
    HorizontalPanel mainPanel;

    final private HandlerManager handlerManager;

    public GroupMemberRowPanel(final Group group, final User user, boolean isOdd) {
        initWidget(uiBinder.createAndBindUi(this));
        this.group = group;
        this.user = user;
        this.handlerManager = new HandlerManager(this);

        if (isOdd) {
            mainPanel.getElement().getStyle().setBackgroundColor("#222222");
        }


        StringBuilder displayBuilder = new StringBuilder();
        displayBuilder.append(user.getFirstName());
        displayBuilder.append(" ");
        displayBuilder.append(user.getLastName());
        displayBuilder.append(" (");
        displayBuilder.append(user.getUsername());
        displayBuilder.append(" )");
        userLabel.setText(displayBuilder.toString());

        removeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                handlerManager.fireEvent(new GroupMemberRemovedEvent(group, user));
            }
        });
    }

    public HandlerRegistration addRemovedHandler(GroupMemberRemovedHandler handler) {
        return handlerManager.addHandler(GroupMemberRemovedEvent.TYPE, handler);
    }

    public User getUser(){
        return user;
    }

}