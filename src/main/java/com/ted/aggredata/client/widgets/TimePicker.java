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

package com.ted.aggredata.client.widgets;

import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ListBox;


/**
 * Listbox that allows the time to be picked via a dropdown using the specified interval.
 */
public class TimePicker extends ListBox
{
    private static int TOTAL_MINUTES =  1440;
    final Integer interval;


    /**
     * Creates a drop down time picker.
     * @param interval number of minutes between each entry (15 = 15 minutes .e.g. 12:00, 12:15, 12:30, etc.
     */
    public @UiConstructor
    TimePicker(int interval)
    {
        super(populateTimes(interval));
        this.interval = interval;
        setSelectedIndex(0);
    }

    /**
     * Converts the hour/minute to an <option> tag.
     * @param hour
     * @param minute
     * @return
     */
    private static String getOption(int hour, int minute){

        boolean pm = (hour >= 12) && (hour < 24);
        if (pm && hour > 12) hour -= 12;

        String value = Integer.toString(((hour * 60) + minute));

        StringBuilder nameStringBuilder = new StringBuilder();
        nameStringBuilder.append(Integer.toString(hour)).append(":");
        if (minute < 10) nameStringBuilder.append("0");
        nameStringBuilder.append(minute).append(" ");
        if (pm) nameStringBuilder.append(" PM");
        else nameStringBuilder.append(" AM");


        //Create the option
        StringBuilder optionStringBuilder = new StringBuilder();
        optionStringBuilder.append("<option value=\"").append(value).append("\">");

        if (hour == 0 && minute == 0) optionStringBuilder.append("Midnight");
        else if (hour >= 24) {
            optionStringBuilder.append("Next Midnight");
        } else {
            optionStringBuilder.append(nameStringBuilder.toString());
        }


        optionStringBuilder.append("</option>");
        return optionStringBuilder.toString();


    }

    private static Element populateTimes(int interval)
    {
        //Calculate the number of entries.
        int numberOfEntries = TOTAL_MINUTES/interval;
        if ((interval * numberOfEntries) != TOTAL_MINUTES) numberOfEntries++;

        //Build a string for the element.
        StringBuilder elementString = new StringBuilder();
        elementString.append("<select>");

        int hour = 0;
        int minute = 0;

        //Add the first entry
        elementString.append(getOption(hour, minute));

        for (int m=0; m < numberOfEntries; m++)
        {
            minute += interval;
            if (minute >= 60) {
                hour++;
                minute = minute - 60;
            }
            elementString.append(getOption(hour, minute));
        }
        elementString.append("</select>");

        //Create the element
        Element div = DOM.createDiv();
        div.setInnerHTML(elementString.toString());
        Element select = div.getFirstChildElement();
        select.removeFromParent();
        return select;
    }

    public void setSelectedItem(int hour, int minute) {
        int value = ((hour * 60) + minute);
        int index = value / interval;
        this.setSelectedIndex(index);
    }

    public int getSelectedHour(){
        int value = getSelectedIndex() * interval;
        return value / 60;
    }

    public int getSelectedMinute() {
        int value = getSelectedIndex() * interval;
        return value % 60;
    }


}
