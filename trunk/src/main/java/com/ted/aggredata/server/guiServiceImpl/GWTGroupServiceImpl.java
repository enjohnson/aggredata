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

import com.ted.aggredata.client.guiService.GWTGroupService;
import com.ted.aggredata.model.*;
import com.ted.aggredata.server.services.GroupService;
import com.ted.aggredata.server.services.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class GWTGroupServiceImpl extends SpringRemoteServiceServlet implements GWTGroupService {

    @Autowired
    GroupService groupService;

    @Autowired
    HistoryService historyService;

    Logger logger = LoggerFactory.getLogger(GWTGroupServiceImpl.class);

    private User getCurrentUser() {
        return (User) getThreadLocalRequest().getSession().getAttribute(UserSessionServiceImpl.USER_SESSION_KEY);
    }

    @Override
    public List<Group> findGroups() {
        User user = getCurrentUser();
        if (logger.isInfoEnabled()) logger.info("Looking up groups for " + user);
        return groupService.getByUser(user);
    }

    @Override
    public Group createGroup(String description) {
        User user = getCurrentUser();
        if (logger.isInfoEnabled()) logger.info("creating group" + description + " for " + user);
        return groupService.createGroup(user, description);
    }

    @Override
    public Group saveGroup(Group group) {
        User user = getCurrentUser();
        if (user.getId().equals(group.getOwnerUserId())) {
            if (logger.isInfoEnabled()) logger.info("saving group " + group);
            return groupService.saveGroup(group);
        }
        logger.warn("Security violation. " + user + " attempted to save " + group);
        return group;
    }

    @Override
    public void deleteGroup(Group group) {
        User user = getCurrentUser();
        if (user.getId().equals(group.getOwnerUserId())) {
            if (logger.isInfoEnabled()) logger.info("deleting group " + group);
            groupService.deleteGroup(group);
        } else {
            logger.warn("Security violation. " + user + " attempted to delete " + group);
        }
    }

    @Override
    public EnergyDataHistoryQueryResult getHistory(Enums.HistoryType historyType, Group group, long startTime, long endTime, int interval) {
        User user = getCurrentUser();
        if (user.getId().equals(group.getOwnerUserId())) {

            if (logger.isInfoEnabled()) logger.info("retrieving " + historyType +" history for  " + group + " " + startTime+"-"+endTime);
            EnergyDataHistoryQueryResult result = historyService.getHistory(historyType, user, group, startTime, endTime, interval);
            return result;
        }
        logger.warn("Security violation. " + user + " attempted to retrieve history for  " + group);
        return new EnergyDataHistoryQueryResult();
    }

    @Override
    public String exportHistory(Enums.HistoryType historyType, Group group, long startTime, long endTime, int interval) {

        //Rerun the history (in case anything has changed since last request)
        EnergyDataHistoryQueryResult result = getHistory(historyType, group, startTime, endTime, interval);

        //Generate a random key for this history (to prevent XSS access to last history being run)
        SecureRandom random = new SecureRandom();
        String key = new BigInteger(130, random).toString(32);



        //Place the results in session (temporary, CSV servlet will clear this and the key out).
        this.getThreadLocalRequest().getSession().setAttribute(key, result);

        //Return the key
        return key;


    }


}
