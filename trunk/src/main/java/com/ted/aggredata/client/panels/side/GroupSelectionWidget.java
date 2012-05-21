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

package com.ted.aggredata.client.panels.side;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.ted.aggredata.client.events.GroupSelectedEvent;
import com.ted.aggredata.client.events.GroupSelectedHandler;
import com.ted.aggredata.model.Group;

import java.util.List;
import java.util.logging.Logger;

/**
 * Side Panel Widget that allows a group to be selected.
 */
public class GroupSelectionWidget extends Composite {

    interface MyUiBinder extends UiBinder<Widget, GroupSelectionWidget> {
    }

    static Logger logger = Logger.getLogger(GraphSidePanel.class.toString());
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    private HandlerManager handlerManager = null;
    @UiField
    VerticalPanel groupPanel;
    @UiField
    ScrollPanel scrollPanel;

    Group selectedGroup;

    public GroupSelectionWidget() {
        initWidget(uiBinder.createAndBindUi(this));
        handlerManager = new HandlerManager(this);
        groupPanel.clear();





    }

    public void setGroups(List<Group> groups, Group selectedGroup) {
        groupPanel.clear();
        for (final Group group : groups) {

            scrollPanel.setHeight("175px");

            RadioButton radioButton = new RadioButton("radioGroup", group.getDescription());
            groupPanel.add(radioButton);

            if (group.getRole() == Group.Role.OWNER) {
                //Set style for Owner
            } else {
                //Set Style for Shared
            }

            //Set the default value
            if (selectedGroup.getId().equals(group.getId())) radioButton.setValue(true);
            this.selectedGroup = selectedGroup;

            //Add the click handler
            radioButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    selectGroup(group);
                }
            });
        }
    }

    private Group getValue() {
        return selectedGroup;
    }

    private void selectGroup(Group group) {
        this.selectedGroup = group;
        logger.fine("Group selected: " + group);
        handlerManager.fireEvent(new GroupSelectedEvent(group));
    }

    public HandlerRegistration addGroupSelectedHandler(GroupSelectedHandler handler) {
        return handlerManager.addHandler(GroupSelectedEvent.TYPE, handler);
    }
}
