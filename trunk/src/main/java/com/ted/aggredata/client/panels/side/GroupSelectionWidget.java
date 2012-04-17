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
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.ted.aggredata.client.events.GroupSelectedEvent;
import com.ted.aggredata.client.events.GroupSelectedHandler;
import com.ted.aggredata.client.guiService.GWTGroupService;
import com.ted.aggredata.client.guiService.GWTGroupServiceAsync;
import com.ted.aggredata.client.guiService.TEDAsyncCallback;
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

    final GWTGroupServiceAsync groupService = (GWTGroupServiceAsync) GWT.create(GWTGroupService.class);

    @UiField
    Tree groupTree;

    List<Group> groupList;
    final private HandlerManager handlerManager;

    public GroupSelectionWidget() {
        initWidget(uiBinder.createAndBindUi(this));
        handlerManager = new HandlerManager(this);
        groupTree.clear();

        //TODO: Add gateways as leafs under the groups so that they can be individually selectable
        groupService.findGroups(new TEDAsyncCallback<List<Group>>() {
            @Override
            public void onSuccess(List<Group> groups) {
                for (Group group : groups) {
                    groupList = groups;
                    TreeItem groupTreeItem = new GroupTreeItem(group);
                    groupTree.addItem(groupTreeItem);

                }
            }
        });

        groupTree.addSelectionHandler(new SelectionHandler<TreeItem>() {
            @Override
            public void onSelection(SelectionEvent<TreeItem> treeItemSelectionEvent) {
                logger.fine("tree item selected");
                TreeItem treeItem = treeItemSelectionEvent.getSelectedItem();
                if (treeItem instanceof GroupTreeItem) {
                    GroupTreeItem groupTreeItem = (GroupTreeItem) treeItem;
                    logger.fine("group selected " + groupTreeItem.getGroup());
                    handlerManager.fireEvent(new GroupSelectedEvent(groupTreeItem.getGroup()));
                }
            }
        });


    }

    public HandlerRegistration addGroupSelectedHandler(GroupSelectedHandler handler) {
        return handlerManager.addHandler(GroupSelectedEvent.TYPE, handler);
    }
}
