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
 * Object record for the Demand Charge portion of the posted energy data
 */
public class DemandRecord implements Serializable {
    static Logger logger = LoggerFactory.getLogger(DemandRecord.class);

    final Integer timestamp;
    final Double peak;
    final Double cost;


    public DemandRecord(Element demandElement) {
        timestamp = Integer.parseInt(demandElement.getAttribute("timestamp"));
        cost = Double.parseDouble(demandElement.getAttribute("cost"));
        peak = Double.parseDouble(demandElement.getAttribute("peak"));
    }


    public Integer getTimestamp() {
        return timestamp;
    }

    public Double getPeak() {
        return peak;
    }

    public Double getCost() {
        return cost;
    }
}
