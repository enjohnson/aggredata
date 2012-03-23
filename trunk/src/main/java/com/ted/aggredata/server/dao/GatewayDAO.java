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

import com.ted.aggredata.model.Gateway;
import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO for accessing the Gateway Object
 */
public class GatewayDAO extends AggredataDAO<Gateway> {

    public static String CREATE_GATEWAY_QUERY = "insert into aggredata.gateway (weatherLocationId, userAccountId, gatewaySerialNumber, state, securityKey, description) values (?,?,?,?,?,?)";
    public static String SAVE_GATEWAY_QUERY = "update aggredata.gateway set weatherLocationId=?, userAccountId=?, gatewaySerialNumber=?, state=?, securityKey=?, description=? where id=?";
    public static String GET_BY_SERIAL_NUMBER_QUERY = "select id, weatherLocationId, userAccountId, gatewaySerialNumber, state, securityKey, description from aggredata.gateway where gatewaySerialNumber=?";
    public static String GET_BY_USER_ACCOUNT_QUERY = "select id, weatherLocationId, userAccountId, gatewaySerialNumber, state, securityKey, description from aggredata.gateway where userAccountId=?";
    public static String GET_BY_GROUP_QUERY = "select id, weatherLocationId, userAccountId, gatewaySerialNumber, state, securityKey, description from aggredata.gateway, aggredata.gatewaygroup where gateway.id = gatewaygroup.id and groupId=?";

    public GatewayDAO() {
        super("aggredata.gateway");
    }

    private RowMapper<Gateway> rowMapper = new RowMapper<Gateway>() {
        public Gateway mapRow(ResultSet rs, int rowNum) throws SQLException {
            Gateway gateway = new Gateway();
            gateway.setId(rs.getLong("id"));
            gateway.setWeatherLocationId(rs.getLong("weatherLocationId"));
            gateway.setUserAccountId(rs.getLong("userAccountId"));
            gateway.setGatewaySerialNumber(rs.getString("gatewaySerialNumber"));
            gateway.setState(rs.getBoolean("state"));
            gateway.setSecurityKey(rs.getString("securityKey"));
            gateway.setDescription(rs.getString("description"));
            return gateway;
        }
    };

    public void create(Gateway gateway) {
        //Check to make sure a gateway with the given serial number does not already exist in the system. No two gateways should have the same serial number.
        if (getBySerialNumber(gateway.getGatewaySerialNumber())==null)
        {
            getJdbcTemplate().update(CREATE_GATEWAY_QUERY, gateway.getWeatherLocationId(), gateway.getUserAccountId(), gateway.getGatewaySerialNumber(), gateway.getState(), gateway.getSecurityKey(), gateway.description);
        } else {
            logger.error("Gateway with serial number " + gateway.getGatewaySerialNumber() + " already exists in the database");
        }
    }

    @Override
    public void save(Gateway gateway) {
        getJdbcTemplate().update(SAVE_GATEWAY_QUERY, gateway.getWeatherLocationId(), gateway.getUserAccountId(), gateway.getSecurityKey(), gateway.getState(), gateway.getSecurityKey(), gateway.description, gateway.getId());
    }


    /**
     * Returns the MTU's for the given gateway
     *
     * @param user
     * @return
     */
    public List<Gateway> getByUserAccount(User user) {
        try {
            return getJdbcTemplate().query(GET_BY_USER_ACCOUNT_QUERY, new Object[]{user.getId()}, getRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No Results returned");
            return null;
        }
    }


    /**
     * Returns the mtu for the given serial number
     *
     * @param serialNumber
     * @return
     */
    public Gateway getBySerialNumber(String serialNumber) {
        try {
            return getJdbcTemplate().queryForObject(GET_BY_SERIAL_NUMBER_QUERY, new Object[]{serialNumber}, getRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No Results returned");
            return null;
        }
    }

    public List<Gateway> getByGroup(Group group)
    {
        try
        {
            return getJdbcTemplate().query(GET_BY_GROUP_QUERY, new Object[]{group.getId()}, getRowMapper());
        } catch (EmptyResultDataAccessException ex){
            logger.debug("no results returned");
            return null;
        }
    }

    @Override
    public RowMapper<Gateway> getRowMapper() {
        return rowMapper;
    }
}
