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

package com.ted.aggredata.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.ted.aggredata.client.guiService.*;
import com.ted.aggredata.client.panels.AggreDataPanel;
import com.ted.aggredata.client.panels.login.LoginPanel;
import com.ted.aggredata.model.User;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AggreData2 implements EntryPoint {

    final UserSessionServiceAsync userSessionService = (UserSessionServiceAsync) GWT.create(UserSessionService.class);
    static final Logger logger = Logger.getLogger(AggreData2.class.toString());
    public void onModuleLoad() {

        /**
         * Check to see if the user already has a valid session
         */
        userSessionService.getUserFromSession(new TEDAsyncCallback<User>() {
            @Override
            public void onSuccess(User result) {
                Globals.user = result;
                if (result != null){
                    if (logger.isLoggable(Level.FINE)) logger.fine("Valid session found for user " + result.getUsername());
                    RootPanel.get("aggreDataSlot").clear();
                    RootPanel.get("aggreDataSlot").add(new AggreDataPanel());
                }  else
                {
                    logger.info("No session found. Redirecting to login page.");
                    RootPanel.get("aggreDataSlot").clear();
                    RootPanel.get("aggreDataSlot").add(new LoginPanel());
                }
            }
        });
    }
}
