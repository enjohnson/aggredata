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
 * Client Side Interface for the service to check user sessions.
 */
@RemoteServiceRelativePath("GWTUserService")
public interface GWTUserService extends RemoteService {

    public User saveUser(User user);
    
    public User changePassword(User user, String Password);
    public User changeUsername(User user, String username);

}