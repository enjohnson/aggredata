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

package server.com.ted.aggredata.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import server.com.ted.aggredata.model.Group;
import server.com.ted.aggredata.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO for accessing the user object
 */
public class UserDAO extends AggreDataDAO<User>{



    public UserDAO()
    {
        super("aggredata.user");
    }



    private RowMapper<User> rowMapper = new RowMapper<User>() {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
              User user = new User();
                        user.setId(rs.getLong("id"));
                        user.setUsername(rs.getString("username"));
                        user.setActivationKey(rs.getString("activationKey"));
                        user.setDefaultGroupId(rs.getShort("defaultGroupId"));
                        user.setRole(User.Role.values()[rs.getInt("role")]);
                        user.setPassword(rs.getString("password"));
                        user.setState(rs.getBoolean("state"));
                        return user;
        }
    };


    public User getUserByUserName(String username) {

        try
        {
            String query = "select id, username, activationKey, defaultGroupId, role, password, state from aggredata.user where username= ?";
            return getJdbcTemplate().queryForObject(query, new Object[]{username}, rowMapper);
        } catch (EmptyResultDataAccessException ex)
        {
            logger.debug("No Results returned");
            return null;
        }


    }

    @Override
    public RowMapper<User> getRowMapper() {
        return rowMapper;
    }

    public void create(User user)
    {
        String query = "insert into aggredata.user (username, activationKey, defaultGroupId, role, password, state) values (?,?,?,?,?,?)";
        getJdbcTemplate().update(query, user.getUsername(), user.getActivationKey(), user.getDefaultGroupId(), user.getRole().ordinal(), user.getPassword(), user.isState());
    }

    public void save(User user)
    {
        String query = "update aggredata.user set username=?, activationKey=?, defaultGroupId=?, role=?, password=?, state=? where id = ?";
        getJdbcTemplate().update(query, user.getUsername(), user.getActivationKey(), user.getDefaultGroupId(), user.getRole().ordinal(), user.getPassword(), user.isState(), user.getId());
    }

}
