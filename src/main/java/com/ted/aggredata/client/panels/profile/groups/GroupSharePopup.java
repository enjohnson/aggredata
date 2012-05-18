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
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.ted.aggredata.client.Aggredata;
import com.ted.aggredata.client.dialogs.OKPopup;
import com.ted.aggredata.client.dialogs.YesNoPopup;
import com.ted.aggredata.client.events.GroupMemberRemovedEvent;
import com.ted.aggredata.client.events.GroupMemberRemovedHandler;
import com.ted.aggredata.client.guiService.GWTGroupService;
import com.ted.aggredata.client.guiService.GWTGroupServiceAsync;
import com.ted.aggredata.client.guiService.TEDAsyncCallback;
import com.ted.aggredata.client.resources.lang.DashboardConstants;
import com.ted.aggredata.client.widgets.LongButton;
import com.ted.aggredata.client.widgets.SmallButton;
import com.ted.aggredata.client.widgets.oracles.AggreDataUserSuggestOracle;
import com.ted.aggredata.client.widgets.oracles.UserSuggestion;
import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.User;

import java.util.List;
import java.util.logging.Logger;


/***
 * Popup used to show the shares of the current group and allows users to be added or removed from it.
 */
public class GroupSharePopup extends PopupPanel {

    static Logger logger = Logger.getLogger(GroupSharePopup.class.toString());

    final GWTGroupServiceAsync groupService = (GWTGroupServiceAsync) GWT.create(GWTGroupService.class);

    static DashboardConstants dashboardConstants = DashboardConstants.INSTANCE;

    interface MyUiBinder extends UiBinder<Widget, GroupSharePopup> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);


    final Group group;
    @UiField
    CaptionPanel captionPanel;


    @UiField  (provided = true) //Provided since we have to set an Oracle for it before binding.
    SuggestBox userSuggestBox;
    @UiField
    LongButton closeButton;
    @UiField
    SmallButton addButton;
    @UiField
    VerticalPanel groupMembersPanel;


    User suggestedUser = null;

    public GroupSharePopup(final Group group) {
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
        refreshGroupMembersPanel();

        userSuggestBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            @Override
            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> suggestionSelectionEvent) {
                UserSuggestion userSuggestion = (UserSuggestion)suggestionSelectionEvent.getSelectedItem();
                if (userSuggestion != null) {
                    suggestedUser = userSuggestion.getUser();
                }
            }
        });



         addButton.addClickHandler(new ClickHandler() {
             @Override
             public void onClick(ClickEvent clickEvent) {
                if (suggestedUser != null) {
                    boolean alreadyMember = false;

                    //See if the user is already a member
                    if (suggestedUser.getId().equals(Aggredata.GLOBAL.getSessionUser().getId())) {
                        alreadyMember = true;
                    } else {
                        for (int i=0; i < groupMembersPanel.getWidgetCount(); i++){
                            GroupMemberRowPanel panel = (GroupMemberRowPanel)groupMembersPanel.getWidget(i);
                            if (suggestedUser.getId().equals(panel.getUser().getId())) {
                                alreadyMember = true;
                                break;
                            }
                        }
                    }

                    if (alreadyMember) {
                        OKPopup alreadyMemberPopup = new OKPopup(dashboardConstants.graphShareAlreadyExistsTitle(), dashboardConstants.graphShareAlreadyExists());
                        alreadyMemberPopup.center();
                        alreadyMemberPopup.setPopupPosition(alreadyMemberPopup.getAbsoluteLeft(), 150);
                        alreadyMemberPopup.show();
                        return;
                    }


                    groupService.addUserToGroup(group, suggestedUser, new TEDAsyncCallback<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            userSuggestBox.setValue("");
                            suggestedUser = null;
                            refreshGroupMembersPanel();
                        }
                    });
                } else {
                    addButton.setVisible(false);
                    OKPopup okPopup = new OKPopup("User Not Found", "No user selected. Please try again.");
                    okPopup.center();
                    okPopup.setPopupPosition(okPopup.getAbsoluteLeft(), 150);
                    okPopup.addCloseHandler(new CloseHandler<PopupPanel>() {
                        @Override
                        public void onClose(CloseEvent<PopupPanel> popupPanelCloseEvent) {
                            addButton.setVisible(true);
                            userSuggestBox.setValue("");
                        }
                    });
                    okPopup.show();

                }
             }
         });

        closeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                hide();
            }
        });
    }


    //Handler for when a user is removed
    GroupMemberRemovedHandler groupMemberRemovedHandler = new GroupMemberRemovedHandler() {
        @Override
        public void onGroupMemberRemoved(final GroupMemberRemovedEvent event) {
            StringBuilder confirmText = new StringBuilder();
            confirmText.append(dashboardConstants.graphShareMemberRemoveText()).append(" ");
            confirmText.append(event.getUser().getFirstName()).append(" ");
            confirmText.append(event.getUser().getLastName());

            final YesNoPopup confirmPopup = new YesNoPopup(dashboardConstants.graphShareMemberRemoveTitle(), confirmText.toString());
            confirmPopup.center();
            confirmPopup.setPopupPosition(confirmPopup.getAbsoluteLeft(), 150);

            confirmPopup.addCloseHandler(new CloseHandler<PopupPanel>() {
                @Override
                public void onClose(CloseEvent<PopupPanel> popupPanelCloseEvent) {
                    if (confirmPopup.getValue() == YesNoPopup.YES) {
                        groupService.removeUserFromGroup(group, event.getUser(), new TEDAsyncCallback<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                refreshGroupMembersPanel();
                            };
                        });
                    }
                }
            });

            confirmPopup.show();
        }
    };

    /**
     * Loads the group members from the database.
     */
    private void refreshGroupMembersPanel(){
        groupService.getGroupMembers(group, new TEDAsyncCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                groupMembersPanel.clear();
                boolean isOdd = false;
                for (User user:users){
                    GroupMemberRowPanel groupMemberRowPanel = new GroupMemberRowPanel(group, user, isOdd);
                    groupMembersPanel.add(groupMemberRowPanel);
                    groupMemberRowPanel.addRemovedHandler(groupMemberRemovedHandler);
                    isOdd = !isOdd;
                }
            }
        });


    }
}