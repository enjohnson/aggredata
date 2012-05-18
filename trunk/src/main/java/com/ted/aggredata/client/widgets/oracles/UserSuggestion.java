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

import com.google.gwt.user.client.ui.SuggestOracle;
import com.ted.aggredata.model.User;

import java.io.Serializable;

public class UserSuggestion implements SuggestOracle.Suggestion, Serializable {
    private String display = null;
    private String replacement = null;
    final private User user;

    public UserSuggestion(User user) {
        this.user = user;


        StringBuilder displayBuilder = new StringBuilder();
        displayBuilder.append(user.getFirstName());
        displayBuilder.append(" ");
        displayBuilder.append(user.getLastName());
        displayBuilder.append(" <");
        displayBuilder.append(user.getUsername());
        displayBuilder.append(" >");

        this.replacement = display.toString();

        if (user.getCompanyName().length() > 0)
        {
            displayBuilder.append(" ");
            displayBuilder.append(user.getCompanyName());
        }

        if (user.getCity().length() > 0)
        {
            displayBuilder.append(" ");
            displayBuilder.append(user.getCity());

            if (user.getAddrState().length() > 0) displayBuilder.append(",");
        }
        if (user.getAddrState().length() > 0)
        {
            displayBuilder.append(" ");
            displayBuilder.append(user.getAddrState());
        }

        this.display = display.toString();

    }

    @Override
    public String getDisplayString() {
        return display;
    }

    @Override
    public String getReplacementString() {
        return replacement;
    }

    public User getUser() {
        return user;
    }
}
