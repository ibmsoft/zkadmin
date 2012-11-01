package com.py110.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * User: Administrator
 * Date: 2010-3-4
 * Time: 13:47:47
 */
public class GenericResultSetExtractor implements ResultSetExtractor {
    /**
     * 数据有几列
     */
    private int meteCount;
    private int pageSize;
    private int pageNo;
    private ParameterizedRowMapper rowMapper;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public ParameterizedRowMapper getRowMapper() {
        return rowMapper;
    }

    public void setRowMapper(ParameterizedRowMapper rowMapper) {
        this.rowMapper = rowMapper;
    }

    public ArrayList extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        ArrayList d = new ArrayList();
        int currentRow = 0;
        int startRow = (getPageNo() - 1) * getPageSize();
        while (resultSet.next() && currentRow < startRow + getPageSize()) {
            if (currentRow >= startRow) {
                d.add(getRowMapper().mapRow(resultSet, getMeteCount()));
            }
            currentRow++;
        }
        return d;
    }

    public void setMeteCount(int cnt) {
        this.meteCount = cnt;
    }

    public int getMeteCount() {
        return this.meteCount;
    }

}
