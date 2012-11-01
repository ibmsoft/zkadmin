package com.py110.user
/**
 * 用户表
 */
class Pyuser implements Serializable{
    String fbh     //
    String fname
    String fpass
    String femail
    String fmobile
    String ftel
    String fqq
    String fip
    String fzt ="0" // 状态 0：正常，1：停用，
    String fadmin   // 0：普通，9：管理员
    String fnote
    Date dateCreated
    Date lastUpdated
    def beforeInsert() {
        dateCreated = new Date()
    }
    def beforeUpdate() {
        lastUpdated = new Date()
    }
    static constraints = {
        fbh(nullable: false,blank: false,unique: true, size: 4..20)
        fname(nullable: false,blank: false,unique: true, size: 4..40)
        femail(nullable: false,blank: false,size: 6..40,email: true)
        fmobile(nullable: false,blank: false,size: 11..11)
        ftel(nullable: true,blank: true,size: 7..20,matches: "[0-9]+[-]+[0-9]+")
        fqq(nullable: true,blank: true,size: 5..20,matches: "[0-9]+")
        fip(nullable: false,size: 0..20)
        fzt(nullable: false,size: 1..1)
        fadmin(nullable: false,size: 1..1)
        fnote(nullable: true,blank: true,size: 0..255)
    }
    static  mapping = {
//        version false
        table 'PYUSER'
        id composite:["fbh"]


    }
    /*
    new Pyuser(fbh: "9999",fname: "系统管理员",fpass: new BasicPasswordEncryptor().encryptPassword("8888"),femail: "liuwenwu_2002@163.com",fmobile: "15839334498",ftel: "",fip: "127.0.0.1",fqq: "45972478",fzt:"0",fadmin: "9",fnote: "").save()
     */

}
