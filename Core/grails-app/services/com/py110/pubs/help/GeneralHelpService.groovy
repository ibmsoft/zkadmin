package com.py110.pubs.help

import groovy.sql.Sql
import org.springframework.jdbc.core.JdbcTemplate
import com.py110.jdbc.GenericRowMapper
import com.py110.jdbc.GenericResultSetExtractor

/**
 * 通用数据帮助窗口
 */
class GeneralHelpService {

  boolean transactional = true
  JdbcTemplate jdbcTemplate
  def dataSource
  Sql sql
  def sTableName,sBhCol,sMcCol,sJsCol,sMxCol,/*sHelpTitle,sBhTitle,sMcTitle,sJsTitle,sMxTitle,*/sWhere
  def sHelpErrors = []
  def sqlString = ""
  def getData(Map arg) {
    sTableName = arg.get(com.zysoft.pubs.ulit.Constants.TableName).toString()
    sBhCol = arg.get(com.zysoft.pubs.ulit.Constants.BhCol).toString()
    sMcCol = arg.get(com.zysoft.pubs.ulit.Constants.McCol).toString()
    sJsCol = arg.get(com.zysoft.pubs.ulit.Constants.JsCol).toString()
    sMxCol = arg.get(com.zysoft.pubs.ulit.Constants.MxCol).toString()
//    sHelpTitle = arg.get(Constants.HelpTitle).toString()
//    sBhTitle = arg.get(Constants.BhTitle).toString()
//    sMcTitle = arg.get(Constants.McTitle).toString()
//    sJsTitle = arg.get(Constants.JsTitle).toString()
//    sMxTitle = arg.get(Constants.MxTitle).toString()
    sWhere = arg.get(com.zysoft.pubs.ulit.Constants.Where).toString()

    if (sTableName == null || sTableName.equals("") || sTableName.equals("null") ) {
      sHelpErrors << "请先指定表名"
    }

    if (sBhCol == null || sBhCol.equals("") || sBhCol.equals("null")) {
      sHelpErrors << "请指定编号列"
    }

    if (sMcCol == null || sMcCol.equals("") || sMcCol.equals("null")) {
      sHelpErrors << "请指定名称列"
    }

    if (sJsCol == null || sJsCol.equals("") || sJsCol.equals("null")) {
      sJsCol = "''"
    }

    if (sMxCol == null || sMxCol.equals("") || sMxCol.equals("null")) {
      sMxCol = "''"
    }

    if (sWhere == null || sWhere.equals("") || sWhere.equals("null")) {
      sWhere = " 1 = 1 "
    }

    if (sHelpErrors.size() >= 1) {
      throw new Exception("帮助出错！原因为"+sHelpErrors.toString())
    }
    sqlString =  " select ${sBhCol} BH,${sMcCol} MC,${sJsCol} JS,${sMxCol} MX from ${sTableName} where ${sWhere}".toString()
    
/*    getSqlConn()
    def helpdatas = []
//    println  " select ${sBhCol} BH,${sMcCol} MC,${sJsCol} JS,${sMxCol} MX from ${sTableName} where ${sWhere}".toString()
    sql.eachRow(sqlString){
      helpdatas << it.toRowResult()
    }
    return helpdatas*/
  }

  def getSqlConn() {
    if (sql == null) {
      sql = new Sql(dataSource)
    }
  }

/*  def getDataByTableName(String name) {
    getSqlConn()
    def rdata = []
    sql.eachRow(" select * from "){

    }
  }*/

  def getDataBySql(String sql,int pageSize,int pageNo) {
    GenericRowMapper rowMapper = new GenericRowMapper()
    GenericResultSetExtractor extractor = new GenericResultSetExtractor()
    extractor.setMeteCount(4)
    extractor.setRowMapper(rowMapper)
    extractor.setPageSize(pageSize)
    extractor.setPageNo (pageNo)
    return jdbcTemplate.query(sql,extractor)

  }
}
