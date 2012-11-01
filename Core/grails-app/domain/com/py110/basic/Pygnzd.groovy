package com.py110.basic

/**
 * 功能字典
 */
class Pygnzd {
    String fgnbh
    String fgnmc
    String ffpct
    String fxtbh
    static constraints = {
        fgnbh(nullable:false,blank:false,size:1..30)
        fgnmc(nullable:false,blank:false,size:1..60)
        ffpct(nullable:false,blank:false,size:1..1)
        fxtbh(nullable:false,blank:false,size:1..10)
    }
    static mapping = {
        table "PYGNZD"
        version false
        cache true
        fgnbh column:"F_GNBH"
        fgnbh column:"F_GNMC"
        fgnbh column:"F_FPCT"
        fgnbh column:"F_XTBH"
    }
}
