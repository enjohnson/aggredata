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
    
    String userNameLabel();
    String passwordLabel();
    
    
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

    String profileSettingsLastName();

    String profileSettingsFirstName();

    String profileSettingsCompanyName();



    String gatewaysActivationInstructions1();

    String gatewaysActivationInstructions2();
}
