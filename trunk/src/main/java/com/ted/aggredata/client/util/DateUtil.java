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

package com.ted.aggredata.client.util;

import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.ted.aggredata.model.Enums;

import java.util.Date;

public class DateUtil {


    /**
     * Sets the date to the start date search range depending on type
     *
     * @param type
     * @param startDate
     * @return
     */
    public static Date adjustToSearchStart(Enums.HistoryType type, Date startDate) {
        if (type.equals(Enums.HistoryType.MONTHLY)) {
            Date theDate = new Date(startDate.getTime());
            theDate.setDate(1);
            return theDate;
        }

        if (type.equals(Enums.HistoryType.DAILY)) {
            Date theDate = new Date(startDate.getTime());
            theDate.setHours(0);
            return theDate;
        }



        if (type.equals(Enums.HistoryType.HOURLY)) {
            Date theDate = new Date(startDate.getTime());
            theDate.setHours(0);
            theDate.setMinutes(0);
            return theDate;
        }


        if (type.equals(Enums.HistoryType.MINUTE)) {
            Date theDate = new Date(startDate.getTime());
            return theDate;
        }

        return null;
    }

    /**
     * Sets the date to the end date search range depending on type
     *
     * @param type
     * @param endDate
     * @return
     */

    public static Date adjustToSearchEnd(Enums.HistoryType type, Date endDate) {

        if (type.equals(Enums.HistoryType.MONTHLY)) {
            Date theDate = new Date(endDate.getTime());
            theDate.setDate(1);
            CalendarUtil.addMonthsToDate(theDate, 1);
            return theDate;
        }

        if (type.equals(Enums.HistoryType.DAILY)) {
            Date theDate = new Date(endDate.getTime());
            theDate.setHours(0);
            CalendarUtil.addDaysToDate(theDate, 1);
            return theDate;
        }

        if (type.equals(Enums.HistoryType.HOURLY)) {

            Date theDate = new Date(endDate.getTime());
            theDate.setHours(0);
            theDate.setMinutes(0);
            CalendarUtil.addDaysToDate(theDate, 1);
            return theDate;
        }

        if (type.equals(Enums.HistoryType.MINUTE)) {
            Date theDate = new Date(endDate.getTime());
            return theDate;
        }


        return null;
    }


}
