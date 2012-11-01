package com.py110.menu

import grails.validation.ValidationException
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.InputEvent
import org.zkoss.zk.ui.event.SelectEvent
import org.zkoss.zkgrails.GrailsComposer
import org.zkoss.zul.*
import com.py110.user.Pyuser

/**
 * 维护Pymenu菜单  -菜单步长按 3 来处理
 */
class PymenuComposer extends GrailsComposer {
    Window crudzymenu,meWindow
    Pymenu pymenu
    Tree treeMenu
    Toolbar menutoolbar
    Toolbarbutton zymenuaddt,zymenuaddx,zymenudel,zymenusave,zymenuclose
//  ServletContext servletContext
    def typesMC
    Pyuser  userInfo
    Treeitem selectedItem
    Set<Pymenu> addOrUpdateList,deleteList,subAddList
    def afterCompose = {window ->
        treeMenu.setNonselectableTags("")
        userInfo = (Pyuser)Executions.getCurrent().getSession().getAttribute("user")
        if (userInfo?.fadmin.equals("0")) {
            menutoolbar.setTooltiptext("非管理员没有维护数据权限！")
            menutoolbar.getChildren().each { c ->
                c.setDisabled(true)
            }
        }
        meWindow = window
        typesMC = ["funs":"函数","dict":"字典","struc":"结构","domain":"数据元素","NO":"无定义"]
        addOrUpdateList = new HashSet<Pymenu>()
        deleteList = new HashSet<Pymenu>()
        subAddList = new HashSet<Pymenu>()
        initTopMenu()
//    servletContext = ServletFns.getCurrentContext()
//    Map map = Executions.getCurrent().getArg()
//    if (map == null) {
//      throw new RuntimeException("没有传入运行参数！")
//    }else{
//      wldwzdInstance = (Pymenu)map.get("wldwzdInstance")
//    }
//    initData()
    }
/*
  def onCreate() {
    initTopMenu()
  }
*/


    def onClick_zymenureload(){
        initTopMenu()
    }

    def onClick_zymenuaddt() {
        doCrudOper("T")
    }

    def onClick_zymenuaddx() {
        doCrudOper("X")
    }

    def onClick_zymenudel() {
        doCrudOper("D")
    }

    def onClick_zymenusave() {
        Pymenu.withTransaction { status ->
            for (Pymenu a:addOrUpdateList) {  // 保存的同时，需要同时更新上下级
                if (a.id) {
                    a.merge(flush:true)
                }else {
                    try{
                        a.save(failOnError:true)
                    }catch (ValidationException e){
                        Messagebox.show(e.getMessage(),"提示",Messagebox.OK,Messagebox.INFORMATION,0,null)
                    }
                    /*   if ( !a.save(flush: true)) {
                      Messagebox.show(a.errors.toString(),"提示",Messagebox.OK,Messagebox.INFORMATION,0,null)
                    }*/
                }
//        def np = Pymenu.findByFbhAndJs()
            }
        }
        Pymenu.withTransaction { delstatus ->
            for (Pymenu d: deleteList) {
                if (d.id) {
                    if (d.mx == 0) {
                        d.delete(flush: true)
                    } else {
                        Pymenu.executeUpdate(" delete from Pymenu m where m.fbh like '" + d.fbh + "%' and m.js >=" + d.js)
                    }
                }
            }
        }
        Pymenu.withTransaction { substatus ->
            def pm
            for (Pymenu subi: subAddList) {
                subi.save(flush:true)
                pm = Pymenu.findByFbhAndJs(subi.fbh[0..-4],subi.js - 1)
                if (pm) {
                    pm.mx = 0
                    if (pm.save(flush:true)) {
                    }else{
                        substatus.setRollbackOnly()
                    }
                }
            }
        }
        addOrUpdateList.clear()
        deleteList.clear()
        subAddList.clear()
        this.onClick_zymenureload()
        Messagebox.show("保存成功","提示",Messagebox.OK,Messagebox.INFORMATION,0,null)
//    meWindow.invalidate()

    }

    def doCrudOper(String stype) {
        selectedItem = treeMenu.getSelectedItem()
        if (selectedItem == null) {
            Messagebox.show("请先选择一条数据！","提示",Messagebox.OK,Messagebox.INFORMATION,0,null)
            return
        }
        pymenu = (Pymenu) selectedItem.getValue()
        crudMenu(pymenu,stype)

    }
//  def onClick_treeMenu() {
//    println "se"
//  }


    def crudMenu(Pymenu menu,String stype){
        switch (stype){
            case "T":
                def tzymenu = new Pymenu(menu.properties)
                tzymenu.id = null
//        tzymenu.properties = menu.properties
                renderText(selectedItem.getParent(),tzymenu)
                addOrUpdateList.add(tzymenu)
                break
            case "X":
                def xzymenu = new Pymenu(menu.properties)
//        xzymenu.properties = wldwzdInstance.properties
                xzymenu.id = null
                xzymenu.js = xzymenu.js + 1
                xzymenu.mx = 1
                def tp = new Treechildren()
                selectedItem.appendChild(tp)
                renderText(tp,xzymenu)
//        addOrUpdateList.add(menu)
                subAddList.add(xzymenu)
                break
            case "D":
                selectedItem.detach()
                deleteList.add(menu)
//        subAddList.add(menu)
                break
//      default:

        }
    }

    def renderText(def p,Pymenu menu) {

        def newItem
        p.append{
            newItem = treeitem(value:menu/*,selected:true*/,open:false){
                treerow{
                    treecell(image:menu.id == null?"/images/icons/dirty.gif":""){
                        textbox(value:menu.fbh,inplace:true,hflex:"1",onChange:{InputEvent e->
                            menu.fbh = e._val
                        })}
                    treecell{
                        combobox(/*fulfill:"onOpen",*/readonly:true,value:typeMC(menu.name),inplace:true,hflex:"1",onSelect:{SelectEvent e->
                            menu.name = e.getSelectedItems().asList().get(0).value
                        }){
                            typesMC.each {
                                comboitem(value:it.key,label:it.value)
                            }
                        }
                    }
                    treecell{
                        textbox(value:menu.sdesc,inplace:true,hflex:"1",onChange:{InputEvent e->
                            menu.sdesc = e._val
                        })
                    }
                    treecell{
                        textbox(value:menu.icon,inplace:true,hflex:"1"/*,readonly:true*/)
                    }
                    treecell{
                        textbox(value:menu.surl,inplace:true,hflex:"1" /*,readonly:true*/)
                    }
                }
            }
        }
        treeMenu.setSelectedItem(newItem)

    }

    def renderMenuText(def p,Pymenu menu) {
        def  isReadOnly = userInfo?.fadmin.equals("0")?false:true
        p.append {
            if (isReadOnly) {
                treecell(/*image:"/images/icons/dirty.gif"*/) {
                    textbox(value: menu.fbh, inplace: true, hflex: "1", onChange: {InputEvent e ->
                        menu.fbh = e._val
                        addOrUpdateList.add(menu)
                    })
                }
                treecell {
                    combobox( readonly: true, value: typeMC(menu.name), inplace: true, hflex: "1", onSelect: {SelectEvent e ->
                        menu.name = e.getSelectedItems().asList().get(0).value
                        addOrUpdateList.add(menu)
                    }) {
                        typesMC.each {
                            comboitem(value: it.key, label: it.value)
                        }
                    }
                }
                treecell {
                    textbox(value: menu.sdesc, inplace: true, hflex: "1", onChange: {InputEvent e ->
                        menu.sdesc = e._val
                        addOrUpdateList.add(menu)
                    })
                }
                treecell(label:menu.icon)
                treecell(label:menu.surl)
                /*  treecell {
                  textbox(value: menu.icon, inplace: true, hflex: "1", readonly: true)
                }
                treecell {
                  textbox(value: menu.surl, inplace: true, hflex: "1", readonly: true)
                }*/
            }else{
                treecell(label:menu.fbh)
                treecell(label:typeMC(menu.name))
                treecell(label:menu.sdesc)
                treecell(label:menu.icon)
                treecell(label:menu.surl)
            }


        }
    }
    def initTopMenu() {
//    treeMenu.invalidate()
        treeMenu.clear()
//    treeMenu.getChildren().clear()
        def result = Pymenu.findByJsAndMx(0,0)
        result.each {
            initMenu(it)
        }

    }
    def initMenu(Pymenu menu) {


//    if (menu.mx.equals(1)){
//      rootItem.onDoubleClick = { Event e ->
//        initTab (maintab,menu)
//      }
//    }

        if (menu.mx.equals(0) && menu.js.equals(0) && menu.id){
            Treeitem   rootItem = new Treeitem()
            rootItem.setOpen(false)
            treeMenu.getTreechildren().appendChild(rootItem)
            rootItem.setId("treeitem_${menu.fbh}")
            rootItem.setValue(menu)

            Treerow treerow  =  new Treerow()
            treerow.setParent(rootItem)
            renderMenuText(treerow,menu)
            treerow.onDoubleClick = { Event e ->
                rootItem.setOpen(!rootItem.isOpen())
            }
            /* treerow.appendChild(new Treecell(menu.fbh))
           treerow.appendChild(new Treecell(typeMC(menu.name)))
           treerow.appendChild(new Treecell(menu.sdesc))
           treerow.appendChild(new Treecell(menu.icon))
           treerow.appendChild(new Treecell(menu.surl))*/
            Treechildren treechildren  = new Treechildren()
            treechildren.setParent (rootItem)
//      def wldwzdInstance = Pymenu.findAllByJsAndMx(menu.js+1,0)
            def zymenu = Pymenu.executeQuery(" select m from Pymenu m where m.js = ? and m.mx = ? order by fbh asc ",[menu.js+1,0])
            zymenu.each {
                initSubMenu(rootItem,it)
            }
        }


    }

    def initSubMenu(Treeitem treeitem,Pymenu menu ){
        Treeitem   rootItem = new Treeitem()
        rootItem.setOpen(false)
        rootItem.setId("treeitem_${menu.fbh}")
        rootItem.setValue(menu)

        Treerow treerow  =  new Treerow()
        treerow.setParent(rootItem)
        renderMenuText(treerow,menu)
        treerow.onDoubleClick = { Event e ->
            rootItem.setOpen(!rootItem.isOpen())
        }
        /*  treerow.appendChild(new Treecell(menu.fbh))
       treerow.appendChild(new Treecell(typeMC(menu.name)))
       treerow.appendChild(new Treecell(menu.sdesc))
       treerow.appendChild(new Treecell(menu.icon))
       treerow.appendChild(new Treecell(menu.surl))*/

        Treechildren treechildren = treeitem.getTreechildren()
        if (treechildren == null) {
            treechildren = new Treechildren()
            treechildren.setParent(treeitem)
        }
        treechildren.appendChild(rootItem)
        if (menu.mx.equals(1)){
//      rootItem.onDoubleClick = { Event e ->
//        initTab (maintab,menu)
//      }
        }else if (menu.mx.equals(0)){
//      def wldwzdInstance = Pymenu.findAllByJsAndFbhLike(menu.js+1,menu.fbh+"%")
            def zymenu = Pymenu.executeQuery(" select m from Pymenu m where m.js =? and m.fbh like ? order by fbh asc",[menu.js+1,menu.fbh+"%"])
            zymenu.each {
                initSubMenu(rootItem,it)
            }
        }

    }


    def typeMC(String s) {
        if (typesMC.keySet().contains(s)) {
            typesMC.get(s)
        }else{
            typesMC["NO"]
//      "无定义"
        }
    }


}