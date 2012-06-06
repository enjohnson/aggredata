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
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MTURecord implements Serializable {
    static Logger logger = LoggerFactory.getLogger(MTURecord.class);

    private final Long id;
    private final Integer type;
    private final List<CumulativeCostRecord> cumulativeCostRecordList = new ArrayList<CumulativeCostRecord>();

    /**
     * Constructs the MTU from the MTU Element
     * @param mtuElement
     */
    public MTURecord(Element mtuElement){

        String mtuIdString = mtuElement.getAttribute("ID");
        id = Long.parseLong(mtuIdString, 16);
        type = Integer.parseInt(mtuElement.getAttribute("type"));

        //Iterate over the cumulative values of this node
        NodeList cumulativeDataList = mtuElement.getElementsByTagName("cumulative");
        int cumulativeDataListCount = cumulativeDataList.getLength();

        for (int c = 0; c < cumulativeDataListCount; c++) {
            Element cumulativeNode = (Element) cumulativeDataList.item(c);
            CumulativeCostRecord cumulativeCostRecord = new CumulativeCostRecord(cumulativeNode);
            cumulativeCostRecordList.add(cumulativeCostRecord);
        }

    }

    public Long getId() {
        return id;
    }

    public Integer getType() {
        return type;
    }

    public List<CumulativeCostRecord> getCumulativeCostRecordList() {
        return cumulativeCostRecordList;
    }
}
