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

/**
 * Single entry of power com.ted.aggredata.dao
 */
public class DemandCharge implements Serializable {
    private Long gatewayId;
    private Integer timestamp;
    private Double peak;
    private Double cost;

    public DemandCharge() {

    }


    /**
     * unix epoch timestamp of the entry
     *
     * @return
     */
    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }


    public Long getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(Long gatewayId) {
        this.gatewayId = gatewayId;
    }


    public Double getPeak() {
        return peak;
    }

    public void setPeak(Double peak) {
        this.peak = peak;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("EnergyData{");
        b.append("gatewayId:" + gatewayId);
        b.append(",timestamp:" + timestamp);
        b.append(", peak:" + peak);
        b.append(", cost:" + cost);
        b.append("}");
        return b.toString();
    }


}
