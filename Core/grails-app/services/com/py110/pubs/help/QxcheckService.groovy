package com.py110.pubs.help

import org.springframework.jdbc.core.JdbcTemplate

/**
 * 数据权限检查
 */
class QxcheckService {

  static transactional = true
  JdbcTemplate jdbcTemplate
  public String gfchecksjqx2(String pszgbh,String psqxbh,String pscol,Integer pibzw,String pssuffixyear){
    String vstemp,vssql,vstbl,vsfzlb,vssavetbl
    int vicount

    if (pssuffixyear == null)  pssuffixyear=''

    if (vssavetbl == null  || vssavetbl.trim().equals("") ) vssavetbl='LSUSSJ'

//分别处理原先有无条件
    vstemp=''
    vstbl=vssavetbl+pssuffixyear

    vssql="select count(*) from LSGRAN"+pssuffixyear+" where F_QXBH='"+psqxbh+ "' and F_SFSY='1'"
    vicount=0
    vicount = jdbcTemplate.queryForInt(vssql)
//    if <0 then
//    messagebox("提示","取数时出现错误！")
//    return ' 1=2'
//    end if
//如果使用该权限
    if (vicount == 1) {
      //判断是否分级
      vstemp=''
      vssql="select F_BMJG from LSGRAN"+pssuffixyear+"  where F_QXBH='"+psqxbh+"'"
      Map rowSet = jdbcTemplate.queryForMap(vssql)
      vstemp = rowSet.get("F_BMJG")
//    if <0 {
//    messagebox("提示","取数时出现错误！")
//    return ' 1=2'
//   }
      if (vstemp.length() >0 && !vstemp.trim().equals("")  ) {
        vssql ="  exists (select 1 from "+vstbl+"   LSUSSJ where LSUSSJ.F_QXBH='"+psqxbh+"' and "+pscol+" like (LSUSSJ.F_SJBH+'%') and LSUSSJ.F_ZGBH='"+pszgbh+"' and LSUSSJ.F_G"+pibzw.toString()+"='1') "
      }else{
        vssql ="  exists (select 1 from "+vstbl+"   LSUSSJ where LSUSSJ.F_QXBH='"+psqxbh+"' and "+pscol+"=LSUSSJ.F_SJBH and LSUSSJ.F_ZGBH='"+pszgbh+"' and LSUSSJ.F_G"+pibzw+"='1') "
      }
//如果不使用该类权限
    }
    else  {
      vssql=' 1=1 '
    }
     return vssql
  }
}
