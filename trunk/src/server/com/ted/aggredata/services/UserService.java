/*
 * Copyright (c) 2011. The Energy Detective. All Rights Reserved
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

package server.com.ted.aggredata.services;

import client.com.ted.aggredata.model.User;

/**
 * Public interface to the user management service
 */
public interface UserService{
    /**
     * Creates a new entity in the database
     * @param entity
     * @return
     */
    public User createUser(User entity);


    /**
     * Deletes a user from the database
     * @param entity
     * @return
     */
    public void deleteUser(User entity);

    /**
     * Changes a user password
     * @param entity  the user to be modified
     * @param enabled true if enabled, false otherwise
     * @return
     */
    public User changeUserStatus(User entity, boolean enabled);


    /**
     * Changes the role of the user int he system
     * @param entity
     * @param role
     * @return
     */
    public User changeUserRole(User entity, User.Role role);



    /**
     * Changes a users's password
     * @param entity
     * @return
     */
    public User changePassword(User entity, String newPassword);

    /**
     * Changes changes the user's username
     * @param entity
     * @return
     */
    public User changeUserName(User entity, String newUsername);


    /**
     * Returns a user object with the given username
     * @param username
     * @return
     */
    public User getUserByUserName(String username);


}
