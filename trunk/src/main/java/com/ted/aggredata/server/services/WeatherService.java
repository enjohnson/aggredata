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

package com.ted.aggredata.server.services;

import com.ted.aggredata.model.Location;
import com.ted.aggredata.model.WeatherHistory;
import com.ted.aggredata.model.WeatherLocation;

import java.util.Date;
import java.util.List;

/**
 * Public interface to the weather management service
 */
public interface WeatherService {

    /**
     * Finds the weather location based on postal code
     *
     * @param postalCode
     * @return
     */
    public WeatherLocation findWeatherLocation(String postalCode);


    /**
     * Finds the weather location based on the lat/lon
     *
     * @param latitude
     * @param longitude
     * @return
     */
    public WeatherLocation findWeatherLocation(Double latitude, Double longitude);


    /**
     * Returns the most recent weather record for the specified location
     *
     * @param location
     * @return
     */
    public WeatherHistory getRecentWeather(WeatherLocation location);

    /**
     * Returns the most recent weather record for the specified location
     *
     * @param location
     * @return
     */
    public WeatherHistory getRecentWeather(Location location);

    /**
     * Returns a weather history for the specified start and end date range
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public List<WeatherHistory> getWeather(WeatherLocation location, Date startDate, Date endDate);

    /**
     * Returns a weather history for the specified start and end date range
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public List<WeatherHistory> getWeather(Location location, Date startDate, Date endDate);


    /**
     * Gets the latest weather from the weather service. If the retrieved weather is already the same as the most recent
     * the weather is dropped.
     *
     * @param weatherLocation
     */
    public void updateWeather(WeatherLocation weatherLocation);

}
