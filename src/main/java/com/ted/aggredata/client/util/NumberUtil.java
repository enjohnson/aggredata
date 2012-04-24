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

/**
 * Miscellaneous numeric utilities
 */
public class NumberUtil {

    /**
     * Rounds to the specified decimal place
     * @param value
     * @param decimalPlaces
     * @return
     */
    public static double round(double value, int decimalPlaces) {
        double multiplier = 1;
        for (int i=0; i < decimalPlaces; i++)
        {
            multiplier = multiplier * 10;
        }

        double roundedValue = value * multiplier;
        roundedValue = Math.round(roundedValue);
        roundedValue = roundedValue / multiplier;
        return roundedValue;

    }

}
