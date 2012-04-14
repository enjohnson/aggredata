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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.ted.aggredata.client.Aggredata;
import com.ted.aggredata.client.dialogs.OKPopup;
import com.ted.aggredata.client.dialogs.YesNoPopup;
import com.ted.aggredata.client.events.GroupSelectedEvent;
import com.ted.aggredata.client.events.GroupSelectedHandler;
import com.ted.aggredata.client.guiService.GWTGroupService;
import com.ted.aggredata.client.guiService.GWTGroupServiceAsync;
import com.ted.aggredata.client.guiService.TEDAsyncCallback;
import com.ted.aggredata.client.resources.lang.DashboardConstants;
import com.ted.aggredata.client.widgets.SmallButton;
import com.ted.aggredata.model.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GroupSelectionPanel extends Composite {



    static Logger logger = Logger.getLogger(GroupSelectionPanel.class.toString());


    interface MyUiBinder extends UiBinder<Widget, GroupSelectionPanel> {
    }
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField
    SmallButton addButton;
    @UiField
    SmallButton deleteButton;
    @UiField
    ListBox groupListBox;
    @UiField
    CaptionPanel captionPanel;

    final private HandlerManager handlerManager;

    final GWTGroupServiceAsync groupService = (GWTGroupServiceAsync) GWT.create(GWTGroupService.class);

    List<Group> groupList = new ArrayList<Group>();
    Group selectedGroup;

    public GroupSelectionPanel()
    {
        initWidget(uiBinder.createAndBindUi(this));
        handlerManager = new HandlerManager(this);

        captionPanel.setCaptionHTML("<span style='color:white'>" + DashboardConstants.INSTANCE.yourGroups() + "</span>");


        groupListBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                fireSelectedGroup();
            }
        });

        addButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                logger.info("Adding new group");
                final int index = groupListBox.getItemCount();

                groupService.createGroup( "New Group " + index, new TEDAsyncCallback<Group>() {
                    @Override
                    public void onSuccess(Group group) {

                        groupListBox.addItem(group.getDescription(), group.getId().toString());
                        groupList.add(group);
                        groupListBox.setSelectedIndex(index);
                        fireSelectedGroup();
                    }
                });
            }
        });

        deleteButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (groupListBox.getSelectedIndex() == -1) return;

                final DashboardConstants dc = DashboardConstants.INSTANCE;
                deleteButton.setEnabled(false);

                if (groupListBox.getItemCount() == 1) {
                    OKPopup okPopup = new OKPopup( dc.deleteGroupCantDeleteTitle(), dc.deleteGroupCantDeleteText());
                    okPopup.addCloseHandler(new CloseHandler<PopupPanel>() {
                        @Override
                        public void onClose(CloseEvent<PopupPanel> popupPanelCloseEvent) {
                            deleteButton.setEnabled(true);
                        }
                    });
                    return;
                }


                StringBuilder delMessage = new StringBuilder();
                delMessage.append(dc.deleteGroupMessage()).append(" \"").append(selectedGroup.getDescription()).append("\"?");
                final YesNoPopup popup = new YesNoPopup(dc.deleteGroupTitle(), delMessage.toString());
                popup.addCloseHandler(new CloseHandler<PopupPanel>() {
                    @Override
                    public void onClose(CloseEvent<PopupPanel> popupPanelCloseEvent) {
                        deleteButton.setEnabled(true);
                        if (popup.getValue() == YesNoPopup.YES) {
                            logger.info("deleting group " + selectedGroup);
                            groupService.deleteGroup(selectedGroup, new TEDAsyncCallback<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    groupList.remove(selectedGroup);
                                    if (groupList.size() == 0) groupListBox.setSelectedIndex(-1);
                                    else groupListBox.setSelectedIndex(0);
                                    redrawGroupList();
                                    fireSelectedGroup();
                                }
                            });
                        }

                    }
                });
            }
        });

    }

    /**
     * Sets the current group list
     * @param groupList
     */
    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
        redrawGroupList();
        if (groupListBox.getItemCount() > 0) {
            groupListBox.setSelectedIndex(0);
            fireSelectedGroup();
        }
    }

    /**
     * Redraws/refreshes the groupListBox. the selected index is not lost.
     */
    public void redrawGroupList()
    {
        int selectedIndex = groupListBox.getSelectedIndex();
        groupListBox.clear();
        for (Group group : groupList)
        {
            if (group.getRole() == Group.Role.OWNER)
            {
                if (logger.isLoggable(Level.FINE)) logger.fine("Adding group " + group + " to list box");
                groupListBox.addItem(group.getDescription(), group.getId().toString());
            }
        }
        groupListBox.setSelectedIndex(selectedIndex);
    }

    /***
     * Method to fire an event w/ the selected group
     */
    private void fireSelectedGroup(){
        int index = groupListBox.getSelectedIndex();
        if (logger.isLoggable(Level.FINE)) logger.fine("Row " + index + " selected");
        Long groupId = new Long(groupListBox.getValue(index));


        for (Group group: groupList) {
            if (group.getId().equals(groupId)){
                this.selectedGroup = group;
                break;
            }
        }

        if (selectedGroup != null) {
            if (logger.isLoggable(Level.FINE)) logger.fine("Group selected: " + selectedGroup);
            handlerManager.fireEvent(new GroupSelectedEvent(selectedGroup));
        }
    }

    public HandlerRegistration addGroupSelectedHandler (GroupSelectedHandler handler) {
        return handlerManager.addHandler(GroupSelectedEvent.TYPE, handler);
    }

}
