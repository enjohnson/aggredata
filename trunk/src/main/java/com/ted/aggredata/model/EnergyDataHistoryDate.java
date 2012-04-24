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
import java.util.Date;

/**
 * Date object that represents the energy history in the client's timezone. This is required because of the timezone shifted "group by" performed
 * on the database. If we return a Date object to different timezones the time will be shifted automatically. Epoch can't be returned because the max
 * epoch returned may be of different hour/min/secs depending on the interval being returned.
 */
public class EnergyDataHistoryDate implements Serializable, Comparable<EnergyDataHistoryDate> {

    public Integer year;
    public Integer month;
    public Integer date;
    public Integer hour;
    public Integer minute;

    public EnergyDataHistoryDate() {
    }

    public EnergyDataHistoryDate(int year, int month, int date, int hour, int minute) {
        this.year = year;
        this.month = month;
        this.date = date;
        this.hour = hour;
        this.minute = minute;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("EnergyDataHistoryDate[");
        sb.append("year=").append(year);
        sb.append(",month=").append(month);
        sb.append(",date=").append(date);
        sb.append(",hour=").append(hour);
        sb.append(",minute=").append(minute);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnergyDataHistoryDate that = (EnergyDataHistoryDate) o;

        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (hour != null ? !hour.equals(that.hour) : that.hour != null) return false;
        if (minute != null ? !minute.equals(that.minute) : that.minute != null) return false;
        if (month != null ? !month.equals(that.month) : that.month != null) return false;
        if (year != null ? !year.equals(that.year) : that.year != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = year != null ? year.hashCode() : 0;
        result = 31 * result + (month != null ? month.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (hour != null ? hour.hashCode() : 0);
        result = 31 * result + (minute != null ? minute.hashCode() : 0);
        return result;
    }

    /**
     * Converts this to a date object. This should only be called by the client in the localized timezone.
     * @return
     */
    public Date getTime(){
        //We use the deprecated methods below to support the GWT side calls.
        Date theDate = new Date();
        theDate.setYear(year-1900);
        theDate.setMonth(month-1);
        theDate.setDate(date);
        theDate.setHours(hour);
        theDate.setMinutes(minute);
        theDate.setSeconds(0);

        //Get rid of any extra milliseconds on this object.
        long t = theDate.getTime();
        t = t/1000;
        t = t*1000;

        return new Date(t);

    }


    @Override
    public int compareTo(EnergyDataHistoryDate o) {
        int yc = year.compareTo(o.year);
        if (yc != 0) return yc;
        int mc = month.compareTo(o.month);
        if (mc != 0) return mc;
        int dc = date.compareTo(o.date);
        if (dc != 0) return dc;
        int hc = hour.compareTo(o.hour);
        if (hc != 0) return hc;
        int minc = minute.compareTo(o.minute);
        return minc;
    }
}
