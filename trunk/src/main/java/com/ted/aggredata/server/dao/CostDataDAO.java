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

import com.ted.aggredata.model.CostData;
import com.ted.aggredata.model.Gateway;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO for storing and retrieving cost data
 */
public class CostDataDAO extends AbstractDAO<CostData> {

    public static String DELETE_COST_DATA = "delete from  aggredata.costdata where gatewayId=?";
    public static String POST_COST_DATA = "insert into aggredata.costdata (gatewayId, timestamp, meterReadDay, meterReadMonth, meterReadYear, fixedCost, minCost) values (?,?,?,?,?,?,?)";
    public static String SAVE_COST_DATA = "update aggredata.costdata set meterReadDay=?, meterReadMonth=?, meterReadYear=?, fixedCost=?, minCost=? where gatewayId=? and timestamp=?";
    public static String FIND_BY_TIMESTAMP = "select  gatewayId, timestamp, meterReadDay, meterReadMonth, meterReadYear, fixedCost, minCost from aggredata.costdata where gatewayId= ? and timestamp = ?";


    public CostDataDAO() {
        super("aggredata.costdata");
    }

    private RowMapper<CostData> rowMapper = new RowMapper<CostData>() {
        public CostData mapRow(ResultSet rs, int rowNum) throws SQLException {
            CostData costData = new CostData();
            costData.setGatewayId(rs.getLong("gatewayId"));
            costData.setTimestamp(rs.getInt("timestamp"));
            costData.setMeterReadDay(rs.getInt("meterReadDay"));
            costData.setMeterReadMonth(rs.getInt("meterReadMonth"));
            costData.setMeterReadYear(rs.getInt("meterReadYear"));
            costData.setFixedCost(rs.getDouble("fixedCost"));
            costData.setMinCost(rs.getDouble("minCost"));
            return costData;
        }
    };

    /**
     * Returns the most recent energy data for the specified mtu
     *
     * @param gatewayId
     * @param timestamp
     * @return
     */
    public CostData findByTimestamp(Long gatewayId, long timestamp) {
        try {
            return getJdbcTemplate().queryForObject(FIND_BY_TIMESTAMP, new Object[]{gatewayId, timestamp}, getRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No Results returned");
            return null;
        }
    }

    /**
     * Creates or updates cost data in the database.
     *
     * @param costData
     */
    public void create(CostData costData) {
        CostData oldCostData = findByTimestamp(costData.getGatewayId(), costData.getTimestamp());
        if (oldCostData == null) {
            getJdbcTemplate().update(POST_COST_DATA, costData.getGatewayId(), costData.getTimestamp(), costData.getMeterReadDay(), costData.getMeterReadMonth(), costData.getMeterReadYear(), costData.getFixedCost(), costData.getMinCost());
        } else {
            getJdbcTemplate().update(SAVE_COST_DATA, costData.getMeterReadDay(), costData.getMeterReadMonth(), costData.getMeterReadYear(), costData.getFixedCost(), costData.getMinCost(), costData.getGatewayId(), costData.getTimestamp());
        }
    }


    public RowMapper<CostData> getRowMapper() {
        return rowMapper;
    }

    /**
     * Removes data for the given Gatreway
     *
     * @param gateway
     */
    public void deleteEnergyData(Gateway gateway) {
        getJdbcTemplate().update(DELETE_COST_DATA, gateway.getId());
    }


    @Override
    public CostData findById(Long id) {
        logger.error("CostData findById not implemented");
        throw new NotImplementedException();
    }


}
