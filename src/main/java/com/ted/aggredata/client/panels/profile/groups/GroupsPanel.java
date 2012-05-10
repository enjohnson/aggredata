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
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.ted.aggredata.client.Aggredata;
import com.ted.aggredata.client.events.GroupSelectedEvent;
import com.ted.aggredata.client.events.GroupSelectedHandler;
import com.ted.aggredata.client.guiService.*;
import com.ted.aggredata.model.Gateway;
import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.User;

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
    final GWTGatewayServiceAsync gatewayService = (GWTGatewayServiceAsync) GWT.create(GWTGatewayService.class);
    final GWTUserServiceAsync userService = (GWTUserServiceAsync) GWT.create(GWTUserService.class);

    List<User> userList;
    List<Group> groupList;
    List<Gateway> gatewayList;


    public GroupsPanel() {
        initWidget(uiBinder.createAndBindUi(this));
        groupDetailsPanel.setEnabled(false);

        groupSelectionPanel.addGroupSelectedHandler(new GroupSelectedHandler() {
            @Override
            public void onGroupSelected(GroupSelectedEvent event) {
                logger.fine("Group Selected: " + event.getGroup());
                final Group selectedGroup = event.getGroup();
                gatewayService.findGateways(event.getGroup(), new TEDAsyncCallback<List<Gateway>>() {
                    @Override
                    public void onSuccess(List<Gateway> gateways) {
                        groupDetailsPanel.setGroup(groupDetailsPanel.getSelectedUser(), selectedGroup, gateways);
                    }
                });

            }
        });


        //We split up the lookups (even though they are almost identical) to prevent an extra server and database
        //hit.
        if (Aggredata.GLOBAL.getSessionUser().getRole().equals(User.ROLE_ADMIN)) {

            //We only need this handler for admins.
            groupDetailsPanel.addUserChangeHandler(new ChangeHandler() {
                @Override
                public void onChange(ChangeEvent changeEvent) {
                    User selectedUser = groupDetailsPanel.getSelectedUser();
                    logger.fine("selected user is " + selectedUser);
                    findGateways(selectedUser);
                }
            });


            //Admin access. Find users AND groups
            userService.findUsers(new TEDAsyncCallback<List<User>>() {
                @Override
                public void onSuccess(List<User> users) {
                    userList = users;
                    groupDetailsPanel.setUsers(users);
                    groupService.findGroups(new TEDAsyncCallback<List<Group>>() {
                        @Override
                        public void onSuccess(List<Group> groups) {
                            if (logger.isLoggable(Level.INFO)) logger.info("Found " + groups.size() + groups);
                            groupList = groups;
                            groupSelectionPanel.setGroupList(groupList);
                            findGateways(Aggredata.GLOBAL.getSessionUser());
                        }
                    });
                }
            });
        } else {
            //NON-ADMIN access...find groups only
            groupService.findGroups(new TEDAsyncCallback<List<Group>>() {
                @Override
                public void onSuccess(List<Group> groups) {
                    if (logger.isLoggable(Level.INFO)) logger.info("Found " + groups.size() + groups);
                    groupList = groups;
                    groupSelectionPanel.setGroupList(groupList);
                    findGateways(Aggredata.GLOBAL.getSessionUser());

                }
            });
        }


    }


    //Finds all the gateways for the specific user
    private void findGateways(final User user){
        logger.fine("find Gateways called for " + user);
        gatewayService.findGateways(user , new TEDAsyncCallback<List<Gateway>>() {
            @Override
            public void onSuccess(List<Gateway> gateways) {
                gatewayList = gateways;
                groupDetailsPanel.setGroupList(groupList, gatewayList);
                groupSelectionPanel.fireSelectedGroup();
            }
        });
    }

}
