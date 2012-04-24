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
 * Miscellaneous String utilities
 */
public class StringUtil {

    public static String toTitleCase(String string){
        String result = "";
        for (int i = 0; i < string.length(); i++){
            String next = string.substring(i, i + 1);
            if (i == 0){
                result += next.toUpperCase();
            } else {
                result += next.toLowerCase();
            }
        }
        return result;
    }

}
