package com.py110.basic

/**
 * 内码表
 */
class Pynbbm implements Serializable{
    String  ftype     // 类型
    String  fprefix   //前缀
    Integer flen      //长度
    Long    fcurlen   //当前长度
    String  fnote     // 备注

    static constraints = {
        ftype(blank: false,size: 2..10)
        fprefix(blank: false,size: 2..10)
        flen(blank: false)
        fcurlen(blank: false)
    }
    static  mapping = {
        version false
        id composite :['ftype','fprefix']
    }
}
