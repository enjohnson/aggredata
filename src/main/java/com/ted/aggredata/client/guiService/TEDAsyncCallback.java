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

package com.ted.aggredata.client.guiService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.gwt.user.client.ui.RootPanel;
import com.ted.aggredata.client.Aggredata;
import com.ted.aggredata.client.panels.login.LoginPanel;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Extends AsyncCallback for generic error handling on any RPC call.
 * Specifically it handles the onFailure for common routing, logging, and redirection.
 */
public abstract class TEDAsyncCallback<T> implements AsyncCallback<T>{
    
    static Logger logger = Logger.getLogger(TEDAsyncCallback.class.toString());

    static Integer STATUS_CODE_UNAUTHORIZED = 401;

    @Override
    public void onFailure(Throwable caught) {

        GWT.log("-----"+ (caught instanceof StatusCodeException));
        if (caught instanceof StatusCodeException)
        {
            StatusCodeException statusCodeException = (StatusCodeException) caught;
            logger.log(Level.SEVERE, "Status Code Exception:" + statusCodeException.getStatusCode());
            if (statusCodeException.getStatusCode() == STATUS_CODE_UNAUTHORIZED) {
                logger.log(Level.INFO, "Redirecting user to login page");
                RootPanel.get(Aggredata.ROOT_PANEL).clear();
                RootPanel.get(Aggredata.ROOT_PANEL).add(new LoginPanel());  //Redirect the user to the login panel.
            } else
            {
                logger.log(Level.SEVERE, "Critcal Failure " + caught.getMessage(), caught);
                //TODO: Critical Dialog and reload
            }
        }  else
        {
            logger.log(Level.SEVERE, "AsyncCallback Failure " + caught.getMessage(), caught);
            //TODO: Critical Dialog and reload
        }
    }

}
