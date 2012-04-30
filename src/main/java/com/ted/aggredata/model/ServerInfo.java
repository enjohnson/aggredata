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

/**
 * This is a model that contains the server runtime properties (mostly used by the
 * client as the Spring properties file can be referenced directly serverside
 */
public class ServerInfo implements Serializable {

    private String serverName;
    private int serverPort;
    private boolean useHttps;
    private int postDelay;
    private boolean highPrecision;
    private String timezone;
    private boolean allowRegistration;
    private boolean allowPasswordReset;
    private String fromAddress;
    private String adminEmailAddress;



    public ServerInfo() {
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


    public boolean isUseHttps() {
        return useHttps;
    }

    public void setUseHttps(boolean useHttps) {
        this.useHttps = useHttps;
    }

    public int getPostDelay() {
        return postDelay;
    }

    public void setPostDelay(int postDelay) {
        this.postDelay = postDelay;
    }

    public boolean isHighPrecision() {
        return highPrecision;
    }

    public void setHighPrecision(boolean highPrecision) {
        this.highPrecision = highPrecision;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }


    public boolean isAllowRegistration() {
        return allowRegistration;
    }

    public void setAllowRegistration(boolean allowRegistration) {
        this.allowRegistration = allowRegistration;
    }

    public boolean isAllowPasswordReset() {
        return allowPasswordReset;
    }

    public void setAllowPasswordReset(boolean allowPasswordReset) {
        this.allowPasswordReset = allowPasswordReset;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getAdminEmailAddress() {
        return adminEmailAddress;
    }

    public void setAdminEmailAddress(String adminEmailAddress) {
        this.adminEmailAddress = adminEmailAddress;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("ServerInfo{");
        b.append("ServerName:" + getServerName());
        b.append(", ServerPort:" + getServerPort());
        b.append(", Use Https:" + isUseHttps());
        b.append(", PostDelay:" + getPostDelay());
        b.append(", HighPrecision:" + isHighPrecision());
        b.append(", timezone:" + getTimezone());
        b.append("}");
        return b.toString();
    }


    public String getActivationUrl() {
        StringBuilder stringBuilder = new StringBuilder();
        if (isUseHttps()) {
            stringBuilder.append("https://");
        } else {
            stringBuilder.append("http://");
        }

        stringBuilder.append(getServerName());

        //Append th eport if its a non-standard port
        if ((isUseHttps() && getServerPort() != 443) || (!isUseHttps() && getServerPort() != 80)) {
            stringBuilder.append(":").append(getServerPort());
        }

        //TODO: See if we can make this a global constant somehwere else.
        stringBuilder.append("/aggredata/activate");

        return stringBuilder.toString();

    }
}
