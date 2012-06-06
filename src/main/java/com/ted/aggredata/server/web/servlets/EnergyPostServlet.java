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

package com.ted.aggredata.server.web.servlets;

import com.ted.aggredata.model.*;
import com.ted.aggredata.server.services.EnergyPostService;
import com.ted.aggredata.server.services.GatewayService;
import com.ted.aggredata.server.services.GroupService;
import com.ted.aggredata.server.services.UserService;
import com.ted.aggredata.server.util.EnergyPostUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;


/**
 * *
 * This is the servlet that handles activation of a new gateway. It checks the security key against enabled user accounts
 * and adds the gateway and mtu's if one does not exists. If a gateway exists, it updates the gateway as active if it has been marked inactive.
 */
public class EnergyPostServlet extends HttpServlet {

    static Logger logger = LoggerFactory.getLogger(EnergyPostServlet.class);
    static DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

    @Autowired
    UserService userService;

    @Autowired
    GatewayService gatewayService;

    @Autowired
    EnergyPostService energyPostService;


    @Autowired
    GroupService groupService;

    @Autowired
    ServerInfo serverInfo;


    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            logger.debug("EnergyPost POST received");

            DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();
            Document doc = db.parse(request.getInputStream());

            //Validate security token
            NamedNodeMap ted5000Attributes = doc.getElementsByTagName("ted5000").item(0).getAttributes();


            String gatewayIdString = ted5000Attributes.getNamedItem("GWID").getNodeValue();
            String securityKey = ted5000Attributes.getNamedItem("auth").getNodeValue();
            if (logger.isDebugEnabled()) logger.debug("Received post from Gateway " + gatewayIdString + " with key " + securityKey);

            //Verify this gateway exists, has the correct key, and is enabled to receive posts.
            Gateway gateway = gatewayService.getById(Long.parseLong(gatewayIdString, 16));

            //Check to see if gateway exists
            if (gateway == null) {
                if (logger.isWarnEnabled()) logger.warn("Gateway with id " + gatewayIdString + " not found in database");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                PrintWriter out = response.getWriter();
                out.write("Gateway Not Found.");
                out.close();
                return;
            }

            //Check the user
            User gatewayUser = userService.findUser(gateway.getUserAccountId());
            if (gatewayUser == null || gatewayUser.getAccountState() != User.STATE_ENABLED) {
                if (logger.isWarnEnabled()) logger.warn("Attempted post to gateway " + gatewayIdString + " with an invalid or disabled user: " + gatewayUser);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                PrintWriter out = response.getWriter();
                out.write("Unauthorized.");
                out.close();
                return;
            }

            //Check security key
            if (!gateway.getSecurityKey().equals(securityKey)) {
                if (logger.isWarnEnabled()) logger.warn("Attempted post to gateway " + gatewayIdString + " with an invalid authentication key");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                PrintWriter out = response.getWriter();
                out.write("Unauthorized.");
                out.close();
                return;
            }

            //Check to see if its enabled
            if (!gateway.getState()) {
                if (logger.isWarnEnabled()) logger.warn("Gateway with id " + gatewayIdString + " is not enabled to receive posts.");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                PrintWriter out = response.getWriter();
                out.write("Unauthorized.");
                out.close();
                return;
            }

            if (logger.isInfoEnabled()) logger.info("gateway " + gatewayIdString + " is authenticated. recording data.");


            //Default values in case cost segment is not defined.
            Integer meterReadDay = 1;
            Double fixedCharge = 0d;
            Double minCharge = 0d;


            //We keep a local hashmap to track unique cost data entries (limiting number of db hits used to update
            //and more efficient than an array list contains check.
            HashMap<Integer, CostData> costDataCache = new HashMap<Integer, CostData>();


            //Process cost data
            //If the packet has COST data, enter it
            NodeList costList = doc.getElementsByTagName("COST");
            if (costList != null && costList.getLength() > 0) {
                logger.debug("Processing COST Packet");
                try {
                    NamedNodeMap costAttributes = costList.item(0).getAttributes();
                    meterReadDay = Integer.parseInt(costAttributes.getNamedItem("mrd").getNodeValue());
                    fixedCharge = Double.parseDouble(costAttributes.getNamedItem("fixed").getNodeValue());
                    minCharge = Double.parseDouble(costAttributes.getNamedItem("min").getNodeValue());
                } catch (Exception ex) {
                    logger.error("Exception parsing cost data:" + ex.getMessage(), ex);
                }


            }


            //Process each MTU
            NodeList mtuList = doc.getElementsByTagName("MTU");
            int mtuCount = mtuList.getLength();
            if (logger.isDebugEnabled()) logger.debug("Gateway " + gatewayIdString + "- MTU Count: " + mtuCount);
            for (int i = 0; i < mtuCount; i++) {
                Element mtuNode = (Element) mtuList.item(i);
                String mtuIdString = mtuNode.getAttribute("ID");
                Long mtuId = Long.parseLong(mtuIdString, 16);

                int type = Integer.parseInt(mtuNode.getAttribute("type"));
                if (logger.isDebugEnabled()) logger.debug("Processing gateway " + gatewayIdString + "- MTU: " + mtuIdString + " type:" + type);

                MTU mtu = gatewayService.getMTU(gateway, mtuId);
                if (mtu == null) {
                    logger.info("MTU with id " + mtuIdString + " not found for " + gateway + ". Adding the MTU to the gateway");
                    mtu = gatewayService.addMTU(gateway, mtuIdString, MTU.ordinalToMTUType(type), "MTU " + mtuIdString);
                }

                if (logger.isDebugEnabled()) logger.debug("Adding cumulative data to " + mtu + " for " + gateway);

                NodeList cumulativeDataList = mtuNode.getElementsByTagName("cumulative");
                int cumulativeDataListCount = cumulativeDataList.getLength();
                if (logger.isDebugEnabled()) logger.debug("Posting " + cumulativeDataListCount + " energy readings for " + mtu);

                EnergyData lastCumulativeValue = null;

                for (int c = 0; c < cumulativeDataListCount; c++) {


                    Element cumulativeNode = (Element) cumulativeDataList.item(c);


                    Integer timestamp = Integer.parseInt(cumulativeNode.getAttribute("timestamp"));
                    Double rate = Double.parseDouble(cumulativeNode.getAttribute("rate"));
                    Double energy = Double.parseDouble(cumulativeNode.getAttribute("watts"));


                    Double energyDifference = 0d;
                    Double minuteCost = 0d;

                    if (lastCumulativeValue == null) {
                        lastCumulativeValue = energyPostService.findByLastPost(gateway, mtu, timestamp);
                        if (logger.isDebugEnabled()) logger.debug("Last Post:" + lastCumulativeValue);
                    }

                    //Make sure that we are not dealing with the first entry for this gateway/mtu
                    if (lastCumulativeValue != null) {
                        Double wattDifference = energy - lastCumulativeValue.getEnergy();
                        energyDifference = wattDifference;
                        wattDifference = wattDifference / 1000; //Rate is on kWh

                        logger.debug("kW:" + wattDifference);
                        minuteCost = (wattDifference * rate);

                    }

                    //Calculate the number of minutes since the last post.
                    Integer minutes = 1;
                    if (lastCumulativeValue != null) {
                        minutes = (timestamp - lastCumulativeValue.getTimestamp()) / 60;
                    }
                    if (logger.isDebugEnabled()) logger.debug("Posting " + minutes + " minutes worth of data");

                    for (int min = 0; min < minutes; min++) {
                        int ts = timestamp - ((minutes - min) * 60);

                        if (logger.isDebugEnabled() && lastCumulativeValue != null) {
                            logger.debug("Posting " + ts + " between " + lastCumulativeValue.getTimestamp()  + " and " + timestamp);
                        }

                        lastCumulativeValue = energyPostService.postEnergyData(gateway, mtu, ts, energy, rate, minuteCost/(double)minutes, energyDifference/(double)minutes);

                        if (logger.isDebugEnabled()) logger.debug("Posted " + lastCumulativeValue);

                        //Make sure there is a cost data element for this gateway and timestamp.
                        if (costDataCache.get(timestamp) == null) {
                            CostData costData = new CostData();
                            costData.setGatewayId(gateway.getId());
                            costData.setTimestamp(timestamp);
                            costData.setMeterReadDay(meterReadDay);
                            costData.setMinCost(minCharge);
                            costData.setFixedCost(fixedCharge);
                            //Set the month and year based on the user's timezone.
                            //Do the timezone conversion
                            Calendar tzCalendar = EnergyPostUtil.getMeterMonth(meterReadDay, gatewayUser.getTimezone(), timestamp);
                            costData.setMeterReadYear(tzCalendar.get(Calendar.YEAR));
                            costData.setMeterReadMonth(tzCalendar.get(Calendar.MONTH) + 1);

                            //Add it to the hashmap as a unique timestamp.
                            costDataCache.put(timestamp, costData);
                        }
                    }
                }


            }


            //------------------POST ACCUMULATED COST DATA------------------------------------
            for (CostData costData : costDataCache.values()) {
                energyPostService.postCostData(costData);
            }


            //--------------------POST DEMAND CHARGES----------------------------------------------

            NodeList demandList = doc.getElementsByTagName("DEMAND");

            //If the packet has DEMAND data, enter it
            if (demandList != null && demandList.getLength() > 0) {
                logger.debug("Processing Demand Packet");
                Element demandNode = (Element) demandList.item(0);
                NodeList demandCostList = demandNode.getElementsByTagName("demandCost");
                int demandCostListCount = demandCostList.getLength();
                if (logger.isDebugEnabled()) logger.debug("Posting " + demandCostListCount + " demand cost entries");

                for (int c = 0; c < demandCostListCount; c++) {
                    Element demandCostNode = (Element) demandCostList.item(c);
                    try {
                        Integer timestamp = Integer.parseInt(demandCostNode.getAttribute("timestamp"));
                        Double cost = Double.parseDouble(demandCostNode.getAttribute("cost"));
                        Double peak = Double.parseDouble(demandCostNode.getAttribute("peak"));
                        energyPostService.postDemandCharge(gateway, timestamp, peak, cost);
                    } catch (Exception ex) {
                        logger.error("Exception parsing demand cost data:" + ex.getMessage(), ex);
                    }
                }
            }

            if (logger.isDebugEnabled()) logger.debug("Writing success response");
            response.setContentType("text/xml");
            PrintWriter out = response.getWriter();
            response.setStatus(200);
            out.write("SUCCESS");
            out.close();

        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("Error parsing inbound xml:" + ex.getMessage(), ex);
        }
    }
}
