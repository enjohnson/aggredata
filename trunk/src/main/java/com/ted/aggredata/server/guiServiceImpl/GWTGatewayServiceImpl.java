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

package com.ted.aggredata.server.guiServiceImpl;

import com.ted.aggredata.client.guiService.GWTGatewayService;
import com.ted.aggredata.model.Gateway;
import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.User;
import com.ted.aggredata.server.services.GatewayService;
import com.ted.aggredata.server.services.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class GWTGatewayServiceImpl extends SpringRemoteServiceServlet implements GWTGatewayService {

    @Autowired
    GroupService groupService;

    @Autowired
    GatewayService gatewayService;


    Logger logger = LoggerFactory.getLogger(GWTGatewayServiceImpl.class);

    private User getCurrentUser() {
        return (User) getThreadLocalRequest().getSession().getAttribute(UserSessionServiceImpl.USER_SESSION_KEY);
    }

    @Override
    public List<Gateway> findGateways() {
        User user = getCurrentUser();
        if (logger.isInfoEnabled()) logger.info("Looking up all gateways for " + user);
        return gatewayService.findByUser(user);

    }

    @Override
    public List<Gateway> findGateways(Group group) {
        User user = getCurrentUser();
        if (logger.isInfoEnabled()) logger.info("Looking up all gateways for " + group);
        return gatewayService.findByGroup(group);
    }

    @Override
    public void addGatewayToGroup(Group group, Gateway gateway) {
        User user = getCurrentUser();
        if (logger.isInfoEnabled()) logger.info(user + " adding " + gateway + " to " + group);


        groupService.addGatewayToGroup(user, group, gateway);
    }

    @Override
    public void removeGatewayFromGroup(Group group, Gateway gateway) {
        User user = getCurrentUser();
        if (logger.isInfoEnabled()) logger.info(user + " removing " + gateway + " from " + group);
        groupService.removeGatewayFromGroup(user, group, gateway);
    }
}
