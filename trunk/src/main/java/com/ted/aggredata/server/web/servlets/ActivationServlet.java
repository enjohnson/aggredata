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

import com.ted.aggredata.model.Gateway;
import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.ServerInfo;
import com.ted.aggredata.model.User;
import com.ted.aggredata.server.services.GatewayService;
import com.ted.aggredata.server.services.GroupService;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


/****
 * This is the servlet that handles activation of a new gateway. It checks the security key against enabled user accounts
 * and adds the gateway and mtu's if one does not exists. If a gateway exists, it updates the gateway as active if it has been marked inactive.
 */
public class ActivationServlet extends HttpServlet {
    
    static Logger logger = LoggerFactory.getLogger(ActivationServlet.class);
    
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
        logger.debug("ActivationServlet POST received");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(request.getInputStream());
            String gatewayIdString = doc.getElementsByTagName("Gateway").item(0).getFirstChild().getNodeValue();
            String unique = doc.getElementsByTagName("Unique").item(0).getFirstChild().getNodeValue();
            if (logger.isDebugEnabled()) logger.info("Received activation request for " + gatewayIdString + " with key " + unique);

            User user = userService.getUserByActivationKey(unique);
            if (user==null){
                if (logger.isWarnEnabled()) logger.warn("No user found with activation key " + unique);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }


            Long gatewayId = Long.parseLong(gatewayIdString, 16);
            Gateway gateway = gatewayService.getById(gatewayId);
            if (gateway != null){
                if (gateway.getUserAccountId() != user.getId()){
                    if (logger.isWarnEnabled()) logger.warn("Attempt to activate a gateway owned by another user: " + gateway);
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                if (logger.isDebugEnabled()) logger.debug("Reactivating gateway " + gatewayIdString + " for " + user);
            } else 
            {
                if (logger.isDebugEnabled()) logger.debug("Creating gateway " + gatewayIdString + " for " + user);
                List<Group> groupList = groupService.getByUser(user);

                if (groupList.size() == 0){
                    logger.info(user + " does not have any groups. Creating a default group.");
                    Group group = groupService.createGroup(user, "Default Group");
                    groupList.add(group);
                }

                gateway = gatewayService.createGateway(groupList.get(0), user, gatewayIdString, "Gateway " + gatewayIdString);
            }

            if (logger.isInfoEnabled()) logger.info("Activating gateway " + gateway +" for " + user);
            gateway = gatewayService.activateGateway(gateway);

            
            logger.debug("Returning activation response");
            
            StringBuilder responseXML = new StringBuilder();
            responseXML.append("<ted500ActivationResponse>\r\n");
            responseXML.append("<PostServer>").append(serverInfo.getServerName()).append("</PostServer>");
            responseXML.append("<UseSSL>").append(serverInfo.isUseHttps()).append("</UseSSL>");
            responseXML.append("<PostPort>").append(serverInfo.getServerPort()).append("</PostPort>");
            responseXML.append("<PostURL>/aggredata/postData</PostURL>");
            responseXML.append("<AuthToken>").append(gateway.getSecurityKey()).append("</AuthToken>");
            responseXML.append("<PostRate>").append(serverInfo.getPostDelay()).append("</PostRate>");
            responseXML.append("<HighPrec>").append((serverInfo.isHighPrecision()?"T":"F")).append("</HighPrec>");
            responseXML.append("</ted500ActivationResponse>\r\n");

            if (logger.isDebugEnabled()) logger.debug("Writing response xml:" + responseXML);
            response.setContentType("text/xml");
            PrintWriter out = response.getWriter();
            response.setStatus(200);
            out.write(responseXML.toString());
            out.close();

        } catch (Exception ex) {
            logger.error("Error parsing inbound xml:" + ex.getMessage(), ex);
        }
    }
}
