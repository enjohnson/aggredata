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

import com.ted.aggredata.model.AggreDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

/**
 * Common abstract class for all AggreData2 DAO's. Contains common methods and utilities.
 *
 * @param <T>
 */

public abstract class AggreDataDAO<T> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DataSource aggredataDataSource;

    private String tableName;
    private JdbcTemplate jdbcTemplate = null;

    public AggreDataDAO(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Returns the rowmapper for the DAO type
     *
     * @return
     */
    public abstract RowMapper<T> getRowMapper();

    public abstract void create(T entity);

    public abstract void save(T entity);


    public T getById(Long id) {
        try {
            String query = "select * from " + tableName + " where id= ?";
            return getJdbcTemplate().queryForObject(query, new Object[]{id}, getRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("No Results returned");
            return null;
        }
    }


    protected JdbcTemplate getJdbcTemplate() {
        if (jdbcTemplate == null) {
            jdbcTemplate = new JdbcTemplate(aggredataDataSource);
        }
        return jdbcTemplate;
    }

    public void delete(AggreDataModel aggreDataModel) {
        getJdbcTemplate().update("delete from " + tableName + " where id=?", new Object[]{aggreDataModel.getId()});
    }


}
