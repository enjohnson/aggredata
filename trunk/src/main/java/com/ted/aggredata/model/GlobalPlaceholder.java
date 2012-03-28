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

package com.ted.aggredata.model;

import java.io.Serializable;
import java.util.List;

/**
 * This is a placeholder model used by the GUI to allow a single asynchronous call to load a 
 * multiple static runtime objects as login (saves overhead of multiple http requests
 * for each login.
 */
public class GlobalPlaceholder implements Serializable {
    User sessionUser;
    ServerInfo serverInfo;
    List<Gateway> gateways;

    public User getSessionUser() {
        return sessionUser;
    }

    public void setSessionUser(User sessionUser) {
        this.sessionUser = sessionUser;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public List<Gateway> getGateways() {
        return gateways;
    }

    public void setGateways(List<Gateway> gateways) {
        this.gateways = gateways;
    }

    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("GlobalPlaceholder{");
        b.append("User:" + sessionUser.toString());
        b.append(", Server:" + serverInfo.toString());
        b.append("}");
        return b.toString();
    }

}
