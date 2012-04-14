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
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.ted.aggredata.client.guiService.GWTGroupService;
import com.ted.aggredata.client.guiService.GWTGroupServiceAsync;
import com.ted.aggredata.client.guiService.TEDAsyncCallback;
import com.ted.aggredata.client.resources.lang.DashboardConstants;
import com.ted.aggredata.client.widgets.SmallButton;
import com.ted.aggredata.model.Gateway;
import com.ted.aggredata.model.Group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GroupDetailsPanel extends Composite {

    final GWTGroupServiceAsync groupService = (GWTGroupServiceAsync) GWT.create(GWTGroupService.class);

    static Logger logger = Logger.getLogger(GroupDetailsPanel.class.toString());

    interface MyUiBinder extends UiBinder<Widget, GroupDetailsPanel> {
    }
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField
    TextBox descriptionField;
    @UiField
    Label descriptionFieldError;
    @UiField
    TextBox custom1Field;
    @UiField
    Label custom1FieldError;
    @UiField
    TextBox custom2Field;
    @UiField
    Label custom2FieldError;
    @UiField
    TextBox custom3Field;
    @UiField
    Label custom3FieldError;
    @UiField
    TextBox custom4Field;
    @UiField
    Label custom4FieldError;
    @UiField
    TextBox custom5Field;
    @UiField
    Label custom5FieldError;
    @UiField
    GroupGatewaysPanel groupGatewaysPanel;
    @UiField
    CaptionPanel captionPanel;

    Group group;
    Integer groupHashCode = 0;
    List<Group> groupList = new ArrayList<Group>();
    List<Gateway> userGatewayList = new ArrayList<Gateway>();
    List<Gateway> groupGatewayList = new ArrayList<Gateway>();



    ChangeHandler saveChangeHanlder = new ChangeHandler() {
        @Override
        public void onChange(ChangeEvent changeEvent) {
            if (group != null) doSave();
        }
    };

    public GroupDetailsPanel()
    {
        initWidget(uiBinder.createAndBindUi(this));

        captionPanel.setCaptionHTML("<span style='color:white'>" + DashboardConstants.INSTANCE.groupDetails() + "</span>");

        descriptionField.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent keyUpEvent) {
                validate();
            }
        });

        descriptionField.addChangeHandler(saveChangeHanlder);
        custom1Field.addChangeHandler(saveChangeHanlder);
        custom2Field.addChangeHandler(saveChangeHanlder);
        custom3Field.addChangeHandler(saveChangeHanlder);
        custom4Field.addChangeHandler(saveChangeHanlder);
        custom5Field.addChangeHandler(saveChangeHanlder);

    }

    public void setGroupList(List<Group> groupList, List<Gateway> userGatewayList) {
        this.groupList = groupList;
        this.userGatewayList = userGatewayList;
    }


    /**
     * Sets all the fields of this panel to be enabled or disabled
     * @param enabled
     */
    public void setEnabled(boolean enabled)
    {

        descriptionField.setEnabled(enabled);
        custom1Field.setEnabled(enabled);
        custom2Field.setEnabled(enabled);
        custom3Field.setEnabled(enabled);
        custom4Field.setEnabled(enabled);
        custom5Field.setEnabled(enabled);
        groupGatewaysPanel.setEnabled(enabled);

    }


    public boolean validate(){
        boolean valid = true;
        descriptionFieldError.setText("");

        if (group == null) return true;
        if (descriptionField.getText().trim().length() == 0) {
            valid = false;
            descriptionFieldError.setText("Required");
        }

        for (Group g:groupList){
            if (!g.getId().equals(group.getId()) && g.getDescription().toLowerCase().equals(descriptionField.getText().trim().toLowerCase())){
                valid = false;
                descriptionFieldError.setText("Already Used");
            }
        }

        return valid;
    }

    private void doSave()
    {
        if (validate())
        {
            group.setDescription(descriptionField.getText().trim());
            group.setCustom1(custom1Field.getText().trim());
            group.setCustom2(custom2Field.getText().trim());
            group.setCustom3(custom3Field.getText().trim());
            group.setCustom4(custom4Field.getText().trim());
            group.setCustom5(custom5Field.getText().trim());
            if (group.hashCode() != groupHashCode){
                logger.info("Group is dirty. Saving " + group);
                groupService.saveGroup(group, new TEDAsyncCallback<Group>() {
                    @Override
                    public void onSuccess(Group group) {
                        groupHashCode = group.hashCode();
                    }
                });
            }
        }
    }

    public void setGroup(Group group, List<Gateway> groupGatewayList)
    {
        if (logger.isLoggable(Level.FINE)) logger.fine("Setting group " + group);
        setEnabled(group!=null);
        descriptionField.setValue(group.getDescription());
        custom1Field.setValue(group.getCustom1());
        custom2Field.setValue(group.getCustom2());
        custom3Field.setValue(group.getCustom3());
        custom4Field.setValue(group.getCustom4());
        custom5Field.setValue(group.getCustom5());
        this.group = group;
        groupHashCode = group.hashCode();
        this.groupGatewayList = groupGatewayList;
        groupGatewaysPanel.setMap(group, userGatewayList, groupGatewayList);
        validate();
    }


}
