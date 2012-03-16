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

public class Group extends AggreDataModel implements Serializable {


    public static enum Role {READONLY, MEMBER, ADMIN}

    ;

    private String description;
    private Long ownerUserId;
    private Role role;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (description != null ? !description.equals(group.description) : group.description != null) return false;
        if (ownerUserId != null ? !ownerUserId.equals(group.ownerUserId) : group.ownerUserId != null) return false;
        if (role != group.role) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = description != null ? description.hashCode() : 0;
        result = 31 * result + (ownerUserId != null ? ownerUserId.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }
}

