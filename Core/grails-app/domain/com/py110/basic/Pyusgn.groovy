package com.py110.basic

/**
 * 用户功能权限表
 */
class Pyusgn implements java.io.Serializable{

    String fzgbh
    String fgnbh
    static constraints = {
        fzgbh(nullable:false,blank: false,size:0..20)
        fzgbh(nullable:false,blank: false,size:0..6)
    }
    static  mapping =  {
        table "PYUSGN"
        version false
        cache true
        id composite: ['fzgbh', 'fgnbh']
        fzgbh column: "F_ZGBH"
        fgnbh column: "F_GNBH"
    }
}
