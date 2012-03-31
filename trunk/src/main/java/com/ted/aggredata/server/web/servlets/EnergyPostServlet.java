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
import com.ted.aggredata.server.services.GatewayService;
import com.ted.aggredata.server.services.GroupService;
import com.ted.aggredata.server.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.w3c.dom.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


/****
 * This is the servlet that handles activation of a new gateway. It checks the security key against enabled user accounts
 * and adds the gateway and mtu's if one does not exists. If a gateway exists, it updates the gateway as active if it has been marked inactive.
 */
public class EnergyPostServlet extends HttpServlet {
    
    static Logger logger = LoggerFactory.getLogger(EnergyPostServlet.class);
    
    @Autowired
    UserService userService;
    
    @Autowired
    GatewayService gatewayService;

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
        logger.debug("EnergyPost POST received");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(request.getInputStream());


            //Validate security token
            NamedNodeMap ted5000Attributes = doc.getElementsByTagName("ted5000").item(0).getAttributes();


            String gatewayIdString = ted5000Attributes.getNamedItem("GWID").getNodeValue();
            String securityKey = ted5000Attributes.getNamedItem("auth").getNodeValue();
            if (logger.isDebugEnabled()) logger.debug("Received post from Gateway " + gatewayIdString + " with key " + securityKey);

            //Verify this gateway exists, has the correct key, and is enabled to receive posts.
            Gateway gateway = gatewayService.getById(Long.parseLong(gatewayIdString, 16));

            //Check to see if gateway exists
            if (gateway == null){
                if (logger.isWarnEnabled()) logger.warn("Gateway with id " + gatewayIdString + " not found in database");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                PrintWriter out = response.getWriter();
                out.write("Gateway Not Found.");
                out.close();
                return;
            }

            //Check security key
            if (!gateway.getSecurityKey().equals(securityKey)){
                if (logger.isWarnEnabled()) logger.warn("Attempted post to gateway " + gatewayIdString + " with an invalid authentication key");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                PrintWriter out = response.getWriter();
                out.write("Unauthorized.");
                out.close();
                return;
            }

            //Check to see if its enabled
            if (!gateway.getState()){
                if (logger.isWarnEnabled()) logger.warn("Gateway with id " + gatewayIdString + " is not enabled to receive posts.");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                PrintWriter out = response.getWriter();
                out.write("Unauthorized.");
                out.close();
                return;
            }

            if (logger.isInfoEnabled()) logger.info("gateway " + gatewayIdString + " is authenticated. recording data.");


            //Process each MTU
            NodeList mtuList =  doc.getElementsByTagName("MTU");
            int mtuCount = mtuList.getLength();
            if (logger.isDebugEnabled()) logger.debug("Gateway " + gatewayIdString + "- MTU Count: " + mtuCount);
            for (int i=0; i < mtuCount; i++)
            {
                Element mtuNode = (Element)mtuList.item(i);
                String mtuIdString = mtuNode.getAttribute("ID");
                Long mtuId = Long.parseLong(mtuIdString, 16);

                int type = Integer.parseInt(mtuNode.getAttribute("type"));
                if (logger.isDebugEnabled()) logger.debug("Processing gateway " + gatewayIdString + "- MTU: " + mtuIdString + " type:" + type);

                MTU mtu = gatewayService.getMTU(gateway, mtuId);
                if (mtu == null){
                    logger.info("MTU with id " + mtuIdString + " not found for " + gateway + ". Adding the MTU to the gateway");
                    mtu = gatewayService.addMTU(gateway, mtuIdString, MTU.ordinalToMTUType(type), "MTU " + mtuIdString);
                }

                if (logger.isDebugEnabled()) logger.debug("Adding cumulative data to " + mtu + " for " + gateway);

                NodeList cumulativeDataList = mtuNode.getElementsByTagName("cumulative");
                int cumulativeDataListCount = cumulativeDataList.getLength();
                if (logger.isDebugEnabled()) logger.debug("Posting " + cumulativeDataListCount + " energy readings for " + mtu);

                EnergyData lastCumulativeValue = null;

                for (int c=0; c < cumulativeDataListCount; c++)
                {
                    Element cumulativeNode = (Element) cumulativeDataList.item(c);
                    Integer timestamp = Integer.parseInt(cumulativeNode.getAttribute("timestamp"));
                    Double rate = Double.parseDouble(cumulativeNode.getAttribute("rate"));
                    Double energy = Double.parseDouble(cumulativeNode.getAttribute("watts"));
                    Double minuteCost = 0d;

                    if (lastCumulativeValue== null) {
                        lastCumulativeValue = gatewayService.findByLastPost(gateway, mtu, timestamp);
                        if (logger.isDebugEnabled()) logger.debug("Last Post:" + lastCumulativeValue);
                    }

                    //Make sure that we are not dealing with the first entry for this gateway/mtu
                    if (lastCumulativeValue != null){
                        Double wattDifference = energy - lastCumulativeValue.getEnergy();

                        wattDifference = wattDifference / 1000; //Rate is on kWh

                        logger.debug("kW:" + wattDifference);
                        minuteCost = (wattDifference * rate);

                    }

                    lastCumulativeValue = gatewayService.postEnergyData(gateway, mtu, timestamp, energy, rate, minuteCost);
                    if (logger.isDebugEnabled()) logger.debug("Posted " + lastCumulativeValue);
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
