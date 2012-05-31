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

package com.ted.aggredata.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utilities for energy posting
 */
public class EnergyPostUtil {

    static Logger logger = LoggerFactory.getLogger(EnergyPostUtil.class);

    /**
     * Converts the time for the specified timezone.
     * @param timezone
     * @param gatewayTime
     * @return
     */
    public static Calendar getTime(String timezone, long gatewayTime){
        if (logger.isDebugEnabled()) logger.debug("Getting calendar for " + timezone + ":" + gatewayTime);
        TimeZone userTimezone  = TimeZone.getTimeZone(timezone);
        Calendar tzCalendar = Calendar.getInstance(userTimezone);
        tzCalendar.setTimeInMillis(gatewayTime * 1000);
        return tzCalendar;
    }

}
