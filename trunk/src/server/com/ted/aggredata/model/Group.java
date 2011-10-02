/*
 * Copyright (c) 2011. The Energy Detective. All Rights Reserved
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

package server.com.ted.aggredata.model;


import java.io.Serializable;

public class Group extends AggreDataModel implements Serializable {



    public static enum Role { READONLY, MEMBER, ADMIN };


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
}
