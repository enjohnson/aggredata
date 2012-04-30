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

import java.security.SecureRandom;

/**
 * Used to generate secure keys in the system.
 */
public class KeyGenerator {

    public static final char[] SECURITY_KEY_CHARS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1',
            '2', '3', '4', '5', '6', '7', '8', '9'};


    /**
     * Generates a random series of letters and numbers for the given length.
     *
     * @param length
     * @return
     */
    public static String generateSecurityKey(int length) {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[length];
        random.nextBytes(bytes);
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < length; i++) {
            key.append(SECURITY_KEY_CHARS[Math.abs(bytes[i]) % SECURITY_KEY_CHARS.length]);
        }
        return key.toString();
    }

}
