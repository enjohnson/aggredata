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
 * Represents a location that can contain one or more gateways.
 */
public class Location extends AggreDataModel implements Serializable {

    private String description;
    private String address1;
    private String address2;
    private String city;
    private String stateOrProvince;
    private String postal;
    private String country;
    private Long weatherLocationId;
    private boolean state;
    private long userId;


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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateOrProvince() {
        return stateOrProvince;
    }

    public void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getWeatherLocationId() {
        return weatherLocationId;
    }

    public void setWeatherLocationId(Long weatherLocationId) {
        this.weatherLocationId = weatherLocationId;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("Location{");
        b.append("id:" + getId());
        b.append(", userId:" + userId);
        b.append(", description:" + description);
        b.append(", address1:" + address1);
        b.append(", address2:" + address2);
        b.append(", city:" + city);
        b.append(", state:" + state);
        b.append(", postal:" + postal);
        b.append(", weatherLocationId:" + weatherLocationId);
        b.append("}");
        return b.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (state != location.state) return false;
        if (userId != location.userId) return false;
        if (address1 != null ? !address1.equals(location.address1) : location.address1 != null) return false;
        if (address2 != null ? !address2.equals(location.address2) : location.address2 != null) return false;
        if (city != null ? !city.equals(location.city) : location.city != null) return false;
        if (country != null ? !country.equals(location.country) : location.country != null) return false;
        if (description != null ? !description.equals(location.description) : location.description != null)
            return false;
        if (getId() != null ? !getId().equals(location.getId()) : location.getId() != null) return false;
        if (postal != null ? !postal.equals(location.postal) : location.postal != null) return false;
        if (stateOrProvince != null ? !stateOrProvince.equals(location.stateOrProvince) : location.stateOrProvince != null)
            return false;
        if (weatherLocationId != null ? !weatherLocationId.equals(location.weatherLocationId) : location.weatherLocationId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (address1 != null ? address1.hashCode() : 0);
        result = 31 * result + (address2 != null ? address2.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (stateOrProvince != null ? stateOrProvince.hashCode() : 0);
        result = 31 * result + (postal != null ? postal.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (weatherLocationId != null ? weatherLocationId.hashCode() : 0);
        result = 31 * result + (state ? 1 : 0);
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        return result;
    }
}
