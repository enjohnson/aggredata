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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Container bean that contains the results of a given query.
 */
public class EnergyDataHistoryQueryResult implements Serializable {

    List<Gateway> gatewayList = new ArrayList<Gateway>();
    List<EnergyDataHistory> netHistoryList = new ArrayList<EnergyDataHistory>();
    HashMap<Long, List<EnergyDataHistory>> gatewayHistoryList = new HashMap<Long, List<EnergyDataHistory>>();
    Group group;

    public EnergyDataHistoryQueryResult() {
    }

    public List<Gateway> getGatewayList() {
        return gatewayList;
    }

    public void setGatewayList(List<Gateway> gatewayList) {
        this.gatewayList = gatewayList;
    }

    public List<EnergyDataHistory> getNetHistoryList() {
        return netHistoryList;
    }

    public void setNetHistoryList(List<EnergyDataHistory> netHistoryList) {
        this.netHistoryList = netHistoryList;
    }

    public HashMap<Long, List<EnergyDataHistory>> getGatewayHistoryList() {
        return gatewayHistoryList;
    }

    public void setGatewayHistoryList(HashMap<Long, List<EnergyDataHistory>> gatewayHistoryList) {
        this.gatewayHistoryList = gatewayHistoryList;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
