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

package com.ted.aggredata.server.services;


import com.ted.aggredata.model.*;

/***
 * Service for posting energy data
 */
public interface EnergyPostService {

    /**
     * Creates a new EnergyData entry in the system
     *
     * @param gateway
     * @param mtu
     * @param timestamp
     * @param watts
     * @param rate
     * @param minCost
     * @param energyDifference
     * @return
     */
    EnergyData postEnergyData(Gateway gateway, MTU mtu, Integer timestamp, Double watts, Double rate, Double minCost, Double energyDifference);


    /**
     * Posts cost data for a specified gateway
     * @param costData
     * @return
     */
    CostData postCostData(CostData costData);


    /***
     * Posts a demand charge
     * @param gateway
     * @param timestamp
     * @param peak
     * @param cost
     * @return
     */
    DemandCharge postDemandCharge(Gateway gateway, Integer timestamp, Double peak, Double cost);


    /**
     * Finds the most recently posted entry for the gateway/mtu
     *
     * @param gateway
     * @param mtu
     * @param timestamp
     * @return
     */
    EnergyData findByLastPost(Gateway gateway, MTU mtu, Integer timestamp);


}
