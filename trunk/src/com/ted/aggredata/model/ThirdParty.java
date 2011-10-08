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
 * Represents a 3rd-party who is allowed access to AggreData
 */
public class ThirdParty extends AggreDataModel implements Serializable {

    private boolean state;
    private String thirdPartyAccessKey;
    private String description;

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getThirdPartyAccessKey() {
        return thirdPartyAccessKey;
    }

    public void setThirdPartyAccessKey(String thirdPartyAccessKey) {
        this.thirdPartyAccessKey = thirdPartyAccessKey;
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
        b.append("ThirdParty{");
        b.append("id:" + getId());
        b.append(", description:" + description);
        b.append(", thirdPartyAccessKey:" + thirdPartyAccessKey);
        b.append(", state:" + state);
        b.append("}");
        return b.toString();
    }
}
