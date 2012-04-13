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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.ted.aggredata.client.Aggredata;
import com.ted.aggredata.client.events.GroupSelectedEvent;
import com.ted.aggredata.client.events.GroupSelectedHandler;
import com.ted.aggredata.client.guiService.GWTGroupService;
import com.ted.aggredata.client.guiService.GWTGroupServiceAsync;
import com.ted.aggredata.client.guiService.TEDAsyncCallback;
import com.ted.aggredata.client.resources.lang.DashboardConstants;
import com.ted.aggredata.model.Group;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GroupsPanel extends Composite {

    static Logger logger = Logger.getLogger(GroupsPanel.class.toString());

    interface MyUiBinder extends UiBinder<Widget, GroupsPanel> {
    }
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField
    GroupDetailsPanel groupDetailsPanel;
    @UiField
    GroupSelectionPanel groupSelectionPanel;


    final GWTGroupServiceAsync groupService = (GWTGroupServiceAsync) GWT.create(GWTGroupService.class);

    List<Group> groupList;


    public GroupsPanel()
    {
        initWidget(uiBinder.createAndBindUi(this));
        groupDetailsPanel.setEnabled(false);

        groupSelectionPanel.addGroupSelectedHandler(new GroupSelectedHandler() {
            @Override
            public void onGroupSelected(GroupSelectedEvent event) {
                logger.fine("Group Selected: " + event.getGroup());
                groupDetailsPanel.setGroup(event.getGroup());
            }
        });


        //Load the groups for the user and populate the listbox
        groupService.findGroups(Aggredata.GLOBAL.getSessionUser(), new TEDAsyncCallback<List<Group>>() {
            @Override
            public void onSuccess(List<Group> groups) {
                if (logger.isLoggable(Level.INFO)) logger.info("Found " + groups.size() + groups);
                groupList = groups;
                groupSelectionPanel.setGroupList(groupList);
                groupDetailsPanel.setGroupList(groupList);
            }
        });

    }

}
