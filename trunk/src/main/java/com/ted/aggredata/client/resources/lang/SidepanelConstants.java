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

public interface SidepanelConstants extends Constants {

    public static final SidepanelConstants INSTANCE = GWT.create(SidepanelConstants.class);

    String settingInstructions();

    String groupInstructions();

    String gatewaysInstructions();

    String activateInstructions();

    String settingInstructions2();

    String groupInstructions2();

    String gatewaysInstructions2();

    String activateInstructions2();

    String settingInstructions3();

    String groupInstructions3();

    String gatewaysInstructions3();

    String activateInstructions3();
}
