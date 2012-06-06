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
import com.ted.aggredata.server.model.CumulativeCostRecord;
import com.ted.aggredata.server.model.DemandRecord;
import com.ted.aggredata.server.model.EnergyPostRecord;
import com.ted.aggredata.server.model.MTURecord;
import com.ted.aggredata.server.services.EnergyPostException;
import com.ted.aggredata.server.services.EnergyPostService;
import com.ted.aggredata.server.services.GatewayService;
import com.ted.aggredata.server.util.EnergyPostUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.parsers.DocumentBuilderFactory;
import java.util.Calendar;
import java.util.HashMap;

public class EnergyPostServiceImpl implements EnergyPostService {

    static Logger logger = LoggerFactory.getLogger(EnergyPostServiceImpl.class);
    static DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

    @Autowired
    ServerInfo serverInfo;

    @Autowired
    protected EnergyDataDAO energyDataDAO;

    @Autowired
    protected DemandChargeDAO demandChargeDAO;

    @Autowired
    protected CostDataDAO costDataDAO;

    @Autowired
    protected GatewayService gatewayService;


    @Override
    public void postEnergyData(User postingUser, Gateway gateway, EnergyPostRecord energyPostRecord) throws EnergyPostException {

        try {


            //We keep a local hashmap to track unique cost data entries (limiting number of db hits used to update
            //and more efficient than an array list contains check.
            HashMap<Integer, CostData> costDataCache = new HashMap<Integer, CostData>();


            if (logger.isDebugEnabled()) logger.debug("Gateway " + energyPostRecord.getGatewayId() + "- MTU Count: " + energyPostRecord.getMtuRecords().size());

            for (MTURecord mtuRecord : energyPostRecord.getMtuRecords()) {
                Long mtuId = Long.parseLong(mtuRecord.getId(), 16);

                MTU mtu = gatewayService.getMTU(gateway, mtuId);
                if (mtu == null) {
                    logger.info("MTU with id " + mtuRecord.getId() + " not found for " + gateway + ". Adding the MTU to the gateway");
                    mtu = gatewayService.addMTU(gateway, mtuRecord.getId(), MTU.ordinalToMTUType(mtuRecord.getType()), "MTU " + mtuRecord.getId());
                }


                if (logger.isDebugEnabled()) logger.debug("Adding cumulative data to " + mtu + " for " + gateway);

                EnergyData lastCumulativeValue = null;

                for (CumulativeCostRecord ccr : mtuRecord.getCumulativeCostRecordList()) {

                    Double energyDifference = 0d;
                    Double minuteCost = 0d;

                    if (lastCumulativeValue == null) {
                        lastCumulativeValue = findByLastPost(gateway, mtu, ccr.getTimestamp());
                        if (logger.isDebugEnabled()) logger.debug("Last Post:" + lastCumulativeValue);
                    }

                    //Make sure that we are not dealing with the first entry for this gateway/mtu
                    if (lastCumulativeValue != null) {
                        Double wattDifference = ccr.getEnergy() - lastCumulativeValue.getEnergy();
                        energyDifference = wattDifference;
                        wattDifference = wattDifference / 1000; //Rate is on kWh

                        logger.debug("kW:" + wattDifference);
                        minuteCost = (wattDifference * ccr.getRate());

                    }

                    //Calculate the number of minutes since the last post.
                    Integer minutes = 1;
                    if (lastCumulativeValue != null) {
                        minutes = (ccr.getTimestamp() - lastCumulativeValue.getTimestamp()) / 60;

                    }
                    if (logger.isDebugEnabled()) logger.debug("Posting " + minutes + " minutes worth of data");

                    for (int min = 0; min < minutes; min++) {
                        int ts = ccr.getTimestamp() - ((minutes - (1+min)) * 60);

                        if (logger.isDebugEnabled() && lastCumulativeValue != null) {
                            logger.debug("Posting " + ts + " between " + lastCumulativeValue.getTimestamp() + " and " + ccr.getTimestamp());
                        }


                        //Make sure there is a cost data element for this gateway and timestamp.
                        if (costDataCache.get(ccr.getTimestamp()) == null) {
                            CostData costData = new CostData();
                            costData.setGatewayId(gateway.getId());
                            costData.setTimestamp(ts);
                            costData.setMeterReadDay(energyPostRecord.getCostRecord().getMeterReadDay());
                            costData.setMinCost(energyPostRecord.getCostRecord().getMinCharge());
                            costData.setFixedCost(energyPostRecord.getCostRecord().getFixedCharge());

                            //Set the month and year based on the user's timezone.
                            //Do the timezone conversion
                            Calendar tzCalendar = EnergyPostUtil.getMeterMonth(energyPostRecord.getCostRecord().getMeterReadDay(), postingUser.getTimezone(), ts);
                            costData.setMeterReadYear(tzCalendar.get(Calendar.YEAR));
                            costData.setMeterReadMonth(tzCalendar.get(Calendar.MONTH) + 1);

                            //Add it to the hashmap as a unique timestamp.
                            costDataCache.put(ccr.getTimestamp(), costData);
                        }

                        lastCumulativeValue = postEnergyData(gateway, mtu, ts, ccr.getEnergy(), ccr.getRate(), minuteCost / (double) minutes, energyDifference / (double) minutes);
                        if (logger.isDebugEnabled()) logger.debug("Posted " + lastCumulativeValue);

                    }

                }

            }


            //------------------POST ACCUMULATED COST DATA------------------------------------
            for (CostData costData : costDataCache.values()) {
                costDataDAO.create(costData);
            }

            for (DemandRecord demandRecord : energyPostRecord.getDemandRecords()) {
                postDemandCharge(gateway, energyPostRecord.getDemandKVA() ? 1 : 0, demandRecord);
            }
        } catch (Exception ex) {
            throw new EnergyPostException(ex.getMessage(), ex);
        }
    }


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


    public DemandCharge postDemandCharge(Gateway gateway, int type, DemandRecord demandRecord) {
        DemandCharge demandCharge = new DemandCharge();
        demandCharge.setGatewayId(gateway.getId());
        demandCharge.setTimestamp(demandRecord.getTimestamp());
        demandCharge.setPeak(demandRecord.getPeak());
        demandCharge.setCost(demandRecord.getCost());
        demandCharge.setType(type);
        demandChargeDAO.create(demandCharge);
        return demandCharge;
    }

    public EnergyData findByLastPost(Gateway gateway, MTU mtu, Integer timestamp) {
        return energyDataDAO.findByLastPost(gateway, mtu, timestamp);
    }

}
