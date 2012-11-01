package com.py110.basic

class Pygran implements  Serializable{
    String fqxbh
    String fqxmc
    String fxtbh
    String fgran
    String fgrnr
    String ftabn
    String fbhzd
    String fmczd
    String fbmjg
    String fsjbh
    String fjszd
    String fmxzd
    String fwher
    String fsfsy
    String fzqxb
    String fqxb
    String fdisp

    static constraints = {
        fqxbh  (nullable:false,blank:false,size:0..6)
        fqxmc  (nullable:true,blank:true,size:0..60)
        fxtbh  (nullable:true,blank:true,size:0..4)
        fgran  (nullable:false,blank:false,size:0..15)
        fgrnr  (nullable:true,blank:true,size:0..250)
        ftabn  (nullable:false,blank:false,size:0..30)
        fbhzd  (nullable:false,blank:false,size:0..30)
        fmczd  (nullable:false,blank:false,size:0..30)
        fbmjg  (nullable:true,blank:true,size:0..30)
        fsjbh  (nullable:true,blank:true,size:0..30)
        fjszd  (nullable:true,blank:true,size:0..30)
        fmxzd  (nullable:true,blank:true,size:0..30)
        fwher  (nullable:true,blank:true,size:0..254)
        fsfsy  (nullable:true,blank:true,size:1..1)
        fzqxb  (nullable:true,blank:true,size:0..30)
        fqxb   (nullable:true,blank:true,size:0..30)
        fdisp  (nullable:true,blank:true,size:0..3)
    }
    static mapping = {
        table "PYGRAN"
        version false
        cache true
        id composite:['fqxbh']
        fqxbh   column:"F_QXBH"
        fqxmc   column:"F_QXMC"
        fxtbh   column:"F_XTBH"
        fgran   column:"F_GRAN"
        fgrnr   column:"F_GRNR"
        ftabn   column:"F_TABN"
        fbhzd   column:"F_BHZD"
        fmczd   column:"F_MCZD"
        fbmjg   column:"F_BMJG"
        fsjbh   column:"F_SJBH"
        fjszd   column:"F_JSZD"
        fmxzd   column:"F_MXZD"
        fwher   column:"F_WHER"
        fsfsy   column:"F_SFSY"
        fzqxb   column:"F_ZQXB"
        fqxb    column:"F_QXB"
        fdisp   column:"F_DISP"
    }
}
