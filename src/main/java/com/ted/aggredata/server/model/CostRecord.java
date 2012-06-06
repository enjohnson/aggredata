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

package com.ted.aggredata.server.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Element;

import java.io.Serializable;

/**
 * Object record for the COST portion of the energy data
 */
public class CostRecord implements Serializable {

    static Logger logger = LoggerFactory.getLogger(CostRecord.class);

    final Integer meterReadDay;
    final Double fixedCharge;
    final Double minCharge;

    /**
     * Constructs the cost record from the element
     * @param costElement
     */
    public CostRecord(Element costElement){
        NamedNodeMap costAttributes = costElement.getAttributes();
        meterReadDay = Integer.parseInt(costAttributes.getNamedItem("mrd").getNodeValue());
        fixedCharge = Double.parseDouble(costAttributes.getNamedItem("fixed").getNodeValue());
        minCharge = Double.parseDouble(costAttributes.getNamedItem("min").getNodeValue());
    }

    public CostRecord() {
        meterReadDay = 1;
        fixedCharge = 0d;
        minCharge = 0d;
    }

    public Integer getMeterReadDay() {
        return meterReadDay;
    }

    public Double getFixedCharge() {
        return fixedCharge;
    }

    public Double getMinCharge() {
        return minCharge;
    }
}
