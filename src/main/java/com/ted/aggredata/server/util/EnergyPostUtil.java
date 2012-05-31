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
    public static Calendar getMeterMonth(int meterReadDate, String timezone, long gatewayTime){
        if (logger.isDebugEnabled()) logger.debug("Getting calendar for " + timezone + ":" + gatewayTime);
        TimeZone userTimezone  = TimeZone.getTimeZone(timezone);
        Calendar cal = Calendar.getInstance(userTimezone);
        cal.setTimeInMillis(gatewayTime * 1000);

        //Adjust the MRD date if the MRD falls after the last day of the month
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int mrd = meterReadDate;
        if (mrd > days) {
            mrd  = days;
        }

        int theDay = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, 1); //Set it back to the first so we don't flip months by accident after adjusting the calendar.

        //If the day in question is before the meter read date, then the billing month is actually the previous month
        if (theDay < mrd) cal.add(Calendar.MONTH, -1);

        return cal;
    }

}
