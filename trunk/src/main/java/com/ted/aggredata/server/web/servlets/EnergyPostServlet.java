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
import com.ted.aggredata.server.model.EnergyPostRecord;
import com.ted.aggredata.server.services.EnergyPostService;
import com.ted.aggredata.server.services.GatewayService;

import com.ted.aggredata.server.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.w3c.dom.Document;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * *
 * This is the servlet that handles activation of a new gateway. It checks the security key against enabled user accounts
 * and adds the gateway and mtu's if one does not exists. If a gateway exists, it updates the gateway as active if it has been marked inactive.
 */
public class EnergyPostServlet extends HttpServlet {

    static Logger logger = LoggerFactory.getLogger(EnergyPostServlet.class);
    static Logger xmlLogger = LoggerFactory.getLogger("raw.xml.out");

    static DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

    @Autowired
    UserService userService;

    @Autowired
    GatewayService gatewayService;

    @Autowired
    EnergyPostService energyPostService;

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



            EnergyPostRecord energyPostRecord = new EnergyPostRecord(doc);

            if (logger.isDebugEnabled())
                logger.debug("Received post from Gateway " + energyPostRecord.getGatewayId() + " with key " + energyPostRecord.getAuthToken());

            //Verify this gateway exists, has the correct key, and is enabled to receive posts.
            Gateway gateway = gatewayService.getById(Long.parseLong(energyPostRecord.getGatewayId(), 16));

            if (xmlLogger.isTraceEnabled()) {
                try {
                    DOMSource domSource = new DOMSource(doc);
                    StringWriter writer = new StringWriter();
                    StreamResult result = new StreamResult(writer);
                    TransformerFactory tf = TransformerFactory.newInstance();
                    Transformer transformer = tf.newTransformer();
                    transformer.transform(domSource, result);
                    xmlLogger.trace("XML IN String format is: \n" + writer.toString());
                    writer.close();
                } catch (Exception ex) {
                    xmlLogger.error("Error printing XML");
                }
            }

            //Check to see if gateway exists
            if (gateway == null) {
                if (logger.isWarnEnabled())
                    logger.warn("Gateway with id " + energyPostRecord.getGatewayId() + " not found in database");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                PrintWriter out = response.getWriter();
                out.write("Gateway Not Found.");
                out.close();
                return;
            }

            if (logger.isDebugEnabled()) logger.debug("Found gateway: " + gateway);

            //Check security key
            if (!gateway.getSecurityKey().equals(energyPostRecord.getAuthToken())) {
                if (logger.isWarnEnabled())
                    logger.warn("Attempted post to gateway " + energyPostRecord.getGatewayId() + " with an invalid authentication key");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                PrintWriter out = response.getWriter();
                out.write("Unauthorized.");
                out.close();
                return;
            }

            //Check to see if its enabled
            if (!gateway.getState()) {
                if (logger.isWarnEnabled())
                    logger.warn("Gateway with id " + energyPostRecord.getGatewayId() + " is not enabled to receive posts.");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                PrintWriter out = response.getWriter();
                out.write("Unauthorized.");
                out.close();
                return;
            }

            //Check the user
            User gatewayUser = userService.findUser(gateway.getUserAccountId());
            if (gatewayUser == null || gatewayUser.getAccountState() != User.STATE_ENABLED) {
                if (logger.isWarnEnabled())
                    logger.warn("Attempted post to gateway " + energyPostRecord.getGatewayId() + " with an invalid or disabled user: " + gatewayUser);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                PrintWriter out = response.getWriter();
                out.write("Unauthorized.");
                out.close();
                return;
            }


            if (logger.isInfoEnabled())
                logger.info("gateway " + energyPostRecord.getGatewayId() + " is authenticated. recording data.");

            energyPostService.postEnergyData(gatewayUser, gateway, energyPostRecord);
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
