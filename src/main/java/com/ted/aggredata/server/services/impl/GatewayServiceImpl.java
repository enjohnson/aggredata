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
import com.ted.aggredata.server.dao.*;
import com.ted.aggredata.server.services.GatewayService;
import com.ted.aggredata.server.util.KeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class GatewayServiceImpl implements GatewayService {

    @Autowired
    ServerInfo serverInfo;


    @Autowired
    protected GatewayDAO gatewayDAO;

    @Autowired
    protected GroupDAO groupDAO;


    @Autowired
    protected MTUDAO mtuDAO;

    @Autowired
    protected EnergyDataDAO energyDataDAO;

    @Autowired
    protected EnergyDataHistoryDAO energyDataHistoryDAO;

    Logger logger = LoggerFactory.getLogger(getClass());

    public Gateway createGateway(Group group, User userAccount, String serialNumber, String description) {
        if (logger.isInfoEnabled()) logger.info("Adding a new gateway with the serial number " + serialNumber + " for " + userAccount + " at " + group);
        Gateway gateway = new Gateway();
        gateway.setId(Long.parseLong(serialNumber, 16));
        gateway.setUserAccountId(userAccount.getId());
        gateway.setDescription(description);
        gateway.setState(true);
        try {
            gateway = gatewayDAO.create(gateway);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }

        gatewayDAO.addGatewayToGroup(gateway, group);
        return gateway;
    }

    public void deleteGateway(Gateway gateway) {
        if (logger.isInfoEnabled()) logger.info("Deleting " + gateway + " and all energy information for it");
        gatewayDAO.delete(gateway);
    }


    public MTU addMTU(Gateway gateway, String mtuSerialNumber, MTU.MTUType type, String description) {
        if (logger.isInfoEnabled()) logger.info("Adding MTU " + mtuSerialNumber + " to " + gateway);
        MTU mtu = new MTU();
        mtu.setId(Long.parseLong(mtuSerialNumber, 16));
        mtu.setDescription(description);
        mtu.setType(type);
        mtu.setGatewayId(gateway.getId());
        return mtuDAO.create(gateway, mtu);
    }

    public Gateway disableGateWay(Gateway gateway) {
        if (logger.isInfoEnabled()) logger.info("Disabling " + gateway);
        gateway.setState(false);
        gateway.setSecurityKey("");
        gatewayDAO.save(gateway);
        return gateway;
    }

    public Gateway activateGateway(Gateway gateway) {
        if (logger.isInfoEnabled()) logger.info("Activating " + gateway);
        String key = KeyGenerator.generateSecurityKey(18);
        gateway.setState(true);
        gateway.setSecurityKey(key);
        gatewayDAO.save(gateway);
        return gateway;
    }

    @Override
    public List<Gateway> findByUser(User user) {
        return gatewayDAO.findByUserAccount(user);
    }

    @Override
    public List<Gateway> findByGroup(Group group) {
        return gatewayDAO.findByGroup(group);
    }

    @Override
    public Gateway getById(Long id) {
        return gatewayDAO.findById(id);
    }

    @Override
    public MTU getMTU(Gateway gateway, Long mtuId) {
        MTU mtu = mtuDAO.findById(gateway.getId(), mtuId);
        if (mtu == null) return null;
        if (mtu.getGatewayId().equals(gateway.getId())) return mtu;
        if (logger.isDebugEnabled()) logger.debug("MTU " + Long.toHexString(mtuId) + " does not belong to gateway " + gateway);
        return null;
    }

    @Override
    public EnergyData postEnergyData(Gateway gateway, MTU mtu, Integer timestamp, Double watts, Double rate, Double minCost, Double energyDifference) {
        EnergyData energyData = new EnergyData();
        energyData.setGatewayId(gateway.getId());
        energyData.setMtuId(mtu.getId());
        energyData.setTimestamp(timestamp);
        energyData.setEnergy(watts);
        energyData.setRate(rate);
        energyData.setMinuteCost(minCost);
        energyData.setEnergyDifference(energyDifference);
        energyDataDAO.create(energyData);
        return energyData;
    }

    @Override
    public EnergyData findByLastPost(Gateway gateway, MTU mtu, Integer timestamp) {
        return energyDataDAO.findByLastPost(gateway, mtu, timestamp);
    }

    @Override
    public Integer countByUser(User user) {
        return gatewayDAO.countByUserAccount(user);
    }

    @Override
    public void saveGateway(Gateway gateway) {
        gatewayDAO.save(gateway);
    }

    @Override
    public void saveMTU(MTU mtu) {
        mtuDAO.save(mtu);
    }

    @Override
    public List<MTU> findMTUByGateway(Gateway gateway) {
        return mtuDAO.findByGateway(gateway);
    }

    @Override
    public void deleteMTU(Gateway gateway, MTU mtu) {
        mtuDAO.delete(gateway, mtu);
    }

    @Override
    public List<EnergyDataHistory> findMonthlyHistory(User user, Gateway gateway, MTU mtu, Long timestampStart, long timestampEnd) {
        return energyDataHistoryDAO.findMonthHistory(gateway, mtu, timestampStart, timestampEnd, serverInfo.getTimezone(), user.getTimezone());
    }

    @Override
    public List<EnergyDataHistory> findDailyHistory(User user, Gateway gateway, MTU mtu, Long timestampStart, long timestampEnd) {
        return energyDataHistoryDAO.findDailyHistory(gateway, mtu, timestampStart, timestampEnd, serverInfo.getTimezone(), user.getTimezone());
    }

    @Override
    public List<EnergyDataHistory> findHourlyHistory(User user, Gateway gateway, MTU mtu, Long timestampStart, long timestampEnd) {
        return energyDataHistoryDAO.findDailyHistory(gateway, mtu, timestampStart, timestampEnd, serverInfo.getTimezone(), user.getTimezone());
    }

}
