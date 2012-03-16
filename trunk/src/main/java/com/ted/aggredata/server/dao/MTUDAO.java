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

package com.ted.aggredata.server.dao;

import com.ted.aggredata.model.Gateway;
import com.ted.aggredata.model.MTU;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO for accessing the MTU Object
 */
public class MTUDAO extends AggreDataDAO<MTU> {

    public static String CREATE_MTU_QUERY = "insert into aggredata.mtu (gatewayId, mtuSerialNumber, type, description) values (?,?,?,?)";
    public static String SAVE_MTU_QUERY = "update aggredata.mtu set gatewayId=?, mtuSerialNumber=?, type=?, description=? where id=?";
    public static String GET_BY_SERIAL_NUMBER_QUERY = "select id, gatewayId, mtuSerialNumber, type, description from aggredata.mtu where mtuSerialNumber=?";
    public static String GET_BY_GATEWAY_QUERY = "select id, gatewayId, mtuSerialNumber, type, description from aggredata.mtu where gatewayId=?";

    public MTUDAO() {
        super("aggredata.mtu");
    }

    private RowMapper<MTU> rowMapper = new RowMapper<MTU>() {
        public MTU mapRow(ResultSet rs, int rowNum) throws SQLException {
            MTU mtu = new MTU();
            mtu.setId(rs.getLong("id"));
            mtu.setGatewayId(rs.getLong("gatewayId"));
            mtu.setMtuSerialNumber(rs.getString("mtuSerialNumber"));
            mtu.setType(MTU.MTUType.values()[rs.getInt("type")]);
            mtu.setDescription(rs.getString("description"));
            return mtu;
        }
    };

    public void create(MTU mtu) {
        getJdbcTemplate().update(CREATE_MTU_QUERY, mtu.getGatewayId(), mtu.getMtuSerialNumber(), mtu.getType(), mtu.getDescription());
    }

    @Override
    public void save(MTU mtu) {
        getJdbcTemplate().update(SAVE_MTU_QUERY, mtu.getGatewayId(), mtu.getMtuSerialNumber(), mtu.getType(), mtu.getDescription(), mtu.getId());
    }


    /**
     * Returns the MTU's for the given gateway
     *
     * @param gateway
     * @return
     */
    public List<MTU> getByGateway(Gateway gateway) {
        try {
            return getJdbcTemplate().query(GET_BY_GATEWAY_QUERY, new Object[]{gateway.getId()}, getRowMapper());
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
    public MTU getBySerialNumber(String serialNumber) {
        try {
            return getJdbcTemplate().queryForObject(GET_BY_SERIAL_NUMBER_QUERY, new Object[]{serialNumber}, getRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No Results returned");
            return null;
        }
    }

    @Override
    public RowMapper<MTU> getRowMapper() {
        return rowMapper;
    }
}
