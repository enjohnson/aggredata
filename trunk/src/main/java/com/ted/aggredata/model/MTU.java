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
 * Represents a single unique MTU
 */
public class MTU extends AggredataModel implements Serializable {

    public static enum MTUType {LOAD, GENERATION, ADJUSTED_NET, STAND_ALONE, STAND_ALONE_NET}

    ;

    Long gatewayId;
    MTUType type;
   String description;


    /**
     * ID of the gateway this MTU is assigned to
     *
     * @return
     */
    public Long getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(Long gatewayId) {
        this.gatewayId = gatewayId;
    }

    /**
     * The configuration of the MTU (LOAD, SOLAR, NET, STAND ALONE)
     *
     * @return
     */
    public MTUType getType() {
        return type;
    }

    public void setType(MTUType type) {
        this.type = type;
    }


    /**
     * User description of the MTU (e.g. "main panel", "guest house")
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("MTUD{");
        b.append("id:" + getId());
        b.append(", gaewayId:" + gatewayId);
        b.append(", type:" + type);
        b.append(", description:" + description);
        b.append("}");
        return b.toString();
    }
}
