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
public class EnergyData extends AggredataModel implements Serializable {
    private Long gatewayId;
    private Long mtuId;
    private Integer timestamp;
    private Double rate;
    private Double energy;
    private Double minuteCost;

    public EnergyData() {

    }

    /**
     * unique id of the mtu that logged this com.ted.aggredata.dao point
     *
     * @return
     */
    public Long getMtuId() {
        return mtuId;
    }

    public void setMtuId(Long mtuId) {
        this.mtuId = mtuId;
    }

    /**
     * unix epoch timestamp of the entr
     *
     * @return
     */
    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * rat e in effect when the energy value was stored.
     *
     * @return
     */
    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    /**
     * total cumulative watt hours recorded by the mtu for the specified time
     *
     * @return
     */
    public Double getEnergy() {
        return energy;
    }

    public void setEnergy(Double energy) {
        this.energy = energy;
    }

    public Double getMinuteCost() {
        return minuteCost;
    }

    public void setMinuteCost(Double minuteCost) {
        this.minuteCost = minuteCost;
    }

    public Long getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(Long gatewayId) {
        this.gatewayId = gatewayId;
    }

    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("EnergyData{");
        b.append(",id:" + getId());
        b.append(",timestamp:" + timestamp);
        b.append(", rate:" + rate);
        b.append(", energy:" + energy);
        b.append(", minuteCost:" + minuteCost);
        b.append(", gatewayId:" + gatewayId);
        b.append(", mtuId:" + mtuId);
        b.append("}");
        return b.toString();
    }
}
