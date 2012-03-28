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

/***
 * This is a model that contains the server runtime properties (mostly used by the
 * client as the Spring properties file can be referenced directly serverside
 */
public class ServerInfo implements Serializable {
    
    private String serverName;
    private int serverPort;
    
    public ServerInfo()
    {
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }


    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("ServerInfo{");
        b.append("ServerName:" + getServerName());
        b.append(", ServerPort:" + getServerPort());
        b.append("}");
        return b.toString();
    }

}
