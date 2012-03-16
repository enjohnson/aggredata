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
 * Represents a user enrolled in the aggredata system.
 */

public class User extends AggreDataModel implements Serializable {

    public static enum Role {MEMBER, ADMIN}

    ;

    private String username;
    private String password;
    private Role role;
    private boolean state;
    private String activationKey;
    private int defaultGroupId;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean checkPassword(String password) {
        return this.password.trim().equals(password.trim());
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean checActivationKey(String activationKey) {
        return activationKey.trim().equals(activationKey);
    }

    public String getActivationKey() {
        return this.activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public int getDefaultGroupId() {
        return defaultGroupId;
    }

    public void setDefaultGroupId(int defaultGroupId) {
        this.defaultGroupId = defaultGroupId;
    }


    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("User{");
        b.append("id:" + getId());
        b.append(", username:" + username);
        b.append(", defaultGroupId:" + defaultGroupId);
        b.append(", role:" + role);
        b.append("}");
        return b.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (defaultGroupId != user.defaultGroupId) return false;
        if (state != user.state) return false;
        if (activationKey != null ? !activationKey.equals(user.activationKey) : user.activationKey != null)
            return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (role != user.role) return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (state ? 1 : 0);
        result = 31 * result + (activationKey != null ? activationKey.hashCode() : 0);
        result = 31 * result + defaultGroupId;
        return result;
    }
}
