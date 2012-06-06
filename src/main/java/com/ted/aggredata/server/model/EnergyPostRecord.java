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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an energy packet being posted by a gateway.
 */
public class EnergyPostRecord implements Serializable {
    static Logger logger = LoggerFactory.getLogger(EnergyPostRecord.class);

    final String gatewayId;
    final String authToken;
    final CostRecord costRecord;
    final Boolean isDemandKVA;
    final List<DemandRecord> demandRecords = new ArrayList<DemandRecord>();
    final List<MTURecord> mtuRecords = new ArrayList<MTURecord>();


    /**
     * Parses an XML energy post and creates an object
     *
     * @param doc the xml document
     */
    public EnergyPostRecord(Document doc) {

            NamedNodeMap ted5000Attributes = doc.getElementsByTagName("ted5000").item(0).getAttributes();
            gatewayId = ted5000Attributes.getNamedItem("GWID").getNodeValue();
            authToken = ted5000Attributes.getNamedItem("auth").getNodeValue();

            //Parse the COST element
            NodeList costList = doc.getElementsByTagName("COST");
            if (costList == null || costList.getLength() == 0) {
                logger.debug("Record DOES NOT contain a COST record");
                costRecord = new CostRecord();
            } else {
                logger.debug("Record contains a COST record");
                costRecord = new CostRecord((Element) costList.item(0));
            }

            //Parse the MTU and cumulative values
            NodeList mtuList = doc.getElementsByTagName("MTU");
            int mtuCount = mtuList.getLength();
            if (logger.isDebugEnabled()) logger.debug("Gateway " + gatewayId + "- MTU Count: " + mtuCount);
            for (int i = 0; i < mtuCount; i++) {
                Element mtuNode = (Element) mtuList.item(i);
                MTURecord mtuRecord = new MTURecord(mtuNode);
                mtuRecords.add(mtuRecord);
            }

            //Parse the Demand Element
            NodeList demandList = doc.getElementsByTagName("DEMAND");

            //If the packet has DEMAND data, enter it
            if (demandList != null && demandList.getLength() > 0) {
                Element demandElement = (Element) demandList.item(0);
                isDemandKVA = demandElement.getAttribute("kVA").equals("1");
                NodeList demandCostList = demandElement.getElementsByTagName("demandCost");
                int demandCostListCount = demandCostList.getLength();
                if (logger.isDebugEnabled()) logger.debug("Record contains " + demandCostListCount + " demand cost entries");
                for (int c = 0; c < demandCostListCount; c++) {
                    Element demandCostNode = (Element) demandCostList.item(c);
                    demandRecords.add(new DemandRecord(demandCostNode));
                }

            } else {
                logger.debug("Record DOES NOT contain any DEMAND charges");
                isDemandKVA = false;
            }
    }


    public String getGatewayId() {
        return gatewayId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public CostRecord getCostRecord() {
        return costRecord;
    }

    public Boolean getDemandKVA() {
        return isDemandKVA;
    }

    public List<DemandRecord> getDemandRecords() {
        return demandRecords;
    }

    public List<MTURecord> getMtuRecords() {
        return mtuRecords;
    }
}
