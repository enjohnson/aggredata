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

package com.ted.aggredata.server.util;

import com.ted.aggredata.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/***
 * This creates a list of EnergyDataHistory for a group. This is responsible for the net calculations. This needs to be as efficient
 * as possible as its the most computationally expensive.
 */
public class EnergyDataHistoryResultFactory {

    static Logger logger = LoggerFactory.getLogger(EnergyDataHistoryResultFactory.class);
    private List<List<EnergyDataHistory>> combinedHistory;


    /**
     * The buckets that will sum up the individual MTU data for each gateway.
     */
    private class GatewayBucket {
        public boolean hasNet = false;
        public HashMap<EnergyDataHistoryDate, EnergyDataHistory> loadMap = new HashMap<EnergyDataHistoryDate, EnergyDataHistory>();
        public HashMap<EnergyDataHistoryDate, EnergyDataHistory> genMap = new HashMap<EnergyDataHistoryDate, EnergyDataHistory>();
        public HashMap<EnergyDataHistoryDate, EnergyDataHistory> netMap = new HashMap<EnergyDataHistoryDate, EnergyDataHistory>();
    }

    //Comparator for the set keys
    private static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
        List<T> list = new ArrayList<T>(c);
        java.util.Collections.sort(list);
        return list;
    }

    //Holds all the unique date keys for the group.
    HashMap<EnergyDataHistoryDate, Long> dateKeyMap;

    //The buckets for each gateway
    HashMap<Long , GatewayBucket> gatewayBucketHashMap;





    final List<Gateway> gatewayList;
    final Group group;


    public EnergyDataHistoryResultFactory(Group group, List<Gateway> gatewayList){
        //Initialize the factory holders
        dateKeyMap = new HashMap<EnergyDataHistoryDate, Long>();
        gatewayBucketHashMap = new HashMap<Long, GatewayBucket>();
        this.gatewayList = gatewayList;
        this.group = group;
    }


    /***
     * Returns the gateway bucket for the specified gateway.
     * @param gwId
     * @return
     */
    protected GatewayBucket getBucket(Long gwId) {
        GatewayBucket bucket = gatewayBucketHashMap.get(gwId);
        if (bucket == null) {
            bucket = new GatewayBucket();
            gatewayBucketHashMap.put(gwId, bucket);
        }
        return bucket;
    }

    /**
     * Adds a list of MTU history data to the internal buckets.
     * @param gateway
     * @param mtu
     * @param energyDataHistoryList
     */
    public void addHistory(Gateway gateway, MTU mtu, List<EnergyDataHistory> energyDataHistoryList) {

        if (logger.isDebugEnabled()) {
            logger.debug("Adding history[" + energyDataHistoryList.size() + "] for " + gateway + " " + mtu);
        }
        GatewayBucket bucket = getBucket(gateway.getId());

        //Iterate over the MTU's data add add it into the appropriate total bucket.
        for (EnergyDataHistory history : energyDataHistoryList) {

            EnergyDataHistoryDate key = history.getHistoryDate();
            //Keep a unique list of keys.
            if (!dateKeyMap.containsKey(key)) {
                if (logger.isDebugEnabled()) logger.debug("Adding new date key of " +key);
                dateKeyMap.put(key, key.getTime().getTime());
            }

            EnergyDataHistory gwHistory = null;
            //Get the map we are supposed to be using.
            if (mtu.getType() == MTU.MTUType.LOAD) {
                gwHistory = bucket.loadMap.get(key);
                if (gwHistory == null) bucket.loadMap.put(key, history);
            } else if (mtu.getType() == MTU.MTUType.GENERATION) {
                gwHistory = bucket.genMap.get(key);
                if (gwHistory == null) bucket.genMap.put(key, history);
            } else if (mtu.getType() == MTU.MTUType.ADJUSTED_NET) {
                bucket.hasNet = true;
                gwHistory = bucket.netMap.get(key);
                if (gwHistory == null) bucket.netMap.put(key, history);
            }
            if (gwHistory != null) {
                gwHistory.setEnergy(gwHistory.getCost() + history.getCost());
                gwHistory.setCost(gwHistory.getCost() + history.getCost());
            }
        }
    }


    /**
     * Returns the combined history for all gateways that have been added thus far.
     * @return
     */
    public EnergyDataHistoryQueryResult getCombinedHistory() {
        //Sort the keys now that all history has been loaded
        List<EnergyDataHistoryDate> sortedKeys = asSortedList(dateKeyMap.keySet());


        EnergyDataHistoryQueryResult result = new EnergyDataHistoryQueryResult();
        result.setGroup(group);
        result.setGatewayList(gatewayList);

        //Iterate over each gateway to calculate its totals
        for (Long gwId: gatewayBucketHashMap.keySet()) {
            GatewayBucket bucket = getBucket(gwId);
            int index = 0;
            ArrayList<EnergyDataHistory> gatewayHistory = new ArrayList<EnergyDataHistory>();
            result.getGatewayHistoryList().put(gwId, gatewayHistory);
            for (EnergyDataHistoryDate key: sortedKeys){
                EnergyDataHistory groupEnergyDataHistory = null;
                logger.debug("Adding " + key);

                if (result.getNetHistoryList().size() < (index+1)) {
                    //Add a new group History Object
                    EnergyDataHistory energyDataHistory = new EnergyDataHistory();
                    energyDataHistory.setGatewayId(0l);
                    energyDataHistory.setMtuId(0l);
                    energyDataHistory.setHistoryDate(key);
                    energyDataHistory.setEnergy(0d);
                    energyDataHistory.setCost(0d);
                    result.getNetHistoryList().add(energyDataHistory);
                    logger.debug("Adding new NET");
                }

                groupEnergyDataHistory = result.getNetHistoryList().get(index);
                EnergyDataHistory energyDataHistory = getGatewayTotal(key,gwId, bucket);
                groupEnergyDataHistory.setEnergy(groupEnergyDataHistory.getEnergy() + energyDataHistory.getEnergy());
                groupEnergyDataHistory.setCost(groupEnergyDataHistory.getCost() + energyDataHistory.getCost());

                //Add the gateway data
                List<EnergyDataHistory> gatewayHistoryList = result.getGatewayHistoryList().get(gwId);
                if (gatewayHistoryList == null) {
                    gatewayHistoryList  =  new ArrayList<EnergyDataHistory>();
                    result.getGatewayHistoryList().put(gwId, gatewayHistoryList);
                }
                gatewayHistoryList.add(energyDataHistory);

                index++;
            }

        }

        return result;
    }


    /***
     * Does the NET calculation for the gateway based on its mtu types.
     * @param key
     * @param gwId
     * @param bucket
     * @return
     */
    private EnergyDataHistory getGatewayTotal(EnergyDataHistoryDate key, Long gwId, GatewayBucket bucket)
    {
        double totalEnergyLoad = 0;
        double totalEnergyGen = 0;
        double totalEnergyNetMeter = 0;
        double totalCostLoad = 0;
        double totalCostGen = 0;
        double totalCostNetMeter = 0;

        if (bucket.loadMap.get(key) != null) {
            totalEnergyLoad = bucket.loadMap.get(key).getEnergy();
            totalCostLoad = bucket.loadMap.get(key).getCost();
        }
        if (bucket.genMap.get(key) != null) {
            totalEnergyGen = bucket.genMap.get(key).getEnergy();
            totalCostGen = bucket.genMap.get(key).getCost();
        }
        if (bucket.netMap.get(key) != null) {
            totalEnergyNetMeter = bucket.netMap.get(key).getEnergy();
            totalCostNetMeter = bucket.netMap.get(key).getCost();
        }


        //Adjust values if adjusted load is used.
        if (bucket.hasNet) {
            totalEnergyNetMeter -= totalEnergyGen;
            totalCostNetMeter -= totalCostGen;
        }
        EnergyDataHistory energyDataHistory = new EnergyDataHistory();
        energyDataHistory.setGatewayId(gwId);
        energyDataHistory.setMtuId(0l);
        energyDataHistory.setHistoryDate(key);
        energyDataHistory.setEnergy(totalEnergyNetMeter + totalEnergyGen + totalEnergyLoad);
        energyDataHistory.setCost(totalCostNetMeter + totalCostGen + totalCostLoad);
        return energyDataHistory;

    }

}
