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


import com.ted.aggredata.model.WeatherLocation;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO for accessing a weather location
 */
public class WeatherLocationDAO extends AbstractDAO<WeatherLocation> {

    public static final String GET_BY_POSTAL = "select id, postal, latitude, longitude from aggredata.weatherlocation where postal= ?";
    public static final String GET_BY_ID = "select id, postal, latitude, longitude from aggredata.weatherlocation where id= ?";
    public static final String SAVE_WEATHER_QUERY = "update aggredata.weatherlocation set postal=?, latitude=?, longitude=? where id = ?";
    public static final String CREATE_WEATHER_QUERY = "insert into aggredata.weatherlocation (postal, latitude, longitude) values (?,?,?)";
    public static final String COUNT_WEATHER_QUERY = "select count(*) from aggredata.weatherlocation where id = ?";


    public WeatherLocationDAO() {
        super("aggredata.weatherLocation");
    }


    private RowMapper<WeatherLocation> rowMapper = new RowMapper<WeatherLocation>() {
        public WeatherLocation mapRow(ResultSet rs, int rowNum) throws SQLException {
            WeatherLocation weatherLocation = new WeatherLocation();
            weatherLocation.setId(rs.getLong("id"));
            weatherLocation.setPostal(rs.getString("postal"));
            weatherLocation.setLatitude(rs.getDouble("latitude"));
            weatherLocation.setLongitude(rs.getDouble("longitude"));
            return weatherLocation;
        }
    };

    public WeatherLocation findById(Long id) {
        try {
            return getJdbcTemplate().queryForObject(GET_BY_ID, new Object[]{id}, rowMapper);
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No Results returned");
            return null;
        }
    }


    public WeatherLocation getByPostal(String postal) {
        try {
            return getJdbcTemplate().queryForObject(GET_BY_POSTAL, new Object[]{postal}, rowMapper);
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No Results returned");
            return null;
        }
    }

    @Override
    public RowMapper<WeatherLocation> getRowMapper() {
        return rowMapper;
    }

    public void create(WeatherLocation weatherLocation) {
        if (getJdbcTemplate().queryForInt(COUNT_WEATHER_QUERY, weatherLocation.getId()) == 0) {
            getJdbcTemplate().update(CREATE_WEATHER_QUERY, weatherLocation.getPostal(), weatherLocation.getLatitude(), weatherLocation.getLongitude());
        }
    }

    public void save(WeatherLocation weatherLocation) {

        getJdbcTemplate().update(SAVE_WEATHER_QUERY, weatherLocation.getPostal(), weatherLocation.getLatitude(), weatherLocation.getLongitude());
    }

}
