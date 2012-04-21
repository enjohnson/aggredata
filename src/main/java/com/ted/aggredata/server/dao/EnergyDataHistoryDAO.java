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

import com.ted.aggredata.model.*;
import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * DAO for accessing and storing energy data  History
 */
public class EnergyDataHistoryDAO extends AbstractDAO<EnergyDataHistory> {

    static Logger logger = LoggerFactory.getLogger(EnergyDataHistoryDAO.class);

    public static String MONTHLY_SUMMARY = "select gatewayId, mtuId, sum(minuteCost) as cost, sum(energyDifference) as energy, " +
            " MONTH(CONVERT_TZ(FROM_UNIXTIME(timestamp),?, ?)) as theMonth, " +
            " YEAR(CONVERT_TZ(FROM_UNIXTIME(timestamp),?, ?)) as theYear, " +
            " 1 as theDay, 0 as theHour" +
            " from energydata " +
            " where gatewayId=? and mtuId=? and timestamp>=? and timestamp<?" +
            " group by gatewayId, mtuId, theMonth, theYear, theDay, theHour";

    public static String DAILY_SUMMARY = "select gatewayId, mtuId, sum(minuteCost) as cost, sum(energyDifference) as energy, " +
            " MONTH(CONVERT_TZ(FROM_UNIXTIME(timestamp),?, ?)) as theMonth, " +
            " YEAR(CONVERT_TZ(FROM_UNIXTIME(timestamp),?, ?)) as theYear, " +
            " DAY(CONVERT_TZ(FROM_UNIXTIME(timestamp),?, ?)) as theDay, " +
            " 0 as theHour" +
            " from energydata " +
            " where gatewayId=? and mtuId=? and timestamp>=? and timestamp<?" +
            " group by gatewayId, mtuId, theMonth, theYear, theDay, theHour";

    public static String HOURLY_SUMMARY = "select gatewayId, mtuId, sum(minuteCost) as cost, sum(energyDifference) as energy, " +
            " MONTH(CONVERT_TZ(FROM_UNIXTIME(timestamp),?, ?)) as theMonth, " +
            " YEAR(CONVERT_TZ(FROM_UNIXTIME(timestamp),?, ?)) as theYear, " +
            " DAY(CONVERT_TZ(FROM_UNIXTIME(timestamp),?, ?)) as theDay, " +
            " HOUR(CONVERT_TZ(FROM_UNIXTIME(timestamp),?, ?)) as theHour" +
            " from energydata " +
            " where gatewayId=? and mtuId=? and timestamp>=? and timestamp<?" +
            " group by gatewayId, mtuId, theMonth, theYear, theDay, theHour" ;


    public EnergyDataHistoryDAO() {
        super("aggredata.energyDataHistory");
    }

    private RowMapper<EnergyDataHistory> rowMapper = new RowMapper<EnergyDataHistory>() {
        public EnergyDataHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
            EnergyDataHistory energyDataHistory = new EnergyDataHistory();
            energyDataHistory.setGatewayId(rs.getLong("gatewayId"));
            energyDataHistory.setMtuId(rs.getLong("mtuId"));


            EnergyDataHistoryDate energyDataHistoryDate = new EnergyDataHistoryDate(rs.getInt("theYear"),rs.getInt("theMonth"),rs.getInt("theDay"),rs.getInt("theHour"));
            energyDataHistory.setHistoryDate(energyDataHistoryDate);

            energyDataHistory.setCost(rs.getDouble("cost"));
            energyDataHistory.setEnergy(rs.getDouble("energy"));
            return energyDataHistory;
        }
    };


    @Override
    public RowMapper<EnergyDataHistory> getRowMapper() {
        return rowMapper;
    }

    /**
     * Returns a list of monthly energy history for the given timestamp range
     */
    public List<EnergyDataHistory> findMonthHistory(Gateway gateway, MTU mtu, long timestampStart, long timestampEnd, String serverTimeZone, String clientTimeZone) {
        try {
            return getJdbcTemplate().query(MONTHLY_SUMMARY, new Object[]{serverTimeZone, clientTimeZone, //Month
                    serverTimeZone, clientTimeZone, //Year
                    gateway.getId(),
                    mtu.getId(),
                    timestampStart,
                    timestampEnd}, getRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No Results returned");
            return null;
        }
    }


    /**
     * Returns a list of daily energy history for the given timestamp range
     */
    public List<EnergyDataHistory> findDailyHistory(Gateway gateway, MTU mtu, long timestampStart, long timestampEnd, String serverTimeZone, String clientTimeZone) {
        try {
            return getJdbcTemplate().query(DAILY_SUMMARY, new Object[]{
                    serverTimeZone, clientTimeZone, //Month
                    serverTimeZone, clientTimeZone, //Year
                    serverTimeZone, clientTimeZone, //Day
                    gateway.getId(),
                    mtu.getId(),
                    timestampStart,
                    timestampEnd}, getRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No Results returned");
            return null;
        }
    }


    /**
     * Returns a list of hourly energy history for the given timestamp range
     */
    public List<EnergyDataHistory> findHourlyHistory(Gateway gateway, MTU mtu, long timestampStart, long timestampEnd, String serverTimeZone, String clientTimeZone) {
        try {
            return getJdbcTemplate().query(HOURLY_SUMMARY, new Object[]{
                    serverTimeZone, clientTimeZone, //Month
                    serverTimeZone, clientTimeZone, //Year
                    serverTimeZone, clientTimeZone, //Day
                    serverTimeZone, clientTimeZone, //Hour
                    gateway.getId(),
                    mtu.getId(),
                    timestampStart,
                    timestampEnd}, getRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No Results returned");
            return null;
        }
    }

}
