package com.py110.basic

/**
 * CUCONF 参数配置文件表
 */
class Pyconf implements Serializable{
  String fvkey
  String fval
  String fnote
  static constraints = {
    fvkey(nullable:false,blank:false,size:0..30)
    fval(nullable:false,blank:true,size:0..255)
    fnote(nullable:true,blank:true,size:0..200)
  }
  static mapping ={
    table "PYCONF"
    version false
    id composite:['fvkey']
    fvkey column:"F_VKEY"
    fval column:"F_VAL"
    fnote column:"F_NOTE"
  }
}
