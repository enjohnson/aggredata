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

import com.ted.aggredata.model.EnergyData;
import com.ted.aggredata.model.Gateway;
import com.ted.aggredata.model.MTU;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO for accessing and storing energy data
 */
public class EnergyDataDAO extends AbstractDAO<EnergyData> {

    public static String DELETE_ENERGY_DATA = "delete from  aggredata.energydata where gatewayId= ? and mtuId=?";
    public static String POST_ENERGY_DATA = "insert into aggredata.energydata (gatewayId, mtuId, timestamp, rate, energy, minuteCost, energyDifference) values (?,?,?,?,?,?,?)";
    public static String SAVE_ENERGY_DATA = "update aggredata.energydata set rate=?, energy=?, minuteCost=?, energyDifference=? where gatewayId=?, mtuId=?, timestamp=?";

    public static String FIND_BY_DATE_RANGE = "select  gatewayId, mtuId, timestamp, rate, energy , minuteCost, energyDifference from aggredata.energydata where mtuId=? and gatewayId=? and timestamp>=? and timestamp < ?";
    public static String FIND_LAST_BY_MTU = "select  gatewayId, mtuId, timestamp, rate, energy, minuteCost, energyDifference from aggredata.energydata where gatewayId= ? and mtuId=? and timestamp < ? order by timestamp desc limit 1";
    public static String FIND_BY_TIMESTAMP = "select  gatewayId, mtuId, timestamp, rate, energy, minuteCost, energyDifference from aggredata.energydata where gatewayId= ? and mtuId=? and timestamp = ?";



    public EnergyDataDAO() {
        super("aggredata.energyData");
    }

    private RowMapper<EnergyData> rowMapper = new RowMapper<EnergyData>() {
        public EnergyData mapRow(ResultSet rs, int rowNum) throws SQLException {
            EnergyData energyData = new EnergyData();
            energyData.setGatewayId(rs.getLong("gatewayId"));
            energyData.setMtuId(rs.getLong("mtuId"));
            energyData.setTimestamp(rs.getInt("timestamp"));
            energyData.setRate(rs.getDouble("rate"));
            energyData.setEnergy(rs.getDouble("energy"));
            energyData.setMinuteCost(rs.getDouble("minuteCost"));
            energyData.setEnergyDifference(rs.getDouble("energyDifference"));
            return energyData;
        }
    };

    /**
     * Returns a list of energy data based on the time range.
     *
     * @param gateway
     * @param mtu
     * @param timestampStart
     * @param timestampEnd
     * @return
     */
    public List<EnergyData> findByDateRange(Gateway gateway, MTU mtu, long timestampStart, long timestampEnd) {
        try {
            return getJdbcTemplate().query(FIND_BY_DATE_RANGE, new Object[]{gateway.getId(), mtu.getId(), timestampStart, timestampEnd}, getRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No Results returned");
            return null;
        }
    }

    /**
     * Returns the most recent energy data for the specified mtu
     *
     * @param mtu
     * @param timestamp
     * @return
     */
    public EnergyData findByLastPost(Gateway gateway, MTU mtu, long timestamp) {
        try {
            return getJdbcTemplate().queryForObject(FIND_LAST_BY_MTU, new Object[]{gateway.getId(), mtu.getId(), timestamp}, getRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No Results returned");
            return null;
        }
    }


    /**
     * Returns the most recent energy data for the specified mtu
     *
     * @param gatewayId
     * @param mtuId
     * @param timestamp
     * @return
     */
    public EnergyData findByTimestamp(Long gatewayId, Long mtuId, long timestamp) {
        try {
            return getJdbcTemplate().queryForObject(FIND_BY_TIMESTAMP, new Object[]{gatewayId, mtuId, timestamp}, getRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No Results returned");
            return null;
        }
    }


    /**
     * Creates or updates energy data in the database.
     *
     * @param energyData
     */
    public void create(EnergyData energyData) {
        EnergyData oldEnergyData = findByTimestamp(energyData.getGatewayId(), energyData.getMtuId(), energyData.getTimestamp());
        if (oldEnergyData == null) {
            getJdbcTemplate().update(POST_ENERGY_DATA, energyData.getGatewayId(), energyData.getMtuId(), energyData.getTimestamp(), energyData.getRate(), energyData.getEnergy(), energyData.getMinuteCost(), energyData.getEnergyDifference());
        } else {
            getJdbcTemplate().update(SAVE_ENERGY_DATA, energyData.getRate(), energyData.getEnergy(), energyData.getMinuteCost(), energyData.getGatewayId(), energyData.getMtuId(), energyData.getTimestamp(), energyData.getEnergyDifference());
        }
    }


    public RowMapper<EnergyData> getRowMapper() {
        return rowMapper;
    }

    /**
     * Removes data for the given MTU
     *
     * @param gateway
     * @param mtu
     */
    public void deleteEnergyData(Gateway gateway, MTU mtu) {
        getJdbcTemplate().update(DELETE_ENERGY_DATA, gateway.getId(), mtu.getId());
    }


    @Override
    public EnergyData findById(Long id) {
        logger.error("EnergyData findById not implemented");
        throw new NotImplementedException();
    }


}
