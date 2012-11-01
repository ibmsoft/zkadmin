package com.py110.jdbc;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: Administrator
 * Date: 2010-3-4
 *
 * Time: 12:27:41
 */
public class GenericRowStringMapper implements ParameterizedRowMapper {
    public String mapRow(ResultSet resultSet, int j) throws SQLException {
        String s = "";
        for (int i = 1; i <= j; i++) {
            s =  resultSet.getString(i) == null ? "" : resultSet.getString(i);
        }
        return s;
    }
}