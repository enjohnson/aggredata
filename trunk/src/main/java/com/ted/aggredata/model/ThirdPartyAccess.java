/*
 * Copyright (c) 2011. The Energy Detective. All Rights Reserved
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

package com.ted.aggredata.model;

import java.io.Serializable;

/**
 * Represents a 3rd-party access permission to a specific groups's com.ted.aggredata.dao
 */
public class ThirdPartyAccess extends AggreDataModel implements Serializable {

    private Integer userId;
    private boolean state;
    private String thirdPartyApplicationKey;
    private String description;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getThirdPartyApplicationKey() {
        return thirdPartyApplicationKey;
    }

    public void setThirdPartyApplicationKey(String thirdPartyApplicationKey) {
        this.thirdPartyApplicationKey = thirdPartyApplicationKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("ThirdPartyAccess{");
        b.append("id:" + getId());
        b.append(", userId:" + userId);
        b.append(", description:" + description);
        b.append(", thirdPartyApplicationKey:" + thirdPartyApplicationKey);
        b.append(", state:" + state);
        b.append("}");
        return b.toString();
    }
}
