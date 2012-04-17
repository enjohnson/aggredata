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
public class GatewayDAO extends AbstractDAO<Gateway> {


    public static String DELETE_GATEWAY_QUERY = "delete from aggredata.gateway where id=?";
    public static String CREATE_GATEWAY_QUERY = "insert into aggredata.gateway (id, weatherLocationId, userAccountId,  state, securityKey, description,custom1,custom2,custom3,custom4,custom5) values (?,?,?,?,?,?,?,?,?,?,?)";
    public static String SAVE_GATEWAY_QUERY = "update aggredata.gateway set weatherLocationId=?, userAccountId=?,  state=?, securityKey=?, description=?,custom1=?,custom2=?,custom3=?,custom4=?,custom5=? where id=?";
    public static String GET_BY_USER_ACCOUNT_QUERY = "select id, weatherLocationId, userAccountId,  state, securityKey, description,custom1,custom2,custom3,custom4,custom5 from aggredata.gateway where userAccountId=?";
    public static String COUNT_BY_USER_ACCOUNT_QUERY = "select count(*) from aggredata.gateway where userAccountId=?";
    public static String GET_BY_GROUP_QUERY = "select g.id, weatherLocationId, userAccountId,  state, securityKey, description, custom1,custom2,custom3,custom4,custom5 from aggredata.gateway g, aggredata.gatewaygroup gg where g.id = gg.gatewayId and gg.groupId=?";

    public static String ADD_GATEWAY_TO_GROUP_QUERY = "insert into aggredata.gatewaygroup (groupId, gatewayId) values (?,?)";
    public static String REMOVE_GATEWAY_FROM_GROUP_QUERY = "delete from aggredata.gatewaygroup where groupId=? and gatewayId=?";
    public static String REMOVE_GATEWAY_FROM_GATEWAYGROUP_QUERY = "delete from aggredata.gatewaygroup where gatewayId=?";
    public static String DELETE_MTU_FROM_GATEWAY_QUERY = "delete from aggredata.mtu where gatewayId=?";
    public static String DELETE_GATEWAY_ENERGY_DATA_QUERY = "delete from aggredata.energydata where mtuId in (select id from aggredata.mtu where gatewayId=?)";


    public GatewayDAO() {
        super("aggredata.gateway");
    }

    private RowMapper<Gateway> rowMapper = new RowMapper<Gateway>() {
        public Gateway mapRow(ResultSet rs, int rowNum) throws SQLException {
            Gateway gateway = new Gateway();
            gateway.setId(rs.getLong("id"));
            gateway.setWeatherLocationId(rs.getLong("weatherLocationId"));
            gateway.setUserAccountId(rs.getLong("userAccountId"));
            gateway.setState(rs.getBoolean("state"));
            gateway.setSecurityKey(rs.getString("securityKey"));
            gateway.setDescription(rs.getString("description"));
            gateway.setCustom1(rs.getString("custom1"));
            gateway.setCustom2(rs.getString("custom2"));
            gateway.setCustom3(rs.getString("custom3"));
            gateway.setCustom4(rs.getString("custom4"));
            gateway.setCustom5(rs.getString("custom5"));

            return gateway;
        }
    };

    public Gateway create(Gateway gateway) throws GatewayExistsException {
        //Check to make sure a gateway with the given serial number does not already exist in the system. No two gateways should have the same serial number.
        if (findById(gateway.getId()) == null) {
            getJdbcTemplate().update(CREATE_GATEWAY_QUERY,
                    gateway.getId(),
                    gateway.getWeatherLocationId(),
                    gateway.getUserAccountId(),
                    gateway.getState(),
                    gateway.getSecurityKey(),
                    gateway.description,
                    gateway.getCustom1(),
                    gateway.getCustom2(),
                    gateway.getCustom3(),
                    gateway.getCustom4(),
                    gateway.getCustom5()
            );
            return findById(gateway.getId());
        } else {
            logger.error("Gateway with serial number " + Long.toHexString(gateway.getId()) + " already exists in the database:" + gateway);
            throw new GatewayExistsException(Long.toHexString(gateway.getId()));
        }
    }


    public void save(Gateway gateway) {
        getJdbcTemplate().update(SAVE_GATEWAY_QUERY,
                gateway.getWeatherLocationId(),
                gateway.getUserAccountId(),
                gateway.getState(),
                gateway.getSecurityKey(),
                gateway.description,
                gateway.getCustom1(),
                gateway.getCustom2(),
                gateway.getCustom3(),
                gateway.getCustom4(),
                gateway.getCustom5(),
                gateway.getId());
    }

    public void delete(Gateway gateway) {

        if (logger.isDebugEnabled()) logger.debug("removing energy data for gateway  " + gateway);
        getJdbcTemplate().update(DELETE_GATEWAY_ENERGY_DATA_QUERY, gateway.getId());
        if (logger.isDebugEnabled()) logger.debug("removing mtu's for gateway  " + gateway);
        getJdbcTemplate().update(DELETE_MTU_FROM_GATEWAY_QUERY, gateway.getId());
        if (logger.isDebugEnabled()) logger.debug("removing " + gateway + " from gatewaygroups");
        getJdbcTemplate().update(REMOVE_GATEWAY_FROM_GATEWAYGROUP_QUERY, gateway.getId());
        if (logger.isDebugEnabled()) logger.debug("removing " + gateway + " from gateway table");
        getJdbcTemplate().update(DELETE_GATEWAY_QUERY, gateway.getId());
    }

    /**
     * Returns the MTU's for the given gateway
     *
     * @param user
     * @return
     */
    public List<Gateway> findByUserAccount(User user) {
        try {
            return getJdbcTemplate().query(GET_BY_USER_ACCOUNT_QUERY, new Object[]{user.getId()}, getRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No Results returned");
            return null;
        }
    }


    public Integer countByUserAccount(User user) {
        return getJdbcTemplate().queryForInt(COUNT_BY_USER_ACCOUNT_QUERY, user.getId());
    }


    public List<Gateway> findByGroup(Group group) {
        try {
            return getJdbcTemplate().query(GET_BY_GROUP_QUERY, new Object[]{group.getId()}, getRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("no results returned");
            return null;
        }
    }


    public void addGatewayToGroup(Gateway gateway, Group group) {
        getJdbcTemplate().update(ADD_GATEWAY_TO_GROUP_QUERY, group.getId(), gateway.getId());
    }

    public void removeGatewayFromGroup(Gateway gateway, Group group) {
        getJdbcTemplate().update(REMOVE_GATEWAY_FROM_GROUP_QUERY, group.getId(), gateway.getId());
    }


    @Override
    public RowMapper<Gateway> getRowMapper() {
        return rowMapper;
    }
}
