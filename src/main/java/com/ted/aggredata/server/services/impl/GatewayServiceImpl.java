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

import com.ted.aggredata.model.Gateway;
import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.MTU;
import com.ted.aggredata.model.User;
import com.ted.aggredata.server.dao.*;
import com.ted.aggredata.server.services.GatewayService;
import com.ted.aggredata.server.util.KeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

@Transactional
public class GatewayServiceImpl implements GatewayService {

    @Autowired
    protected GatewayDAO gatewayDAO;

    @Autowired
    protected GroupDAO groupDAO;

    
    @Autowired
    protected MTUDAO mtuDAO;

    @Autowired
    protected EnergyDataDAO energyDataDAO;

    Logger logger = LoggerFactory.getLogger(getClass());

    public Gateway createGateway(Group group, User userAccount, String serialNumber, String description) {
        if (logger.isInfoEnabled()) logger.info("Adding a new gateway with the serial number " + serialNumber + " for " + userAccount + " at " + group);
        Gateway gateway = new Gateway();
        gateway.setUserAccountId(userAccount.getId());
        gateway.setId(Long.parseLong(serialNumber, 16));
        gateway.setDescription(description);
        try{
            gateway = gatewayDAO.create(gateway);    
        } catch (Exception ex){
            logger.error(ex.getMessage(), ex);
            return null;
        }

        gatewayDAO.addGatewayToGroup(gateway, group);
        return gateway;
    }

    public void deleteGateway(Gateway gateway) {
        if (logger.isInfoEnabled()) logger.info("Deleting " + gateway + " and all energy information for it");
        List<MTU> mtuList = mtuDAO.getByGateway(gateway);
        Iterator<MTU> mtuIterator = mtuList.iterator();
        while (mtuIterator.hasNext()) {
            MTU mtu = mtuIterator.next();
            logger.info("Deleting data for " + mtu + " as part of " + gateway + " deletion");
            energyDataDAO.removeEnergyData(mtu);
            mtuDAO.delete(mtu);
        }

        gatewayDAO.delete(gateway);
    }



    public MTU addMTU(Gateway gateway, String mtuSerialNumber, MTU.MTUType type, String description) {
        if (logger.isInfoEnabled()) logger.info("Adding MTU " + mtuSerialNumber + " to " + gateway);
        MTU existingMTU = mtuDAO.findById(Long.parseLong(mtuSerialNumber, 16));
        if (existingMTU != null) {
            logger.info("MTU Already exists. Updating information");
            existingMTU.setDescription(description);
            existingMTU.setType(type);
            existingMTU.setGatewayId(gateway.getId());
            mtuDAO.save(existingMTU);
        } else {
            MTU mtu = new MTU();
            mtu.setId(Long.parseLong(mtuSerialNumber,16));
            mtu.setDescription(description);
            mtu.setType(type);
            mtu.setGatewayId(gateway.getId());
            mtuDAO.save(mtu);
        }
        return mtuDAO.findById(Long.parseLong(mtuSerialNumber, 16));
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
        return gateway;
    }

    @Override
    public List<Gateway> getByUser(User user) {
        return gatewayDAO.findByUserAccount(user);
    }

    @Override
    public List<Gateway> getByGroup(Group group) {
        return gatewayDAO.findByGroup(group);
    }
}
