package com.py110.basic

/**
 * 流程条件帮助
 */
class Pystru30 implements Serializable {
  String ftabn
  String fcoln
  String fcola
  String fdisp
  String ftype
  String fhelp
  String fjszd
  String fbhzd
  String fmczd
  String fmxzd
  String fbmjg
  String fcxbz
  Integer fsubs

  static constraints = {
    ftabn(size:0..40,nullable:false)
    fcoln(size:0..40,nullable:false)
    fcola(size:0..40,nullable:false)
    fdisp(size:0..3,nullable:true)
    ftype(size:0..16,nullable:false)
    fhelp(maxSize:11,nullable:true)
    fjszd(maxSize:11,nullable:true)
    fbhzd(maxSize:11,nullable:true)
    fmczd(maxSize:11,nullable:true)
    fmxzd(maxSize:11,nullable:true)
    fbmjg(maxSize:11,nullable:true)
    fcxbz(maxSize:1,nullable:true)
    fsubs(size:0..1,nullable:false)

  }
  static mapping ={
    table "PYSTRU30"
    version false
    cache true
    id composite:['ftabn','fdisp','fcoln']
    ftabn column:"F_TABN"
    fcoln column:"F_COLN"
    fcola column:"F_COLA"
    fdisp column:"F_DISP"
    ftype column:"F_TYPE"
    fhelp column:"F_HELP"
    fjszd column:"F_JSZD"
    fbhzd column:"F_BHZD"
    fmczd column:"F_MCZD"
    fmxzd column:"F_MXZD"
    fbmjg column:"F_BMJG"
    fcxbz column:"F_CXBZ"
    fsubs column:"F_SUBS"

  }
}