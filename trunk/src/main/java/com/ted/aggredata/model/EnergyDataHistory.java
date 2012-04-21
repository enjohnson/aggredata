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

package com.ted.aggredata.model;

import java.io.Serializable;
import java.util.Date;

/***
 * Object that holds the summarized energy data for a
 * timeframe query
 */
public class EnergyDataHistory implements Serializable  {

    private static final Long NET_GATEWAY = 0l;

    private Long gatewayId;
    private Long mtuId;
    private EnergyDataHistoryDate historyDate;
    private Double energy;
    private Double cost;

    private String key = null;


    /**
     * Returns true if this is history for the net gateway
     * @return
     */
    public Boolean isNet(){
        return gatewayId == NET_GATEWAY;
    }

    public EnergyDataHistoryDate getHistoryDate() {
        return historyDate;
    }

    public void setHistoryDate(EnergyDataHistoryDate historyDate) {
        this.historyDate = historyDate;
    }

    public Long getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(Long gatewayId) {
        this.gatewayId = gatewayId;
    }

    public Long getMtuId() {
        return mtuId;
    }

    public void setMtuId(Long mtuId) {
        this.mtuId = mtuId;
    }

    public Double getEnergy() {
        return energy;
    }

    public void setEnergy(Double energy) {
        this.energy = energy;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("EnergyDataHistory{");
        b.append("gatewayId:" + gatewayId);
        b.append(", mtuId:" + mtuId);
        b.append(",historyDate:" + historyDate);
        b.append(",energy:" + energy);
        b.append(",cost:" + cost);
        b.append("}");
        return b.toString();
    }



}
