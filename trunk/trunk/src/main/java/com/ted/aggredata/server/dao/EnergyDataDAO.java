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

import com.ted.aggredata.model.EnergyData;
import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.MTU;
import com.ted.aggredata.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO for accessing and storing energy data
 */
public class EnergyDataDAO extends AggreDataDAO<EnergyData> {

    public static String POST_ENERGY_DATA = "insert into aggredata.energyData (mtuId, timestamp, rate, energy) values (?,?,?,?)";
    public static String SAVE_ENERGY_DATA = "update aggredata.energyData set mtuId=?, timestamp=?, rate=?, energy=? where id = ?";
    public static String DELETE_ENERGY_DATA = "delete from aggredata.energyData where mtuId=?";


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

    public void create(EnergyData energyData) {
        getJdbcTemplate().update(POST_ENERGY_DATA, energyData.getMtuId(), energyData.getTimestamp(), energyData.getRate(), energyData.getEnergy());
    }

    @Override
    public void save(EnergyData energyData) {
        getJdbcTemplate().update(SAVE_ENERGY_DATA, energyData.getMtuId(), energyData.getTimestamp(), energyData.getRate(), energyData.getEnergy(), energyData.getId());
    }

    @Override
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
