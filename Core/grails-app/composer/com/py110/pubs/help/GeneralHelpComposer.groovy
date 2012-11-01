package com.py110.pubs.help

//import groovy.sql.Sql


import org.springframework.jdbc.core.JdbcTemplate
import org.zkoss.util.resource.Labels
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.ForwardEvent
import org.zkoss.zkgrails.GrailsComposer
import org.zkoss.zul.impl.InputElement
import org.zkoss.zul.*
import com.py110.pubs.ulit.Constants

/**
 * 通用数据字典帮助
 * 如果是分级字典，需要传入对应的结构编码
 */
class GeneralHelpComposer extends GrailsComposer  {

  Toolbarbutton tbbok,tbbcancel,tbbsearch
  Checkbox cbauto
  Textbox tbsearch
  Tree treeMenu
  Toolbar tbbar
//  Treefooter tfcnt
  Paging pagingBar
  Label labelRowCnt
  Window wParent
  Map args
  def sTableName,sBhCol,sMcCol,sJsCol,sMxCol,sHelpTitle,sBhTitle,sMcTitle,sJsTitle,sMxTitle,sWhere,sTableStru,sMulSelect
  def sHelpErrors = []
  Treeitem selectItem
  Set selectItems
  ArrayList<Map> returnItems
  JdbcTemplate jdbcTemplate
  InputElement inputElement
  def treeList = [],filterTreeList = []
//  SimpleTreeModel simpleTreeModel
  def afterCompose = {window ->
//    simpleTreeModel = new SimpleTreeModel()
    wParent     = window
    returnItems =  new ArrayList<Map>()
    args        = Executions.getCurrent().getArg()
    sTableName  = arg.get(com.py110.pubs.ulit.Constants.TableName).toString().trim()
    sBhCol      = arg.get(com.py110.pubs.ulit.Constants.BhCol).toString().trim()
    sMcCol      = arg.get(com.py110.pubs.ulit.Constants.McCol).toString().trim()
    sJsCol      = arg.get(com.py110.pubs.ulit.Constants.JsCol).toString().trim()
    sMxCol      = arg.get(com.py110.pubs.ulit.Constants.MxCol).toString().trim()
    sMulSelect  = arg.get(com.py110.pubs.ulit.Constants.MSelect).toString().trim()
    sTableStru  = arg.get(com.py110.pubs.ulit.Constants.TSTRU).toString().trim()   // 表结构
    sHelpTitle  = arg.get(com.py110.pubs.ulit.Constants.HelpTitle).toString().trim()
    sBhTitle    = arg.get(com.py110.pubs.ulit.Constants.BhTitle).toString().trim()
    sMcTitle    = arg.get(com.py110.pubs.ulit.Constants.McTitle).toString().trim()
    sJsTitle    = arg.get(com.py110.pubs.ulit.Constants.JsTitle).toString().trim()
    sMxTitle    = arg.get(com.py110.pubs.ulit.Constants.MxTitle).toString().trim()
    sWhere      = arg.get(com.py110.pubs.ulit.Constants.Where).toString()

    if (arg.containsKey(com.py110.pubs.ulit.Constants.Component)) {
      inputElement = (InputElement)arg.get(com.py110.pubs.ulit.Constants.Component)
    }

    if (sTableName == null || sTableName.equals("") || sTableName.equals("null") ) {
      sHelpErrors << message("help.table.notfound")
    }

    if (sBhCol == null || sBhCol.equals("") || sBhCol.equals("null")) {
      sHelpErrors << message("help.table.bhcol")
    }

    if (sMcCol == null || sMcCol.equals("") || sMcCol.equals("null")) {
      sHelpErrors << message("help.table.mccol")
    }

    if (sJsCol == null || sJsCol.equals("") || sJsCol.equals("null")) {
      sJsCol = "''"
    }

    if (sMxCol == null || sMxCol.equals("") || sMxCol.equals("null")) {
      sMxCol = "''"
    }
    if (sTableStru == null || sTableStru.equals("") || sTableStru.equals("null")) {
      sTableStru = "''"
    }

    if (sHelpTitle == null || sHelpTitle.equals("") || sHelpTitle.equals("null")) {
      sHelpTitle = message("help.table.title")
    }
    if (sWhere == null || sWhere.equals("") || sWhere.equals("null")) {
      sWhere = " 1 = 1 "
    }

    if (sHelpErrors.size() >= 1) {
      throw new Exception("帮助出错！原因为"+sHelpErrors.toString())
    }
    if (sMulSelect == null || sMulSelect.equals("")) {
      treeMenu.setMultiple(false)
    }else if (sMulSelect.equals("1")) {
      treeMenu.setMultiple(true)
    }
    treeMenu.setTreeitemRenderer (new GeneralTreeRender())
    wParent.setTitle(sHelpTitle)
    treeList = initData()
    filterTreeList = treeList
    initTree(treeList)
    /*  wParent.addEventListener(Events.ON_CLOSE, new EventListener() {
      public void onEvent(Event event){
        println "tt"
      }
    })*/
  }


  def initData(){
    treeList =  jdbcTemplate.queryForList(" select ${sBhCol} BH,${sMcCol} MC,${sJsCol} JS,${sMxCol} MX from ${sTableName} where ${sWhere}".toString())
  }

  def initTree(def menuData) {
    pagingBar.setTotalSize(menuData.size)
    wParent.setTitle (sHelpTitle + "-- 总数据："+menuData.size() +" 行 ")
    if (sJsCol.equals("''") || sMxCol.equals("''") || sJsCol == null || sMxCol == null) {
      initNoJsTree(menuData)
    }else {
      if (treeMenu.getTreeitemRenderer() != null) {
//        wParent.removeChild(treeMenu)
//        wParent.insertBefore(tbbar,treeMenu)
        treeMenu.setModel (null)
        treeMenu.setTreeitemRenderer null
        treeMenu.clear()
      }
//      Treechildren treechildren = treeMenu.getTreechildren()
//      if (treechildren != null) {
//        treeMenu.clear()
//        wParent.invalidate()
//      }
      treeMenu.setZclass("z-dottree")
      initTopMenu(menuData)
    }

  }
  //
  def initNoJsTree(def treeData) {
    // use model
    pagingBar.setTotalSize(treeData.size)
    wParent.setTitle (sHelpTitle + "-- 总数据："+treeData.size() +" 行 ")
//    treeMenu.setPageSize(40)
//    treeMenu.getPage().se
    treeMenu.setModel (getTreeModel(treeData,0))
//    treeMenu.setMold ("paging")
//    treeMenu.getPaginal().setMold("os")
//    treeMenu.getPagingChild().setAutohide(true)
//    treeMenu.getPaginal().setMold("os")
  }
  def onDoubleClick_treeMenu() {
    onClick_tbbok()
  }


  def onClick_tbbok() {
    if (isSelected() /*&& selectItem?.getValue()["MX"].toString().toInteger() == 1*/) {
      /*if (treeMenu.isMultiple()) {
        for (Treeitem t: selectItems) {
          returnItems.add(t.getValue())
        }
      }else{
        returnItems.add(selectItem.getValue())
      }
       Ulits.getSession().setAttribute("ReturnData",returnItems)
       wParent.detach()*/
//        lslcdyComposer.renderLcry(selectItem.getValue(), true,true)
        args.put(Constants.BhCol,selectItem.getValue()["BH"])
        args.put(Constants.McCol,selectItem.getValue()["MC"])
      if (inputElement != null &&  !treeMenu.isMultiple()) {
        inputElement.setText (selectItem.getValue()["MC"])
        inputElement.setAttribute("key",selectItem.getValue()["BH"])
        inputElement.setAttribute("value",selectItem.getValue()["MC"])
        inputElement.onFocus = {  Event e ->
          inputElement.setValue(inputElement.getAttribute("key"))
        }
        inputElement.onBlur = {  Event e ->
          inputElement.setValue(inputElement.getAttribute("value"))
        }
      } else{
        lslcdyComposer.onAddLcry(selectItem.getValue())
      }
      wParent.detach()
    }

  }


  def onClick_tbbcancel() {
    wParent.detach()
  }

  def getReturnValue() {
//    onClick_tbbok()
    if (isSelected()) {
      return selectItem.getValue()
      wParent.detach()
    }
  }

  def isSelected() {
   /* if (treeMenu.isMultiple()) {
      selectItems = treeMenu.selectedItems
    }else{
      selectItem = treeMenu.getSelectedItem()
      if (selectItem != null){
        selectItems == null ? new HashSet():selectItems
        selectItems.add(selectItem)
      }

    }*/
    selectItems = treeMenu.getSelectedItems()
    if (/*selectItem == null ||*/ selectItems == null) {
      Messagebox.show(Labels.getLabel("selecteditem"), Labels.getLabel("toolstip"), Messagebox.OK, Messagebox.INFORMATION, 0, null)
      return false
    }
    selectItem = selectItems?.toArray()[0]   
    for (Treeitem s:selectItems) {
      if (s.getValue()["MX"].toString().equals("0")) {
        s.setOpen(!s.isOpen())
//      Messagebox.show(Labels.getLabel("selectedMxItem"),Labels.getLabel("toolstip"),Messagebox.OK,Messagebox.INFORMATION,0,null)
        return false
      }
    }
    /* if (selectItem.getValue()["MX"].toString().equals("0")) {
      selectItem.setOpen(!selectItem.isOpen())
//      Messagebox.show(Labels.getLabel("selectedMxItem"),Labels.getLabel("toolstip"),Messagebox.OK,Messagebox.INFORMATION,0,null)
      return false
    }*/
    return true
  }

  def initTopMenu(def menuList) {
    if (sTableStru.equals("''") || sTableStru == null) {
      throw new Exception(message("help.table.stru"))
    }
    pagingBar.setVisible(false)
    Treechildren treechildren = treeMenu.getTreechildren()
    if (treechildren != null && treechildren.getItemCount() > 0) {
      treechildren.getItems().clear()
    }
    def result = menuList.findAll { it["JS"].equals("1") /*&& it["MX"] == 0*/}
    result.each { one ->
      initMenu(menuList, one)
    }

  }

  /**
   * 处理第一级别的数据
   * @param menuList
   * @param menu
   * @return
   */
  def initMenu(def menuList ,def menu) {
    Treeitem   rootItem = new Treeitem()
    rootItem.setOpen(false)
    Treechildren first =  treeMenu.getTreechildren()
    if ( first == null) {
      first =  new Treechildren()
      treeMenu.appendChild(first)
    }else{
//      first.invalidate()
    }
    first.appendChild(rootItem)
    rootItem.setValue(menu)

    Treerow treerow  =  new Treerow()
    treerow.setParent(rootItem)

//      renderMenuText(treerow,menu)
    String sShowBh = ""
    def js = menu["JS"].toString().toInteger()
    def ilen = Ulits.getStruLength(sTableStru,js)
    def mx = menu["MX"].toString().toInteger()
    String bh = menu["BH"].toString()
    String mc = menu["MC"].toString()
    if (bh.length() > ilen && mx != 0) {
      sShowBh = bh.substring(ilen)
    }
    treerow.appendChild(new Treecell(bh))
    treerow.appendChild(new Treecell(mc))
    treerow.appendChild(new Treecell(js.toString()))
    treerow.appendChild(new Treecell(mx.toString()))
    // for mx
    if (mx == 0 ) {
      Treechildren treechildren  = new Treechildren()
      treechildren.setParent (rootItem)
      def nlist = menuList.findAll {
        it["JS"].equals((js+1).toString()) && it["BH"].toString().substring(0,ilen) == bh
      }
      nlist.each {
        initSubMenu(menuList,rootItem,it)
      }
    }
  }

  def initSubMenu(def menuList ,Treeitem treeitem,def menu ){
    Treeitem   rootItem = new Treeitem()
    rootItem.setOpen(false)
    rootItem.setValue(menu)
    Treerow treerow  =  new Treerow()
    treerow.setParent(rootItem)
    def js = menu["JS"].toString().toInteger()
    def mx = menu["MX"].toString().toInteger()
    String bh = menu["BH"].toString()
    String mc = menu["MC"].toString()
    String sShowBh = ""
    int ilength,istart
    ilength = Ulits.getStruLength(sTableStru,js)
    istart = Ulits.getStruLength(sTableStru,js - 1)
    sShowBh = bh.substring(istart,ilength)
    treerow.appendChild(new Treecell(sShowBh/*menu["BH"].toString()*/))
    treerow.appendChild(new Treecell(mc))
    treerow.appendChild(new Treecell(js.toString()))
    treerow.appendChild(new Treecell(mx.toString()))

    Treechildren treechildren = treeitem.getTreechildren()
    if (treechildren == null) {
      treechildren = new Treechildren()
      treechildren.setParent(treeitem)
    }

    treechildren.appendChild(rootItem)
    if (mx?.equals(1)){
//      rootItem.onDoubleClick = { Event e ->
//        initTab (maintab,menu)
//      }
    }else if (mx?.equals(0)){
      def nl = menuList.findAll {it["JS"].equals((js+1).toString()) && it["BH"].toString().substring(0,ilength).equals(bh)}
      nl.each {
        initSubMenu(menuList,rootItem,it)
      }
    }

  }

  def onPaging_pagingBar(ForwardEvent fe) {
    def e = fe.origin
//    def start = e.activePage
//    def psize = pagingBar.pageSize
//    def startIndex = start  * psize
//    def endIndex =  startIndex + psize
//
//    if (endIndex > treeList.size() && treeList.size() >= 1) {
//      endIndex = treeList.size() - 1
//    }
//    def n = []
//    if (treeList.size() > 0 ) {
//      n = treeList[startIndex..endIndex]
//    }
    treeMenu.setModel(getTreeModel(filterTreeList,e.activePage))
  }

  def onChange_tbsearch() {
    def stext = tbsearch.getValue().toString().trim()
//    println "tbsearch="+stext
    doFilter(stext)

  }

  def getTreeModel(def data,def pageNo) {
    if (data == null || data.size() == 0) {
      def temp =["BH":"","MC":"","JS":"","MX":""]
      data.add(temp)
    }
    def psize = pagingBar.pageSize
    def startIndex = pageNo  * psize
    def endIndex =  startIndex + psize

    if (endIndex > data.size() && data.size() >= 1) {
      endIndex = data.size() - 1
    }
    if (startIndex == 0 && endIndex == psize ) {
      endIndex = endIndex - 1
    }
    def n = []
    if (data.size() > 0 ) {
      n = data[startIndex..endIndex]
    }else{
      n = data
    }
    return new SimpleTreeModel(new SimpleTreeNode(n.get(0), n))
  }

  def doFilter(String data) {
//    def newlist = []
    if (!data.equals("")) {
      filterTreeList = treeList.findAll { it["BH"].toString().indexOf(data) >= 0 || it["MC"].toString().indexOf(data) >= 0}
//      filterTreeList = newlist
      initNoJsTree (filterTreeList)
//      treeMenu.clear()
//      treeMenu.setModel (getTreeModel(newlist))
//      tbsearch.setTooltiptext "共找到数据："+ newlist.size() + " 行 "

    }else {
      filterTreeList = treeList
      initTree(treeList)
    }
  }

  def onClick_tbbsearch() {
//    println "tbsearch="
//    println tbsearch.getText()
    doFilter(tbsearch.getText().trim())
  }

  def onOK_tbsearch() {
    onChange_tbsearch()
  }

  // Render
  class GeneralTreeRender implements TreeitemRenderer {
    /*   String tfjs,tfbh,tbh
   int ilen*/
    void render(Treeitem item, Object data) {
      item.setValue data
      /*  tfjs  = data["JS"].toString()
     tfbh  = data["BH"].toString()
     ilen = Ulits.getStruLength(sTableStru,tfjs.toInteger())
     if (tfbh && ilen) {
       if (tfbh.length() > ilen) {
         tbh = tfbh.substring(ilen)
       }
     }
     println tbh*/
      Treerow treerow = new Treerow()
      treerow.setParent item
      treerow.appendChild(new Treecell(data["BH"].toString()))
      treerow.appendChild(new Treecell(data["MC"].toString()))
      treerow.appendChild(new Treecell(data["JS"].toString()))
      treerow.appendChild(new Treecell(data["MX"].toString()))
    }
  }
}