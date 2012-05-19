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

package com.ted.aggredata.client.resources.lang;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;

public interface DashboardConstants extends Constants {

    public static final DashboardConstants INSTANCE = GWT.create(DashboardConstants.class);

    String month();

    String day();

    String hour();

    String minute();

    String hello();

    String profile();

    String admin();

    String logout();

    String energyData();

    String accountSettings();

    String accountGroups();

    String accountTEDS();

    String settingsTitle();

    String settingsInstructions();

    String groupsTitle();

    String groupsInstructions();

    String gatewaysTitle();

    String gatewaysInstructions();

    String monthTitle();

    String monthInstructions();


    String dayTitle();

    String dayInstructions();

    String hourTitle();

    String hourInstructions();

    String minuteTitle();

    String minuteInstructions();

    String systemUsers();

    String systemServer();

    String systemUsersTitle();

    String systemUsersInstructions();

    String systemServerTitle();

    String systemServerInstructions();

    //Settings panel
    String profileSettingsLastName();

    String profileSettingsFirstName();

    String profileSettingsCompanyName();

    String profileSettingsMiddleName();

    String profileSettingsAddress();
    
    String profileSettingsChangePass();
    
    String profileSettingsChangeEmail();

    String profileSettingsCity();

    String profileSettingState();

    String profileSettingsZip();

    String profileSettingsPhoneNumber();

    String profileSettingsTimezone();

    String minutes();


    String accountActivate();

    String activateTitle();

    String activateInstructions1();

    String activateInstructions2();

    String activateInstructions3();

    String activationLabel1();

    String activationLabel2();

    String activationInstructions4();

    String groupSettingsDescription();

    String yes();

    String no();

    String deleteGroupTitle();

    String deleteGroupMessage();

    String ok();

    String deleteGroupCantDeleteTitle();

    String deleteGroupCantDeleteText();

    String add();

    String delete();

    String yourGroups();

    String yourGateways();

    String deleteGatewayTitle();

    String deleteGatewayMessage();

    String groupDetails();

    String gatewayDetails();

    String gatewaySerialNumber();

    String gatewaySettingsDescription();

    String mtuSerialNumber();

    String mtuDescription();

    String mtuType();

    String deleteMTUTitle();

    String deleteMTUMessage();


    String sideBarGroupHeader();

    String sideBarGroupDetails();

    String sideBarDateHeader();

    String sideBarDateDetails();

    String startDate();

    String endDate();

    String sideBarTypeHeader();

    String sideBarTypeDetails();

    String graphMonthly();

    String graphDaily();

    String graphHourly();

    String graphPerminute();

    String graphCost();

    String graphEnergy();

    String graphCSVExport();

    String graphTotalCost();

    String graphTotalEnergy();

    String graphDateRange();

    String graphTo();

    String graphTypeCost();

    String graphTypeEnergy();

    String minuteDate();

    String minuteStartTime();

    String minuteEndTime();

    String minuteInterval();

    String export();

    String loading();
    
    String adminTitle();
    
    String adminInstructions();

    String yourUsers();
    
    String userDetails();
    
    String firstNameField();
    
    String lastNameField();

    String usernameField();

    //Change Email and Password Popups
    String changeEmail();

    String changePassword();

    String changePasswordMessage();

    String changeEmailMessage();
    
    String changeEmailVerification();
    
    String changePassVerification();
    
    //Delete User Popup
    
    String deleteUser();
    
    String deleteUserVerification();

    String graphShowTotal();

    String shareGroup();

    String groupShareHeader();


    String groupShareAddLabel();

    String groupShareAddButton();

    String close();

    String groupShareInstructions();

    String groupMembers();

    String remove();

    String graphShareMemberRemoveTitle();

    String graphShareMemberRemoveText();

    String graphShareAlreadyExists();

    String graphShareAlreadyExistsTitle();
    
    //Create user popup
    
    String cancel();
    
    String firstNameMessage();
    
    String lastNameMessage();
    
    String passwordMessage();
    
    String emailMessage();
    
    String createUser();
}
