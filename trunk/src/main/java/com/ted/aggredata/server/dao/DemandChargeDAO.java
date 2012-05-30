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


import com.ted.aggredata.model.DemandCharge;
import com.ted.aggredata.model.Gateway;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO for storing and retrieving demand charge data
 */
public class DemandChargeDAO extends AbstractDAO<DemandCharge> {

    public static String DELETE_DEMAND_CHARGE_DATA = "delete from  aggredata.demandcharge where gatewayId=?";
    public static String POST_DEMAND_CHARGE_DATA = "insert into aggredata.demandcharge (gatewayId, timestamp, peak, cost) values (?,?,?,?)";
    public static String SAVE_DEMAND_CHARGE_DATA = "update aggredata.demandcharge set peak=?, cost=? where gatewayId=? and timestamp=?";
    public static String FIND_BY_TIMESTAMP = "select  gatewayId, timestamp, peak, cost from aggredata.demandcharge where gatewayId= ? and timestamp = ?";


    public DemandChargeDAO() {
        super("aggredata.demandcharge");
    }

    private RowMapper<DemandCharge> rowMapper = new RowMapper<DemandCharge>() {
        public DemandCharge mapRow(ResultSet rs, int rowNum) throws SQLException {
            DemandCharge demandCharge = new DemandCharge();
            demandCharge.setGatewayId(rs.getLong("gatewayId"));
            demandCharge.setTimestamp(rs.getInt("timestamp"));
            demandCharge.setPeak(rs.getDouble("peak"));
            demandCharge.setCost(rs.getDouble("cost"));
            return demandCharge;
        }
    };

    /**
     * Returns the most recent energy data for the specified mtu
     *
     * @param gatewayId
     * @param timestamp
     * @return
     */
    public DemandCharge findByTimestamp(Long gatewayId, long timestamp) {
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
     * @param demandCharge
     */
    public void create(DemandCharge demandCharge) {
        DemandCharge oldDemandCharge = findByTimestamp(demandCharge.getGatewayId(), demandCharge.getTimestamp());
        if (oldDemandCharge == null) {
            getJdbcTemplate().update(POST_DEMAND_CHARGE_DATA, demandCharge.getGatewayId(), demandCharge.getTimestamp(), demandCharge.getPeak(), demandCharge.getCost());
        } else {
            getJdbcTemplate().update(SAVE_DEMAND_CHARGE_DATA, demandCharge.getPeak(), demandCharge.getCost(), demandCharge.getGatewayId(), demandCharge.getTimestamp());
        }
    }


    public RowMapper<DemandCharge> getRowMapper() {
        return rowMapper;
    }

    /**
     * Removes data for the given Gatreway
     *
     * @param gateway
     */
    public void deleteEnergyData(Gateway gateway) {
        getJdbcTemplate().update(DELETE_DEMAND_CHARGE_DATA, gateway.getId());
    }


    @Override
    public DemandCharge findById(Long id) {
        logger.error("DemandCharge findById not implemented");
        throw new NotImplementedException();
    }


}
