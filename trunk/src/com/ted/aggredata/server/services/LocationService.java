/*
 * Copyright (c) 2011. The Energy Detective. All Rights Reserved
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

package com.ted.aggredata.server.services;

import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.Location;
import com.ted.aggredata.model.User;

import java.util.List;

/**
 * Public interface to the user management service
 */
public interface LocationService {

    /**
     * Adds a new location to the system
     *
     * @param user
     * @param location
     */
    public void addLocation(User user, Location location);

    /**
     * Removes a location from the system
     *
     * @param location
     */
    public void removeLocation(Location location);


    /**
     * Updates an existing location
     *
     * @param location
     */
    public void updateLocation(Location location);


    /**
     * Retrieves a list of locations for a given user
     *
     * @param user
     * @return
     */
    public List<Location> getLocations(User user);

    /**
     * Retrieves a list of locations for a given group
     *
     * @param group
     * @return
     */
    public List<Location> getLocations(Group group);

    /**
     * Returns the location w/ the given description for the specified user
     *
     * @param user
     * @param description
     * @return
     */
    public Location getLocation(User user, String description);


    /**
     * Adds a location to the given group
     *
     * @param location
     * @param group
     */
    public void addLocationToGroup(Location location, Group group);


    /**
     * Removes a location from the given group
     *
     * @param location
     * @param group
     */
    public void removeLocationFromGroup(Location location, Group group);


}
