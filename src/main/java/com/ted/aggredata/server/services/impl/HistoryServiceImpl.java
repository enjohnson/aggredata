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

package com.ted.aggredata.server.services.impl;

import com.ted.aggredata.model.*;
import com.ted.aggredata.server.dao.EnergyDataHistoryDAO;
import com.ted.aggredata.server.services.GatewayService;
import com.ted.aggredata.server.services.GroupService;
import com.ted.aggredata.server.services.HistoryService;
import com.ted.aggredata.server.util.EnergyDataHistoryResultFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
public class HistoryServiceImpl implements HistoryService {

    private static long DAY_OFFSET = 24 * (3600 * 1000);

    @Autowired
    protected EnergyDataHistoryDAO energyDataHistoryDAO;

    @Autowired
    protected GroupService groupService;

    @Autowired
    protected GatewayService gatewayService;

    @Autowired
    ServerInfo serverInfo;


    static Logger logger = LoggerFactory.getLogger(HistoryServiceImpl.class);


    /**
     * Common method for combining and returning history. No matter the type, the algorithm used to total the mtu's and gateway values
     * are the same (just slightly different queries.
     */
    public EnergyDataHistoryQueryResult getHistory(Enums.HistoryType type, User user, Group group, long startTime, long endTime) {

        if (logger.isDebugEnabled()) {
            logger.debug("Looking up " + type + " history for " + user + " and " + group + " for the range " + new Date(startTime) + " to " + new Date(endTime));
        }

        List<Gateway> groupGateways = gatewayService.findByGroup(group);
        EnergyDataHistoryResultFactory energyDataHistoryFactory = new EnergyDataHistoryResultFactory(group, groupGateways);

        for (Gateway gateway : groupGateways) {
            if (logger.isDebugEnabled()) logger.debug("Loading History for gateway " + gateway);
            //Create the gateway bucket.
            //Find the MTU's and iterate over them to load history and assign the values to the correct bucket.
            List<MTU> mtuList = gatewayService.findMTUByGateway(gateway);
            for (MTU mtu : mtuList) {
                if (logger.isDebugEnabled()) logger.debug("Loading History for mtu " + mtu);
                List<EnergyDataHistory> historyList = getHistory(type, user, gateway, mtu, startTime, endTime);
                if (logger.isDebugEnabled()) logger.debug("Adding History for mtu " + mtu);
                energyDataHistoryFactory.addHistory(gateway, mtu, historyList);
            }
        }

        logger.debug("Returning combined history");
        return energyDataHistoryFactory.getCombinedHistory();
    }


    private List<EnergyDataHistory> getHistory(Enums.HistoryType type, User user, Gateway gateway, MTU mtu, long startTime, long endTime) {

        if (type.equals(Enums.HistoryType.MONTHLY)) return energyDataHistoryDAO.findMonthHistory(gateway, mtu, startTime/1000, endTime/1000, serverInfo.getTimezone(), user.getTimezone());
        if (type.equals(Enums.HistoryType.DAILY)) return energyDataHistoryDAO.findDailyHistory(gateway, mtu, startTime/1000, endTime/1000, serverInfo.getTimezone(), user.getTimezone());
        if (type.equals(Enums.HistoryType.HOURLY)) return energyDataHistoryDAO.findHourlyHistory(gateway, mtu, startTime/1000, endTime/1000, serverInfo.getTimezone(), user.getTimezone());
        return new ArrayList<EnergyDataHistory>();

    }




}
