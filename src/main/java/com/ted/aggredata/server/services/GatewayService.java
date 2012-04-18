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

package com.ted.aggredata.server.services;

import com.ted.aggredata.model.*;

import java.util.List;


public interface GatewayService {

    /**
     * Creates a new Gateway in the system
     *
     * @param group
     * @param userAccount
     * @param serialNumber
     * @param description
     * @return
     */
    public Gateway createGateway(Group group, User userAccount, String serialNumber, String description);

    /**
     * Removes a gateway and all associated data from the system.
     *
     * @param gateway
     */
    public void deleteGateway(Gateway gateway);


    /**
     * Adds a new MTU to the system. If the serial number already exists it will be moved to a new gateway.
     *
     * @param gateway
     * @param mtuSerialNumber
     * @param type
     * @param description
     * @return
     */
    public MTU addMTU(Gateway gateway, String mtuSerialNumber, MTU.MTUType type, String description);


    /**
     * Disables a gateway
     *
     * @param gateway
     * @return
     */
    public Gateway disableGateWay(Gateway gateway);

    /**
     * Enables a gateway and generates a new security key for posting
     *
     * @param gateway
     * @return
     */
    public Gateway activateGateway(Gateway gateway);


    /**
     * *
     * Returns a list of gateways for the specified user
     *
     * @param user
     * @return
     */
    List<Gateway> findByUser(User user);


    /**
     * Returns a list of gateways for the specified group
     *
     * @param group
     * @return
     */
    List<Gateway> findByGroup(Group group);


    /**
     * Looks up a gateway by its unique id
     *
     * @param id
     * @return
     */
    Gateway getById(Long id);

    /**
     * Looks up the MTU for the specific gateway
     *
     * @param gateway
     * @param mtuId
     * @return
     */
    MTU getMTU(Gateway gateway, Long mtuId);

    /**
     * Creates a new EnergyData entry in the system
     *
     * @param gateway
     * @param mtu
     * @param timestamp
     * @param watts
     * @param rate
     * @param minCost
     * @param energyDifference
     * @return
     */
    EnergyData postEnergyData(Gateway gateway, MTU mtu, Integer timestamp, Double watts, Double rate, Double minCost, Double energyDifference);

    /**
     * Finds the most recently posted entry for the gateway/mtu
     *
     * @param gateway
     * @param mtu
     * @param timestamp
     * @return
     */
    EnergyData findByLastPost(Gateway gateway, MTU mtu, Integer timestamp);


    /**
     * Returns the number of registered Gateways for the specified user
     *
     * @param user
     * @return
     */
    public Integer countByUser(User user);

    /**
     * Updates the gateway information
     *
     * @param gateway
     */
    public void saveGateway(Gateway gateway);

    /**
     * Saves an MTU
     *
     * @param mtu
     */
    public void saveMTU(MTU mtu);

    /**
     * Returns a list of MTUs for the specified gateway
     *
     * @param gateway
     * @return
     */
    public List<MTU> findMTUByGateway(Gateway gateway);

    /**
     * Deletes the MTU and its history from the database
     *
     * @param gateway
     * @param mtu
     */
    public void deleteMTU(Gateway gateway, MTU mtu);
}



