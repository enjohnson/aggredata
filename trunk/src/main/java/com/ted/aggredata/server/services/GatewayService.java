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

import com.ted.aggredata.model.Gateway;
import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.MTU;
import com.ted.aggredata.model.User;


public interface GatewayService {

    /**
     * Creates a new Gateway in the system
     *
     * @param Group
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


}
