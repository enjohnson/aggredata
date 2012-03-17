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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.ted.aggredata.client.guiService.AggreDataUserService;
import com.ted.aggredata.client.guiService.AggreDataUserServiceAsync;
import com.ted.aggredata.client.panels.MainPanel;

public class AggreData implements EntryPoint {

    final AggreDataUserServiceAsync accountService = (AggreDataUserServiceAsync) GWT.create(AggreDataUserService.class);

    public void onModuleLoad() {

        accountService.getTestString(new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Initialization Failed");
                GWT.log(caught.getMessage(), caught);
            }

            @Override
            public void onSuccess(String result) {
                RootPanel.get("aggreDataSlot").add(new MainPanel());
            }
        });
    }
}
