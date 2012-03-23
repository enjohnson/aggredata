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

package com.ted.aggredata.server.dao;


import com.ted.aggredata.model.WeatherHistory;
import com.ted.aggredata.model.WeatherLocation;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * DAO for accessing weather history
 */
public class WeatherHistoryDAO extends AggredataDAO<WeatherHistory> {


    public static final String GET_RECENT_BY_WEATHER_LOCATION = "select top 1 id, weatherLocationId, timestamp, temperature, windspeed, direction, weatherConditions, iconLink from aggredata.weatherhistory where weatherLocationId= ? order by timestamp desc";
    public static final String GET_RANGE_BY_WEATHER_LOCATION = "select id, weatherLocationId, timestamp, temperature, windspeed, direction, weatherConditions, iconLink from aggredata.weatherhistory where timestamp >= ? and timestamp < ? and weatherLocationId=? order by timestamp desc";
    public static final String CREATE_WEATHER_HISTORY = "insert into aggredata.weatherhistory (weatherLocationId, timestamp, temperature, windspeed, direction, weatherConditions, iconlink) values (?,?,?,?,?,?,?)";
    public static final String SAVE_WEATHER_HISTORY = "update aggredata.weatherhistory set weatherLocationId=?, timestamp=?, temperature=?, windspeed=?, direction=?, weatherConditions=?, iconlink=? where id=?";


    public WeatherHistoryDAO() {
        super("aggredata.weatherHistory");
    }


    private RowMapper<WeatherHistory> rowMapper = new RowMapper<WeatherHistory>() {
        public WeatherHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
            WeatherHistory weatherHistory = new WeatherHistory();
            weatherHistory.setId(rs.getLong("id"));
            weatherHistory.setWeatherLocationId(rs.getLong("weatherLocationId"));
            weatherHistory.setTimestamp(rs.getInt("timestamp"));
            weatherHistory.setTemperature(rs.getInt("temperature"));
            weatherHistory.setWindSpeed(rs.getInt("windspeed"));
            weatherHistory.setDirection(rs.getInt("direction"));
            weatherHistory.setWeatherConditions(rs.getString("weatherConditions"));
            weatherHistory.setIconLink(rs.getString("iconLink"));
            return weatherHistory;
        }
    };

    public WeatherHistory getRecentWeather(WeatherLocation location) {
        try {
            return getJdbcTemplate().queryForObject(GET_RECENT_BY_WEATHER_LOCATION, new Object[]{location.getId()}, rowMapper);
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No Results returned");
            return null;
        }
    }

    public List<WeatherHistory> getWeatherRange(WeatherLocation location, Date startDate, Date endDate) {
        try {
            return getJdbcTemplate().query(GET_RANGE_BY_WEATHER_LOCATION, new Object[]{location.getId(), startDate, endDate}, rowMapper);
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No Results returned");
            return null;
        }
    }


    @Override
    public RowMapper<WeatherHistory> getRowMapper() {
        return rowMapper;
    }

    public void create(WeatherHistory weatherHistory) {
        getJdbcTemplate().update(CREATE_WEATHER_HISTORY, weatherHistory.getWeatherLocationId(), weatherHistory.getTimestamp(), weatherHistory.getTemperature(), weatherHistory.getWindSpeed(), weatherHistory.getDirection(), weatherHistory.getWeatherConditions(), weatherHistory.getIconLink());
    }

    @Override
    public void save(WeatherHistory weatherHistory) {
        getJdbcTemplate().update(SAVE_WEATHER_HISTORY, weatherHistory.getWeatherLocationId(), weatherHistory.getTimestamp(), weatherHistory.getTemperature(), weatherHistory.getWindSpeed(), weatherHistory.getDirection(), weatherHistory.getWeatherConditions(), weatherHistory.getIconLink(), weatherHistory.getId());
    }


}
