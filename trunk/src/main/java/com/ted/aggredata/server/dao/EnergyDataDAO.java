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
import com.ted.aggredata.model.MTU;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO for accessing and storing energy data
 */
public class EnergyDataDAO extends AbstractDAO<EnergyData> {

    public static String POST_ENERGY_DATA = "insert into aggredata.energydata (mtuId, timestamp, rate, energy) values (?,?,?,?)";
    public static String DELETE_ENERGY_DATA = "delete from aggredata.energydata where mtuId=?";
    public static String FIND_BY_MTU = "select id, mtuId, timestamp, rate, energy from aggredata.energydata where mtuId=? and timestamp>=? and timestamp < ?";

    public EnergyDataDAO() {
        super("aggredata.energyData");
    }

    private RowMapper<EnergyData> rowMapper = new RowMapper<EnergyData>() {
        public EnergyData mapRow(ResultSet rs, int rowNum) throws SQLException {
            EnergyData energyData = new EnergyData();
            energyData.setId(rs.getLong("id"));
            energyData.setMtuId(rs.getLong("mtuId"));
            energyData.setTimestamp(rs.getInt("timestamp"));
            energyData.setRate(rs.getDouble("rate"));
            energyData.setEnergy(rs.getDouble("energy"));
            return energyData;
        }
    };

    /**
     * Returns a list of energy data based on the time range.
     *
     * @param mtu
     * @param timestampStart
     * @param timestampEnd
     * @return
     */
    public List<EnergyData> findByMTU(MTU mtu, long timestampStart, long timestampEnd) {
        try {
            return getJdbcTemplate().query(FIND_BY_MTU, new Object[]{mtu.getId(), timestampStart, timestampEnd}, getRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No Results returned");
            return null;
        }
    }

    public void create(EnergyData energyData) {
        getJdbcTemplate().update(POST_ENERGY_DATA, energyData.getMtuId(), energyData.getTimestamp(), energyData.getRate(), energyData.getEnergy());
    }



    public RowMapper<EnergyData> getRowMapper() {
        return rowMapper;
    }

    /**
     * Removes data for the given MTU
     *
     * @param mtu
     */
    public void removeEnergyData(MTU mtu) {
        getJdbcTemplate().update(DELETE_ENERGY_DATA, mtu.getId());
    }


}
