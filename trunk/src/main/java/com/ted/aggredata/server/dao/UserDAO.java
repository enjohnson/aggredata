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

package com.ted.aggredata.server.dao;

import com.ted.aggredata.model.MTU;
import com.ted.aggredata.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO for accessing the user object
 */
public class UserDAO extends AbstractDAO<User> {

    public static final String DELETE_USER_QUERY = "delete from aggredata.user where id=?";
    public static final String GET_BY_USERNAME_QUERY = "select id, username, activationKey, defaultGroupId, role, state from aggredata.user where username= ?";
    public static final String GET_BY_KEY_QUERY = "select id, username, activationKey, defaultGroupId, role, state from aggredata.user where activationKey= ?";
    public static final String CREATE_USER_QUERY = "insert into aggredata.user (username, activationKey, defaultGroupId, role,  state) values (?,?,?,?,?)";
    public static final String COUNT_USER_QUERY = "select count(*) from  aggredata.user where username=?";
    public static final String SAVE_USER_QUERY = "update aggredata.user set username=?, activationKey=?, defaultGroupId=?, role=?,  state=? where id = ?";
    public static final String UPDATE_PASSWORD = "update aggredata.user set password=? where id = ?";
    public static final String UNIQUE_KEY_CHECK = "select count(*) from  aggredata.user where activationKey=?";


    public UserDAO() {
        super("aggredata.user");
    }


    private RowMapper<User> rowMapper = new RowMapper<User>() {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setActivationKey(rs.getString("activationKey"));
            user.setDefaultGroupId(rs.getShort("defaultGroupId"));
            user.setRole(rs.getString("role"));
            user.setState(rs.getBoolean("state"));
            return user;
        }
    };


    public User getUserByUserName(String username) {

        try {
            if (logger.isDebugEnabled()) logger.debug("looking up user object for username " + username);
            return getJdbcTemplate().queryForObject(GET_BY_USERNAME_QUERY, new Object[]{username}, rowMapper);
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No Results returned");
            return null;
        }
    }

    public User getUserByKey(String key) {
        try {
            if (logger.isDebugEnabled()) logger.debug("looking up user object for key " + key);
            return getJdbcTemplate().queryForObject(GET_BY_KEY_QUERY, new Object[]{key}, rowMapper);
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No Results returned");
            return null;
        }
    }

    @Override
    public RowMapper<User> getRowMapper() {
        return rowMapper;
    }

    public User create(User user) {
        if (getJdbcTemplate().queryForInt(COUNT_USER_QUERY, user.getUsername()) == 0) {
            if (logger.isDebugEnabled()) logger.debug("creating new user " + user);
            getJdbcTemplate().update(CREATE_USER_QUERY, user.getUsername(), user.getActivationKey(), user.getDefaultGroupId(), user.getRole(), user.isState());
        } else {
            if (logger.isDebugEnabled()) logger.debug("User already exists. Skipping create. user=" + user);
        }
        return getUserByUserName(user.getUsername());
    }


    public User save(User user) {
        if (user.getId() == null) {
            logger.debug("id is null. creating new user");
            return create(user);
        } else {
            if (logger.isDebugEnabled()) logger.debug("Saving user " + user);
            getJdbcTemplate().update(SAVE_USER_QUERY, user.getUsername(), user.getActivationKey(), user.getDefaultGroupId(), user.getRole(), user.isState(), user.getId());
            return user;
        }
    }

    public void updatePassword(User user, String password) {
        if (user.getId() == null) {
            logger.error("id is null. skipping password update.");
        } else {
            if (logger.isDebugEnabled()) logger.debug("Saving user " + user);
            getJdbcTemplate().update(UPDATE_PASSWORD, password, user.getId());
        }
    }

    public void delete(User user) {
        if (logger.isDebugEnabled()) logger.debug("removing " + user + " from user table");
        getJdbcTemplate().update(DELETE_USER_QUERY, user.getId());
    }


    /**
     * Checks to see if an activation key is already assigned to a user
     * @param key
     * @return
     */
    public Boolean isUniqueKey(String key) {
        return (getJdbcTemplate().queryForInt(UNIQUE_KEY_CHECK, key) == 0);
    }
}
