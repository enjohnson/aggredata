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
import com.ted.aggredata.server.dao.CostDataDAO;
import com.ted.aggredata.server.dao.DemandChargeDAO;
import com.ted.aggredata.server.dao.EnergyDataDAO;
import com.ted.aggredata.server.services.EnergyPostService;
import org.springframework.beans.factory.annotation.Autowired;

public class EnergyPostServiceImpl implements EnergyPostService {

    @Autowired
    ServerInfo serverInfo;

    @Autowired
    protected EnergyDataDAO energyDataDAO;


    @Autowired
    protected DemandChargeDAO demandChargeDAO;

    @Autowired
    protected CostDataDAO costDataDAO;



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
    public CostData postCostData(CostData costData) {
        costDataDAO.create(costData);
        return costData;
    }

    @Override
    public DemandCharge postDemandCharge(Gateway gateway, Integer timestamp, Double peak, Double cost) {
        DemandCharge demandCharge = new DemandCharge();
        demandCharge.setGatewayId(gateway.getId());
        demandCharge.setTimestamp(timestamp);
        demandCharge.setPeak(peak);
        demandCharge.setCost(cost);
        demandChargeDAO.create(demandCharge);
        return  demandCharge;
    }

    @Override
    public EnergyData findByLastPost(Gateway gateway, MTU mtu, Integer timestamp) {
        return energyDataDAO.findByLastPost(gateway, mtu, timestamp);
    }

}
