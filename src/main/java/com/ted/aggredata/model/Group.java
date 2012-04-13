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

public class Group extends AggredataModel implements Serializable {


    public static enum Role {READONLY, MEMBER, OWNER};

    private String description;
    private Long ownerUserId;
    private Role role;

    private String custom1;
    private String custom2;
    private String custom3;
    private String custom4;
    private String custom5;


    public Long getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(Long ownerGroupId) {
        this.ownerUserId = ownerGroupId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustom1() {
        return custom1;
    }

    public void setCustom1(String custom1) {
        this.custom1 = custom1;
    }

    public String getCustom2() {
        return custom2;
    }

    public void setCustom2(String custom2) {
        this.custom2 = custom2;
    }

    public String getCustom3() {
        return custom3;
    }

    public void setCustom3(String custom3) {
        this.custom3 = custom3;
    }

    public String getCustom4() {
        return custom4;
    }

    public void setCustom4(String custom4) {
        this.custom4 = custom4;
    }

    public String getCustom5() {
        return custom5;
    }

    public void setCustom5(String custom5) {
        this.custom5 = custom5;
    }

    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("Group{");
        b.append("id:" + getId());
        b.append(", description:" + description);
        b.append(", ownerUserId:" + ownerUserId);
        b.append(", role:" + role);
        b.append("}");
        return b.toString();
    }



    @Override
    public int hashCode() {
        int result = description != null ? description.hashCode() : 0;
        result = 31 * result + (ownerUserId != null ? ownerUserId.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (custom1 != null ? custom1.hashCode() : 0);
        result = 31 * result + (custom2 != null ? custom2.hashCode() : 0);
        result = 31 * result + (custom3 != null ? custom3.hashCode() : 0);
        result = 31 * result + (custom4 != null ? custom4.hashCode() : 0);
        result = 31 * result + (custom5 != null ? custom5.hashCode() : 0);
        return result;
    }
}

