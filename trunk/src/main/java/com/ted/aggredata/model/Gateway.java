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
public class Gateway extends AggredataModel implements Serializable {

    public Long weatherLocationId;
    public Long userAccountId;
    public boolean state;
    public String securityKey;
    public String description;
    public String custom1;
    public String custom2;
    public String custom3;
    public String custom4;
    public String custom5;


    /**
     * the unique location id of the weather location associated with this gateway
     *
     * @return
     */
    public Long getWeatherLocationId() {
        return weatherLocationId;
    }

    public void setWeatherLocationId(Long weatherLocationId) {
        this.weatherLocationId = weatherLocationId;
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

    public String getCustom1() {
        return custom1;
    }

    public void setCustom1(String custom1) {
        this.custom1 = custom1;
    }

    public String getCustom2() {
        return custom2;
    }

    public void setCustom2(String custom2) {
        this.custom2 = custom2;
    }

    public String getCustom3() {
        return custom3;
    }

    public void setCustom3(String custom3) {
        this.custom3 = custom3;
    }

    public String getCustom4() {
        return custom4;
    }

    public void setCustom4(String custom4) {
        this.custom4 = custom4;
    }

    public String getCustom5() {
        return custom5;
    }

    public void setCustom5(String custom5) {
        this.custom5 = custom5;
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

    @Override
    public int hashCode() {
        int result = weatherLocationId != null ? weatherLocationId.hashCode() : 0;
        result = 31 * result + (userAccountId != null ? userAccountId.hashCode() : 0);
        result = 31 * result + (state ? 1 : 0);
        result = 31 * result + (securityKey != null ? securityKey.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (custom1 != null ? custom1.hashCode() : 0);
        result = 31 * result + (custom2 != null ? custom2.hashCode() : 0);
        result = 31 * result + (custom3 != null ? custom3.hashCode() : 0);
        result = 31 * result + (custom4 != null ? custom4.hashCode() : 0);
        result = 31 * result + (custom5 != null ? custom5.hashCode() : 0);
        return result;
    }
}
