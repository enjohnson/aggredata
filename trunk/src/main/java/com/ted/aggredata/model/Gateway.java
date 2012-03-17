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

package com.ted.aggredata.model;

import java.io.Serializable;

/**
 * Represents a single gateway enrolled in the system
 */
public class Gateway extends AggreDataModel implements Serializable {

    public Long weatherLocationId;
    public Long userAccountId;
    public String gatewaySerialNumber;
    public boolean state;
    public String securityKey;
    public String description;

    /**
     * the unique location id of the weather location associated with this gateway
     *
     * @return
     */
    public Long getWeatherLocationId() {
        return weatherLocationId;
    }

    public void setWeatherLocationId(Long locationId) {
        this.weatherLocationId = locationId;
    }

    /**
     * The unique user account id of the user who enrolled this gateway
     *
     * @return
     */
    public Long getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(Long userAccountId) {
        this.userAccountId = userAccountId;
    }

    /**
     * unique six-character serial number of the gateway
     *
     * @return
     */
    public String getGatewaySerialNumber() {
        return gatewaySerialNumber;
    }

    public void setGatewaySerialNumber(String gatewaySerialNumber) {
        this.gatewaySerialNumber = gatewaySerialNumber;
    }

    /**
     * whether or not this gateway is currently enabled. gateways need to be enabled to receive post com.ted.aggredata.dao
     * and to have their com.ted.aggredata.dao accessed by the api.
     *
     * @return
     */
    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    /**
     * security key used to authenticate valid post com.ted.aggredata.dao
     *
     * @return
     */
    public String getSecurityKey() {
        return securityKey;
    }

    public void setSecurityKey(String securityKey) {
        this.securityKey = securityKey;
    }

    /**
     * user supplied description of the gateway
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("Gateway{");
        b.append("id:" + getId());
        b.append(", weatherLocationId:" + weatherLocationId);
        b.append(", userAccountId:" + userAccountId);
        b.append(", state:" + state);
        b.append(", securityKey:" + securityKey);
        b.append(", description:" + description);
        b.append("}");
        return b.toString();
    }
}
