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

package com.ted.aggredata.client.guiService;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ted.aggredata.model.GlobalPlaceholder;
import com.ted.aggredata.model.User;


/**
 * Client Side Interface for the service to check user sessions.  This is intended for users BEFORE they are authenticated.
 */

@RemoteServiceRelativePath("UserSessionService")
public interface UserSessionService extends RemoteService {

    /**
     * Creates a user session
     *
     * @param username
     * @param password
     * @return
     */
    public GlobalPlaceholder logon(String username, String password);

    /**
     * Logs the current user out of their session
     */
    public void logoff();

    /**
     * Checks to see if the user is currently in a valid/logged in session.
     *
     * @return
     */
    public GlobalPlaceholder getUserFromSession();

    /**
     * Resets the password for the given user
     * @param username
     */
    public void resetPassword(String username);


    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_FAIL_CAPTCHA = 1;
    public static final int RESULT_DUPE_USERNAME = 2;
    public static final int RESULT_REGISTRATION_DISABLED = 3;
    public static final int RESULT_FAILED_AUTHENTICATION = 3;

    /***
     * Adds a user to the system. The result code is any errors in registration that have occured.
     * @param captchaString
     * @param user
     * @return
     */
    public int validateCaptcha(String captchaString, String username, String password, User user);


}
