package com.py110.jdbc;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * User: Administrator
 * Date: 2010-3-4
 *
 * Time: 12:27:41
 */
public class GenericRowMapper implements ParameterizedRowMapper {
    public Object mapRow(ResultSet resultSet, int j) throws SQLException {
        ArrayList data = new ArrayList();
        for (int i = 1; i <= j; i++) {
            data.add(resultSet.getString(i) == null ? "" : resultSet.getString(i));
        }
        return data;
    }
}
