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

public class WeatherHistory  extends AggreDataModel implements Serializable {
    private Integer weatherLocationId;
    private Integer timestamp;
    private Integer temperature;
    private Integer windSpeed;
    private Integer direction;
    private String weatherConditions;
    private String iconLink;

    public Integer getWeatherLocationId() {
        return weatherLocationId;
    }

    public void setWeatherLocationId(Integer weatherLocationId) {
        this.weatherLocationId = weatherLocationId;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public Integer getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Integer windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public String getWeatherConditions() {
        return weatherConditions;
    }

    public void setWeatherConditions(String weatherConditions) {
        this.weatherConditions = weatherConditions;
    }

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }


    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("WeatherHistory{");
        b.append("id:" + getId());
        b.append(", weatherLocationId: " + weatherLocationId);
        b.append(", timestamp:" + timestamp);
        b.append(", weatherLocationId:" + weatherLocationId);
        b.append("}");
        return b.toString();
    }
}
