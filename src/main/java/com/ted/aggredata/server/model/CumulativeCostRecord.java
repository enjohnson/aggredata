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
import org.w3c.dom.Element;

import java.io.Serializable;

public class CumulativeCostRecord implements Serializable {

    static Logger logger = LoggerFactory.getLogger(CostRecord.class);

    final Integer timestamp;
    final Double energy;
    final Double rate;

    public CumulativeCostRecord(Element cumulativeNode) {
        timestamp = Integer.parseInt(cumulativeNode.getAttribute("timestamp"));
        rate = Double.parseDouble(cumulativeNode.getAttribute("rate"));
        energy = Double.parseDouble(cumulativeNode.getAttribute("watts"));
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public Double getEnergy() {
        return energy;
    }

    public Double getRate() {
        return rate;
    }
}
