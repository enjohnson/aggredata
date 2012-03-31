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
    public static String ROLE_ADMIN= "ROLE_ADMIN";

    

    private String username;
    private String role;
    private boolean state;
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getFirstName(){
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMiddleName(){
        return middleName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName(){
        return companyName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity(){
        return city;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getZip(){
        return zip;
    }

    public void setAddrState(String addrState) {
        this.addrState = addrState;
    }

    public String getAddrState(){
        return addrState;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress(){
        return address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public void setCustom1(String custom1) {
        this.custom1 = custom1;
    }

    public String getCustom1(){
        return custom1;
    }

    public void setCustom2(String custom2) {
        this.custom2 = custom2;
    }

    public String getCustom2(){
        return custom2;
    }

    public void setCustom3(String custom3) {
        this.custom3 = custom3;
    }

    public String getCustom3(){
        return custom3;
    }

    public void setCustom4(String custom4) {
        this.custom4 = custom4;
    }

    public String getCustom4(){
        return custom4;
    }

    public void setCustom5(String custom5) {
        this.custom5 = custom5;
    }

    public String getCustom5(){
        return custom5;
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
        if (role != user.role) return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (state ? 1 : 0);
        result = 31 * result + (activationKey != null ? activationKey.hashCode() : 0);
        result = 31 * result + defaultGroupId;
        return result;
    }
}