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
 * Model that stores monthly cost data information.
 */
public class CostData implements Serializable {

    private Long gatewayId;
    private Integer timestamp;
    private Integer meterReadDay;
    private Integer meterReadMonth;
    private Integer meterReadYear;
    private Double fixedCost;
    private Double minCost;

    public CostData(){

    }

    public Long getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(Long gatewayId) {
        this.gatewayId = gatewayId;
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

    public Integer getMeterReadDay() {
        return meterReadDay;
    }

    public void setMeterReadDay(Integer meterReadDay) {
        this.meterReadDay = meterReadDay;
    }

    public Integer getMeterReadMonth() {
        return meterReadMonth;
    }

    public void setMeterReadMonth(Integer meterReadMonth) {
        this.meterReadMonth = meterReadMonth;
    }

    public Integer getMeterReadYear() {
        return meterReadYear;
    }

    public void setMeterReadYear(Integer meterReadYear) {
        this.meterReadYear = meterReadYear;
    }

    public Double getFixedCost() {
        return fixedCost;
    }

    public void setFixedCost(Double fixedCost) {
        this.fixedCost = fixedCost;
    }

    public Double getMinCost() {
        return minCost;
    }

    public void setMinCost(Double minCost) {
        this.minCost = minCost;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("CostData{");
        b.append("gatewayId:" + gatewayId);
        b.append(",timestamp:" + timestamp);
        b.append(", meterReadDay:" + meterReadDay);
        b.append(", meterReadMonth:" + meterReadMonth);
        b.append(", meterReadYear:" + meterReadYear);
        b.append(", fixedCost:" + fixedCost);
        b.append(", minCost:" + minCost);
        b.append("}");
        return b.toString();
    }


}
