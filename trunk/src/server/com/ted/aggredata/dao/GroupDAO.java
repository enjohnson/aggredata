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

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import server.com.ted.aggredata.model.Group;
import server.com.ted.aggredata.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO for accessing the user object
 */
public class GroupDAO extends AggreDataDAO<Group> {



    public GroupDAO() {
        super("aggredata.group");
    }

    private RowMapper<Group> rowMapper = new RowMapper<Group>() {
        public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
            Group group = new Group();
            group.setId(rs.getLong("id"));
            group.setOwnerUserId(rs.getLong("ownerUserId"));
            group.setRole(Group.Role.values()[rs.getInt("role")]);
            group.setDescription(rs.getString("description"));
            return group;
        }
    };

    public void create(Group group) {
        String query = "insert into aggredata.group (ownerUserId, description) values (?,?)";
        getJdbcTemplate().update(query, group.getOwnerUserId(), group.getDescription());
    }

    @Override
    public void save(Group group) {
        String query = "update aggredata.group set ownerUserId=?, description=? where id=?";
        getJdbcTemplate().update(query, group.getOwnerUserId(), group.getDescription(), group.getId());
    }

    /**
     * Delete's all memberships for a selected group/
     * @param group
     */
    public void deleteGroupMemberships(Group group)
    {
        String query = "delete from aggredata.usergroup where groupId=?";
        getJdbcTemplate().update(query,  group.getId());
    }

    /**
     * Removes a group from the membership table
     * @param user
     * @param group
     */
    public void removeGroupMembership(User user, Group group)
    {
        String query = "delete from aggredata.usergroup where userId=? and groupId=?";
        getJdbcTemplate().update(query, user.getId(), group.getId());
    }


    /**
        * Removes a group from the membership table
        * @param user
        * @param group
        */
       public void updateGroupMembership(User user, Group group, Group.Role role)
       {
           String query = "update aggredata.usergroup set role = ? where userId=? and groupId=?";
           getJdbcTemplate().update(query, role.ordinal(), user.getId(), group.getId());
       }

    /**
     * updates a group
     * @param user
     * @param group
     * @param role
     */
    public void addGroupMembership(User user, Group group, Group.Role role)
    {
        String query = "insert into aggredata.usergroup(userId, groupId, role) values (?,?,?)";
        getJdbcTemplate().update(query, user.getId(), group.getId(), role.ordinal());
    }

    /**
     * Returns a group list based on the user's role.
     *
     * @param user
     * @return
     */
    public List<Group> getGroups(User user) {
        try
        {
            String query = "select g.id, g.ownerUserId, g.description, ug.role from aggredata.group g, aggredata.usergroup ug where ug.groupId = g.id and ug.userId=?";
            return getJdbcTemplate().query(query, new Object[]{user.getId()}, getRowMapper());
        } catch (EmptyResultDataAccessException ex)
        {
            logger.debug("No Results returned");
            return null;
        }
    }

    public Group getGroup(User user, String description)
    {
        try
        {
            String query = "select g.id, g.ownerUserId, g.description, ? as role from aggredata.group g where g.description=? and g.ownerUserId=?";
            return getJdbcTemplate().queryForObject(query, new Object[]{ Group.Role.ADMIN.ordinal(), description, user.getId()}, getRowMapper()) ;
        } catch (EmptyResultDataAccessException ex)
        {
            logger.debug("No Results returned");
            return null;
        }
    }

    @Override
    public RowMapper<Group> getRowMapper() {
        return rowMapper;
    }
}
