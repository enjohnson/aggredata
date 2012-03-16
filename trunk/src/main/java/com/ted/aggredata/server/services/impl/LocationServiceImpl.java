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

package com.ted.aggredata.server.services.impl;

import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.Location;
import com.ted.aggredata.model.User;
import com.ted.aggredata.server.dao.LocationDAO;
import com.ted.aggredata.server.services.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the GroupService interface
 */

@Transactional
public class LocationServiceImpl implements LocationService {

    @Autowired
    protected LocationDAO locationDAO;
    Logger logger = LoggerFactory.getLogger(getClass());

    public void addLocation(User user, Location location) {
        logger.info("User " + user + " + is adding " + location);
        location.setUserId(user.getId());
        locationDAO.create(location);
    }

    public void removeLocation(Location location) {
        logger.info("removing " + location);
        locationDAO.removeGroupLocations(location);
        locationDAO.delete(location);
    }

    public void updateLocation(Location location) {
        logger.info("Updating " + location);
        locationDAO.save(location);
    }

    public List<Location> getLocations(User user) {
        return locationDAO.getByUser(user);
    }

    public List<Location> getLocations(Group group) {
        return locationDAO.getByGroup(group);
    }

    public Location getLocation(User user, String description) {
        return locationDAO.getLocation(user, description);
    }

    public void addLocationToGroup(Location location, Group group) {
        logger.info("Adding " + location + " to " + group);
        locationDAO.addLocationToGroup(location, group);
    }

    public void removeLocationFromGroup(Location location, Group group) {
        logger.info("Removing " + location + " from " + group);
        locationDAO.removeLocationFromGroup(location, group);
    }
}
