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

package com.ted.aggredata.client.guiService;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ted.aggredata.model.Gateway;
import com.ted.aggredata.model.Group;
import com.ted.aggredata.model.MTU;

import java.util.List;

/**
 * Client Side Interface for the service to check user sessions.
 */
@RemoteServiceRelativePath("GWTGatewayService")
public interface GWTGatewayService extends RemoteService {

    /**
     * Finds gateways for the  user
     * @return
     */
    public List<Gateway> findGateways();

    /**
     * Finds gateways for the  group
     * @return
     */
    public List<Gateway> findGateways(Group group);

    public List<MTU> findMTU(Gateway gateway);

    public void addGatewayToGroup(Group group, Gateway gateway);

    public void removeGatewayFromGroup(Group group, Gateway gateway);

    public void deleteGateway(Gateway gateway);

    public Gateway saveGateway(Gateway gateway);

    public MTU saveMTU(Gateway gateway, MTU mtu);

    public void deleteMTU(Gateway gateway, MTU mtu);

}
