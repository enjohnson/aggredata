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

public class User extends AggredataModel implements Serializable {

    public static String ROLE_USER = "ROLE_USER";
    public static String ROLE_ADMIN = "ROLE_ADMIN";

    public static int STATE_WAITING_ACTIVATION = 0;
    public static int STATE_ENABLED = 1;
    public static int STATE_DISABLED = 99;



    private String username;
    private String role;
    private int accountState;
    private String activationKey;
    private int defaultGroupId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String companyName;
    private String address;
    private String city;
    private String addrState;
    private String zip;
    private String custom1;
    private String custom2;
    private String custom3;
    private String custom4;
    private String custom5;
    private String phoneNumber;
    private String timezone;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getZip() {
        return zip;
    }

    public void setAddrState(String addrState) {
        this.addrState = addrState;
    }

    public String getAddrState() {
        return addrState;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setCustom1(String custom1) {
        this.custom1 = custom1;
    }

    public String getCustom1() {
        return custom1;
    }

    public void setCustom2(String custom2) {
        this.custom2 = custom2;
    }

    public String getCustom2() {
        return custom2;
    }

    public void setCustom3(String custom3) {
        this.custom3 = custom3;
    }

    public String getCustom3() {
        return custom3;
    }

    public void setCustom4(String custom4) {
        this.custom4 = custom4;
    }

    public String getCustom4() {
        return custom4;
    }

    public void setCustom5(String custom5) {
        this.custom5 = custom5;
    }

    public String getCustom5() {
        return custom5;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public int getAccountState() {
        return accountState;
    }

    public void setAccountState(int accountState) {
        this.accountState = accountState;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("User{");
        b.append("id:" + getId());
        b.append(", username:" + username);
        b.append(", defaultGroupId:" + defaultGroupId);
        b.append(", role:" + role);
        b.append("}");
        return b.toString();
    }


    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + accountState;
        result = 31 * result + (activationKey != null ? activationKey.hashCode() : 0);
        result = 31 * result + defaultGroupId;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (middleName != null ? middleName.hashCode() : 0);
        result = 31 * result + (companyName != null ? companyName.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (addrState != null ? addrState.hashCode() : 0);
        result = 31 * result + (zip != null ? zip.hashCode() : 0);
        result = 31 * result + (custom1 != null ? custom1.hashCode() : 0);
        result = 31 * result + (custom2 != null ? custom2.hashCode() : 0);
        result = 31 * result + (custom3 != null ? custom3.hashCode() : 0);
        result = 31 * result + (custom4 != null ? custom4.hashCode() : 0);
        result = 31 * result + (custom5 != null ? custom5.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (timezone != null ? timezone.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
