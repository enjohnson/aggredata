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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * This servlet takes a specified EnergyDataHistoryQueryResult and converts it to a csv file.
 *
 */
public class HistoryExportServlet extends HttpServlet {

    static Logger logger = LoggerFactory.getLogger(HistoryExportServlet.class);
    static DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

    @Autowired
    UserService userService;

    @Autowired
    ServerInfo serverInfo;


    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }

    private static String cleanFileName(String groupName) {
        StringBuilder scrubbedFile = new StringBuilder();
        for (char c: groupName.toCharArray()) {
            if (Character.isLetterOrDigit(c)) scrubbedFile.append(c);
            else if (c=='-') scrubbedFile.append(c);
            else if (c=='.') scrubbedFile.append(c);
        }
        return scrubbedFile.toString();
    }

    private static SimpleDateFormat getDateFormat(Enums.HistoryType type) {
        if (type.equals(Enums.HistoryType.MINUTE)) return new SimpleDateFormat("MM/dd/yyyy hh:mm");
        if (type.equals(Enums.HistoryType.HOURLY)) return new SimpleDateFormat("MM/dd/yyyy hh:mm");
        if (type.equals(Enums.HistoryType.DAILY)) return new SimpleDateFormat("MM/dd/yyyy");
        if (type.equals(Enums.HistoryType.MONTHLY)) return new SimpleDateFormat("MM/yyyy");
        return new SimpleDateFormat("MM/dd/yyyy");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            logger.debug("History Export Servlet called");

            String key = request.getParameter("key");
            logger.debug("Checking session for object with key " + key);

            EnergyDataHistoryQueryResult result = (EnergyDataHistoryQueryResult) request.getSession().getAttribute(key);

            //Cehck to see if the object exists
            if (result == null) {
                if (logger.isWarnEnabled()) logger.warn("Results not found for key " + key);
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                PrintWriter out = response.getWriter();
                out.write("Gateway Not Found.");
                out.close();
                return;
            }


            StringBuilder fileNameBuilder = new StringBuilder();
            fileNameBuilder.append("export-").append(result.getGroup().getDescription()).append("-").append(result.getHistoryType()).append("-").append(result.getStartTime()).append(".csv");

            String fileName = cleanFileName(fileNameBuilder.toString().toLowerCase());
            if (logger.isDebugEnabled()) logger.debug("Using filename " + fileName);

            response.setHeader("Content-Type", "text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            PrintWriter writer = response.getWriter();


            //Create the header

            writer.append("Date");
            writer.append(",");
            writer.append("Total Energy");
            writer.append(",");
            writer.append("Total Cost");
            for (Gateway g: result.getGatewayList())
            {
                writer.append(",");
                writer.append(g.getDescription()).append(" (").append(Long.toHexString(g.getId()).toUpperCase()).append(") Power");
                writer.append(",");
                writer.append(g.getDescription()).append(" (").append(Long.toHexString(g.getId()).toUpperCase()).append(") Cost");
            }
            writer.append("\n");

            SimpleDateFormat dateFormat = getDateFormat(result.getHistoryType());
            DecimalFormat pwrFormat = new DecimalFormat("0.000");
            DecimalFormat cstFormat = new DecimalFormat("0.00");




            int index = 0;

            //Write each line of data.
            for (EnergyDataHistory history: result.getNetHistoryList())
            {

                writer.append(dateFormat.format(history.getHistoryDate().getTime()));
                writer.append(",");
                writer.append(pwrFormat.format(history.getEnergy()/1000.0));
                writer.append(",");
                writer.append(cstFormat.format(history.getCost()));


                for (Gateway g: result.getGatewayList())
                {
                    EnergyDataHistory gh = result.getGatewayHistoryList().get(g.getId()).get(index);

                    writer.append(",");
                    writer.append(pwrFormat.format(gh.getEnergy()/1000.0));
                    writer.append(",");
                    writer.append(cstFormat.format(gh.getCost()));

                }

                writer.append("\n");

                index++;
            }




            writer.flush();
            writer.close();

            logger.debug("Clearing out session object");
            request.getSession().removeAttribute(key);

        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("Error parsing inbound xml:" + ex.getMessage(), ex);
        }
    }
}
