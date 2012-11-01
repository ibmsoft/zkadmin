package com.py110.query;

import java.util.List;

/**
 * 通用查询接口
 */
public interface Query {
    void createTmpTable();
    void setTableGS(String sTable);
    void showResult(List data);
}
