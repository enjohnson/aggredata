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

package com.ted.aggredata.server.services.impl;

import com.ted.aggredata.model.Location;
import com.ted.aggredata.model.WeatherHistory;
import com.ted.aggredata.model.WeatherLocation;
import com.ted.aggredata.server.services.WeatherService;

import java.util.Date;
import java.util.List;

public class WeatherServiceImpl implements WeatherService {
    @Override
    public WeatherLocation findWeatherLocation(String postalCode) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public WeatherLocation findWeatherLocation(Double latitude, Double longitude) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public WeatherHistory getRecentWeather(WeatherLocation location) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public WeatherHistory getRecentWeather(Location location) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<WeatherHistory> getWeather(WeatherLocation location, Date startDate, Date endDate) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<WeatherHistory> getWeather(Location location, Date startDate, Date endDate) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateWeather(WeatherLocation weatherLocation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
