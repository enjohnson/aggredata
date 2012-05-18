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

package com.ted.aggredata.client.widgets.oracles;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.ted.aggredata.client.guiService.GWTUserService;
import com.ted.aggredata.client.guiService.GWTUserServiceAsync;
import com.ted.aggredata.model.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/***
 * Used by the SuggestBox on the Group Share to find users in the system. It attempts to match by name or email address.
 */
public class AggreDataUserSuggestOracle extends MultiWordSuggestOracle {
    final GWTUserServiceAsync userService = (GWTUserServiceAsync) GWT.create(GWTUserService.class);
    static Logger logger = Logger.getLogger(AggreDataUserSuggestOracle.class.toString());

    @Override
    public void requestSuggestions(final Request request, final Callback callback) {
        if (logger.isLoggable(Level.FINE)) logger.fine("Request query " + request);

        if (request.getQuery().length() < 3) {
            //Require at least 3 characters before we even bother to hit the database
            List<MultiWordSuggestion> list = new ArrayList<MultiWordSuggestion>();
            Response r = new Response();
            r.setSuggestions(list);
            if (request != null && callback != null) callback.onSuggestionsReady(request, r);
        } else {

            userService.findUsers(request.getQuery(), new AsyncCallback<List<User>>() {
                public void onFailure(Throwable throwable) {
                    logger.severe("DB Error:" + throwable.getMessage());
                }

                public void onSuccess(List<User> userAccountDTOs) {
                    Iterator<User> userIterator = userAccountDTOs.iterator();
                    List<UserSuggestion> list = new ArrayList<UserSuggestion>();
                    while (userIterator.hasNext()) {
                        User user = userIterator.next();
                        UserSuggestion sug = new UserSuggestion(user);
                        list.add(sug);
                    }
                    Response r = new Response();
                    r.setSuggestions(list);
                    if (request != null && callback != null) callback.onSuggestionsReady(request, r);

                }
            });
        }

    }

}
