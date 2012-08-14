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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO for accessing and storing energy data  History
 */
public class EnergyDataHistoryDAO extends AbstractDAO<EnergyDataHistory> {

    static Logger logger = LoggerFactory.getLogger(EnergyDataHistoryDAO.class);

    public static String MONTHLY_SUMMARY = "select  0 as type, ed.gatewayId, ed.mtuId, " +
            "sum(ed.minuteCost) + max(cd.fixedCost) as cost, max(cd.minCost) as minCharge, " +
            "sum(ed.energyDifference) as energy, " +
            "cd.meterReadMonth as theMonth, cd.meterReadYear as theYear, 1 as theDay, 0 as theHour, 0 as theMinute " +
            "from energydata ed JOIN costdata cd on (ed.timestamp = cd.timestamp and ed.gatewayId = cd.gatewayId) " +
            "                   LEFT JOIN demandcharge dc on (ed.timestamp = dc.timestamp and ed.gatewayId = dc.gatewayId) " +
            "where ed.gatewayId=? and ed.mtuId=? " +
            "  and ed.timestamp>=? and ed.timestamp<? "+
            "group by ed.gatewayId, ed.mtuId, theMonth, theYear, theDay, theHour, theMinute " +
            "order by theYear, theMonth ";

    public static String DAILY_SUMMARY = "select 1 as type, gatewayId, mtuId, sum(minuteCost) as cost, 0 as minCharge, sum(energyDifference) as energy, " +
            " MONTH(CONVERT_TZ(FROM_UNIXTIME(timestamp),?, ?)) as theMonth, " +
            " YEAR(CONVERT_TZ(FROM_UNIXTIME(timestamp),?, ?)) as theYear, " +
            " DAY(CONVERT_TZ(FROM_UNIXTIME(timestamp),?, ?)) as theDay, " +
            " 0 as theHour, 0 as theMinute " +
            " from energydata " +
            " where gatewayId=? and mtuId=? and timestamp>=? and timestamp<?" +
            " group by gatewayId, mtuId, theMonth, theYear, theDay, theHour, theMinute" ;

    public static String HOURLY_SUMMARY = "select 2 as type, gatewayId, mtuId, sum(minuteCost) as cost,  0 as minCharge,  sum(energyDifference) as energy, " +
            " MONTH(CONVERT_TZ(FROM_UNIXTIME(timestamp),?, ?)) as theMonth, " +
            " YEAR(CONVERT_TZ(FROM_UNIXTIME(timestamp),?, ?)) as theYear, " +
            " DAY(CONVERT_TZ(FROM_UNIXTIME(timestamp),?, ?)) as theDay, " +
            " HOUR(CONVERT_TZ(FROM_UNIXTIME(timestamp),?, ?)) as theHour," +
            " 0 as theMinute " +
            " from energydata " +
            " where gatewayId=? and mtuId=? and timestamp>=? and timestamp<?" +
            " group by gatewayId, mtuId, theMonth, theYear, theDay, theHour, theMinute" ;

    public static String MINUTE_SUMMARY = "select 3 as type, gatewayId, mtuId, sum(minuteCost)*? as cost,  0 as minCharge,  sum(energyDifference)*? as energy, " +
            " FLOOR(MINUTE(CONVERT_TZ(FROM_UNIXTIME(timestamp),?, ?))/?)*? as theMinute," +
            " MONTH(CONVERT_TZ(FROM_UNIXTIME(timestamp),?, ?)) as theMonth, " +
            " YEAR(CONVERT_TZ(FROM_UNIXTIME(timestamp),?, ?)) as theYear, " +
            " DAY(CONVERT_TZ(FROM_UNIXTIME(timestamp),?, ?)) as theDay, " +
            " HOUR(CONVERT_TZ(FROM_UNIXTIME(timestamp),?, ?)) as theHour " +
            " from energydata " +
            " where gatewayId=? and mtuId=? and timestamp>=? and timestamp<?" +
            " group by gatewayId, mtuId, theMonth, theYear, theDay, theHour, theMinute";


    public EnergyDataHistoryDAO() {
        super("aggredata.energyDataHistory");
    }

    private RowMapper<EnergyDataHistory> rowMapper = new RowMapper<EnergyDataHistory>() {
        public EnergyDataHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
            EnergyDataHistory energyDataHistory = new EnergyDataHistory();

            energyDataHistory.setGatewayId(rs.getLong("gatewayId"));
            energyDataHistory.setMtuId(rs.getLong("mtuId"));
            EnergyDataHistoryDate energyDataHistoryDate = new EnergyDataHistoryDate(rs.getInt("theYear"),rs.getInt("theMonth"),rs.getInt("theDay"),rs.getInt("theHour"), rs.getInt("theMinute"));
            logger.debug("query returned: " + rs.getInt("theMinute") + " " + energyDataHistoryDate);
            energyDataHistory.setHistoryDate(energyDataHistoryDate);
            energyDataHistory.setCost(rs.getDouble("cost"));

            //Only worry about the min charge check on Monthly graphs. Zero values are ignored.
            if (rs.getInt("type") == 0)
            {
                //Minimum charge check.
                Double minCharge = rs.getDouble("minCharge");
                if (minCharge != 0 && minCharge.doubleValue() > energyDataHistory.getCost().doubleValue()){
                    energyDataHistory.setCost(minCharge);
                }
            }

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
            return getJdbcTemplate().query(MONTHLY_SUMMARY, new Object[]{
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

    /**
     * Returns a list of minute energy history for the given timestamp range
     */
    public List<EnergyDataHistory> findMinuteHistory(Gateway gateway, MTU mtu, long timestampStart, long timestampEnd, String serverTimeZone, String clientTimeZone, Integer interval) {
        try {
            int multiplier = 60/interval;

            return getJdbcTemplate().query(MINUTE_SUMMARY, new Object[]{
                    multiplier, multiplier,
                    serverTimeZone, clientTimeZone, //Minute
                    interval, //Interval of minutes to group (15, 5, 1)
                    interval, //Interval of minutes to group (15, 5, 1)
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
