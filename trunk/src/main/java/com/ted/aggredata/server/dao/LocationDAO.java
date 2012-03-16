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

import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.Location;
import com.ted.aggredata.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO for accessing the Location object
 */
public class LocationDAO extends AggreDataDAO<Location> {


    public static final String COUNT_LOCATION_QUERY = "select count(*) from aggredata.location where userId=? and description=?";
    public static final String FIND_LOCATION_QUERY = "select * from aggredata.location where userId=? and description=?";
    public static final String INSERT_QUERY = "insert into aggredata.location (description, address1, address2, city, stateOrProvince, postal, country, weatherLocationId, state, userId) values (?,?,?,?,?,?,?,?,?,?)";
    public static final String SAVE_QUERY = "update aggredata.location set description=?, address1=?, address2=?, city=?, stateOrProvince=?, postal=?, country=?, weatherLocationId=?, state=?, userId=? where id = ?";
    public static final String COUNT_GROUP_LOCATION_QUERY = "select count(*) from aggredata.grouplocation where locationId=? and groupId=?";
    public static final String INSERT_GROUP_LOCATION_QUERY = "insert into aggredata.grouplocation (locationId, groupId) values (?,?)";
    public static final String DELETE_GROUP_LOCATION_QUERY = "delete from aggredata.grouplocation where locationId=? and groupId=?";
    public static final String DELETE_GROUP_QUERY = "delete from aggredata.grouplocation where groupId=?";
    public static final String DELETE_LOCATION_FROM_GROUP_QUERY = "delete from aggredata.grouplocation where locationId=?";
    public static final String GET_BY_USER_QUERY = "select * from  aggredata.location where userId=?";
    public static final String GET_BY_GROUP_QUERY = "select l.* from  aggredata.location l, aggredata.grouplocation gl where l.id=gl.locationid and gl.groupId=?";


    public LocationDAO() {
        super("aggredata.location");
    }


    private RowMapper<Location> rowMapper = new RowMapper<Location>() {
        public Location mapRow(ResultSet rs, int rowNum) throws SQLException {
            Location location = new Location();
            location.setId(rs.getLong("id"));
            location.setDescription(rs.getString("description"));
            location.setAddress1(rs.getString("address1"));
            location.setAddress2(rs.getString("address2"));
            location.setCity(rs.getString("city"));
            location.setStateOrProvince(rs.getString("stateOrProvince"));
            location.setCountry(rs.getString("country"));
            location.setWeatherLocationId(rs.getLong("weatherLocationId"));
            location.setState(rs.getBoolean("state"));
            location.setUserId(rs.getLong("userId"));
            return location;
        }
    };


    @Override
    public RowMapper<Location> getRowMapper() {
        return rowMapper;
    }

    @Override
    public void create(Location location) {
        if (getJdbcTemplate().queryForInt(COUNT_LOCATION_QUERY, location.getUserId(), location.getDescription()) == 0) {
            logger.debug("Inserting new " + location);
            getJdbcTemplate().update(INSERT_QUERY, location.getDescription(), location.getAddress1(), location.getAddress2(), location.getCity(), location.getStateOrProvince(), location.getPostal(), location.getCountry(), location.getWeatherLocationId(), location.isState(), location.getUserId());
        }
    }

    @Override
    public void save(Location location) {
        logger.debug("saving " + location);
        getJdbcTemplate().update(SAVE_QUERY, location.getDescription(), location.getAddress1(), location.getAddress2(), location.getCity(), location.getStateOrProvince(), location.getPostal(), location.getCountry(), location.getWeatherLocationId(), location.isState(), location.getUserId(), location.getId());
    }

    public void addLocationToGroup(Location location, Group group) {
        if (getJdbcTemplate().queryForInt(COUNT_GROUP_LOCATION_QUERY, location.getId(), group.getId()) == 0) {
            logger.debug("Adding " + location + " to " + group);
            getJdbcTemplate().update(INSERT_GROUP_LOCATION_QUERY, location.getId(), group.getId());
        }
    }

    public void removeLocationFromGroup(Location location, Group group) {
        getJdbcTemplate().update(DELETE_GROUP_LOCATION_QUERY, location.getId(), group.getId());
    }


    public Location getLocation(User user, String description) {
        return getJdbcTemplate().queryForObject(FIND_LOCATION_QUERY, new Object[]{user.getId(), description}, getRowMapper());
    }

    public List<Location> getByUser(User user) {
        try {
            return getJdbcTemplate().query(GET_BY_USER_QUERY, new Object[]{user.getId()}, getRowMapper());

        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No results returned");
            return null;
        }
    }


    public List<Location> getByGroup(Group group) {
        try {
            return getJdbcTemplate().query(GET_BY_GROUP_QUERY, new Object[]{group.getId()}, getRowMapper());

        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No results returned");
            return null;
        }

    }

    public void removeGroupLocations(Group group) {
        getJdbcTemplate().update(DELETE_GROUP_QUERY, group.getId());
    }

    public void removeGroupLocations(Location location) {
        getJdbcTemplate().update(DELETE_LOCATION_FROM_GROUP_QUERY, location.getId());
    }
}
