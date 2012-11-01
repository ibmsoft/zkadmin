package com.py110.menu
/**
 * 菜单
 */
class Pymenu implements Serializable{
    String fbh
    String name  // func
    String sdesc
    String icon
    String sdomain
    String stype
    String surl
    Integer mx
    Integer js
    Integer isort
    Date lastUpdated
    Date dateCreated

    static constraints = {
        fbh(blank:false,nullable:false,unique:true,size:0..20)
        name(blank:false,nullable:false,size:0..100)
        sdesc(blank:false,nullable:false,size:0..100)
        icon(blank:true,nullable:true,size:0..100)
        sdomain(blank:true,nullable:true,size:0..100)
        stype(blank:false,nullable:false,size:0..100)
        surl(blank:true,nullable:true,size:0..200)
        mx(blank:false,nullable:false,size:0..20)
        js(blank:false,nullable:false,size:0..20)
        isort(blank:true,nullable:true,size:0..100)
    }
    static mapping = {
        table 'PYMENU'
        cache:true
//    version false
        //    id composite:['fbh']
    }
    /*
    new Pymenu(fbh: "1", name: "basic", sdesc: "菜单", icon: "", stype: "${MenuType.MNONE}", surl: "", sdomain: "", isort: 00, mx: 0, js: 0).save();
new Pymenu(fbh: "001", name: "basic", sdesc: "字典对象", icon: "/images/icons/folder.png", stype: "${MenuType.MURL}", surl: "", sdomain: "", isort: 00, mx: 0, js: 1).save();
new Pymenu(fbh: "001010", name: "cuwbzd", sdesc: "外币维护", icon: "/images/icons/folder.png", stype: "${MenuType.MURL}", surl: "com/zysoft/basic/cuwbzd.zul", sdomain: "", isort: 00, mx: 1, js: 2).save();
new Pymenu(fbh: "001030", name: "cuholiday", sdesc: "节日维护", icon: "/images/icons/folder.png", stype: "${MenuType.MURL}", surl: "com/zysoft/basic/cuholiday.zul", sdomain: "", isort: 00, mx: 1, js: 2).save();
new Pymenu(fbh: "001040", name: "cuconf", sdesc: "参数设置", icon: "/images/icons/folder.png", stype: "${MenuType.MURL}", surl: "com/zysoft/basic/pyconf.zul", sdomain: "", isort: 00, mx: 1, js: 2).save();
new Pymenu(fbh: "001045", name: "cubase", sdesc: "月末处理", icon: "/images/icons/folder.png", stype: "${MenuType.MURL}", surl: "com/zysoft/basic/cumonth.zul", sdomain: "", isort: 00, mx: 1, js: 2).save();
new Pymenu(fbh: "001050", name: "cuquery", sdesc: "汇率查询", icon: "/images/icons/folder.png", stype: "${MenuType.MURL}", surl: "com/zysoft/basic/cuquery.zul", sdomain: "", isort: 00, mx: 1, js: 2).save();
new Pymenu(fbh: "001070", name: "basic", sdesc: "用户维护", icon: "/images/icons/folder.png", stype: "${MenuType.MURL}", surl: "com/zysoft/auth/lspass.zul", sdomain: "", isort: 00, mx: 1, js: 2).save();
new Pymenu(fbh: "001080", name: "basic", sdesc: "定时任务", icon: "/images/icons/folder.png", stype: "${MenuType.MURL}", surl: "com/py110/basic/job.zul", sdomain: "", isort: 00, mx: 1, js: 2).save();
new Pymenu(fbh: "999", name: "basic", sdesc: "系统菜单维护", icon: "/images/icons/folder.png", stype: "${MenuType.MNONE}", sdomain: "", isort: 00, mx: 0, js: 1).save();
new Pymenu(fbh: "999001", name: "basic", sdesc: "菜单", icon: "/images/icons/folder.png", stype: "${MenuType.MURL}", surl: "com/py110/menu/menulist.zul", sdomain: "", isort: 00, mx: 1, js: 2).save();
     */
}
