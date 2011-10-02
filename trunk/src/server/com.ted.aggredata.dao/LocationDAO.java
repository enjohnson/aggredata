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

package server.dao;

import java.io.Serializable;

/**
 * Represents a location that can contain one or more gateways.
 */
public class LocationDAO implements Serializable{


    private String description;
    private String address1;
    private String address2;
    private int weatherLocationId;
    private boolean state;
    private int userId;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public int getWeatherLocationId() {
        return weatherLocationId;
    }

    public void setWeatherLocationId(int weatherLocationId) {
        this.weatherLocationId = weatherLocationId;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("LocationDAO{");
//        b.append("id:" + getId());
        b.append(", userId:" + userId);
        b.append(", description:" + description);
        b.append(", address1:" + address1);
        b.append(", address2:" + address2);
        b.append(", state:" + state);
        b.append(", weatherLocationId:" + weatherLocationId);
        b.append("}");
        return b.toString();
    }

}
