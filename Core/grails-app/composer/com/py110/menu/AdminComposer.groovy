package com.py110.menu

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.Session
import org.zkoss.zk.ui.event.BookmarkEvent
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.sys.ComponentsCtrl
import org.zkoss.zkgrails.GrailsComposer
import org.zkoss.zul.*
import com.py110.user.Pyuser
import com.py110.theme.ThemeSelector
import com.py110.theme.Themes
import com.py110.pubs.ulit.Constants
import com.py110.pubs.ulit.MenuType
import com.py110.pubs.ulit.Ulits

/**
 * 系统的主要入口程序
 * TODO
 * 1 、点击树，创建多个tab 失败 （可以考虑，如果在tabs 中有，就不创建，最好是允许用户自定义设置 ）
 * 2 、单击树，显示一个提示，不让用户再操作其他的
 *
 */

class AdminComposer extends GrailsComposer  {
  Pymenu mains
  Tree treeMenu
  Borderlayout main
  West westMenu
  Tabbox  maintab
  Tabpanel helptp
  Label userName
  Label loginDate
  Session session
  Pyuser userInfo
  Window loginWindow
  Combobox selecttheme
  Menupopup tabMenu
  Toolbar logout
  def grailsApplication = ApplicationHolder.application
  def srcUrl
  def execution
  def showDateFormat = Constants.DATE_SHOW_FORMAT
  def afterCompose = { window ->
    execution = Executions.getCurrent()

    srcUrl = execution.scheme+"://"+execution.remoteHost+":"+execution.serverPort+execution.getContextPath()

    userInfo   =  Ulits.getUser()
    if (userInfo != null) {
      userName.setValue("登录用户："+userInfo?.fname)
      loginDate.setValue("登录日期："+Ulits.getLogonDate())
//      loginDate.setValue("登录日期："+userInfo?.flastlogindate.format("yyyy-MM-dd"))
    }
  }
  // 页面第一次打开的时候，都执行这个方法
  def onCreate() {
    ThemeSelector themeSelector = new ThemeSelector()
    themeSelector.onCreate()
    logout.appendChild(themeSelector)
    logout.append{
      // todo 退去允许用户保存当前打开的 tab

      toolbarbutton(tooltiptext:"退出", image:"/images/icons/cancel.png",onClick:{Event e->
//        Clients.showBusy ("准备退出.....",true)
        Executions.sendRedirect("/")
        execution.getSession().invalidate()
//        main.invalidate()
//        Clients.showBusy ("",false)
      })
    }
//    Menupopup mp = new Menupopup()
////    mp.setParent(westMenu)
//    westMenu.setContext(mp)
    initTabContext tabMenu
    initTab (maintab,new Pymenu(fbh:"ihelp",name:"ihelp",surl:"help.zul",sdesc:"首页",stype:MenuType.MURL),false)
//     Clients.evalJavaScript("jQuery('#searchBox.getUuid().mask('99.999-999'))");
    if (!userInfo) {
      loginWindow =  Executions.createComponents(Constants.UIDir+"login.zul",null,null)
      loginWindow.doModal()
    }else{
//      Window w = Executions.createComponents("help.zul", helptp,null)
//      w.setParent(helptp)
      def result = Pymenu.findByJsAndMx(0,0,[cache:true])
      result.each {
        initMenu(it)
      }
    }
  }
  def initMenu(Pymenu pymenu) {

    Treeitem   rootItem = new Treeitem()
    rootItem.setId("treeitem_${pymenu.fbh}")
    rootItem.setValue(pymenu)
    rootItem.setOpen(false)

    Treerow treerow  =  new Treerow()
    if (pymenu.mx == 1) {
      treerow.appendChild(new Treecell(pymenu.sdesc))
    }else{
      treerow.appendChild(new Treecell(pymenu.sdesc,pymenu.icon))
      treerow.onDoubleClick = { Event e ->
        rootItem.setOpen(!rootItem.isOpen())
      }
    }
    treerow.setParent(rootItem)

    if (pymenu.mx.equals(1)){
      rootItem.onDoubleClick = { Event e ->
        initTab (maintab,pymenu,true)
      }
    }


    if (pymenu.mx.equals(0) && pymenu.js.equals(0)){
      treeMenu.getTreechildren().appendChild(rootItem)
      Treechildren treechildren  = new Treechildren()
      treechildren.setParent (rootItem)
      def pymenus = Pymenu.findAllByJsAndMx(pymenu.js+1,0,[cache:true])
      pymenus.each {
        initSubMenu(rootItem,it)
      }
    }

  }

  def initSubMenu(Treeitem treeitem,Pymenu pymenu ){
    Treeitem   rootItem = new Treeitem()
    rootItem.setId("treeitem_${pymenu.fbh}")
    rootItem.setValue(pymenu)
    rootItem.setOpen(false)

    Treerow treerow  =  new Treerow()
    if (pymenu.mx == 1) {
      treerow.appendChild(new Treecell(pymenu.sdesc))
    }else{
      treerow.appendChild(new Treecell(pymenu.sdesc,pymenu.icon))
      treerow.onDoubleClick = { Event e ->
        rootItem.setOpen(!rootItem.isOpen())
      }
    }
    treerow.setParent(rootItem)

    Treechildren treechildren = treeitem.getTreechildren()
    if (treechildren == null) {
      treechildren = new Treechildren()
      treechildren.setParent(treeitem)
    }
    treechildren.appendChild(rootItem)
    if (pymenu.mx.equals(1)){
      rootItem.onDoubleClick = { Event e ->
        initTab (maintab,pymenu,true)
      }
    }else if (pymenu.mx.equals(0)){
      def pymenus = Pymenu.findAllByJsAndFbhLike(pymenu.js+1,pymenu.fbh+"%",[cache:true])
      pymenus.each {
        initSubMenu(rootItem,it)
      }
    }

  }

  void initTabContext(Menupopup menupopup) {
    Menuitem menuitem
    menuitem = new Menuitem("关闭")
    menuitem.setId("menuitem_close")
    menuitem.onClick = { Event e ->
      onCloseTab(false)
    }
    menupopup.appendChild(menuitem)
    menuitem = new Menuitem("关闭其他")
    menuitem.setId("menuitem_closeOther")
    menuitem.onClick = { Event e ->
      Tab tab = maintab.getSelectedTab()
      List  children = maintab.getTabs().getChildren()
      List newChildren = new ArrayList()
      List tabpanelChildren = new ArrayList()
      for(Tab t: children){
        if( (!t.getId().equals(tab.getId())) && (t.isClosable())){
          newChildren.add(t)

          tabpanelChildren.add(maintab.getFellow(t.getId().replaceAll("tab","tabpanel")))
        }
      }
      maintab.getTabs().getChildren().removeAll(newChildren)
      maintab.getTabpanels().getChildren().removeAll(tabpanelChildren)
      newChildren.each{
        if (main.getFellow(it.id.replaceAll("tab","menuitem"))!=null && !main.getFellow(it.id.replaceAll("tab","menuitem")).equals("null")) {
          tabMenu.getChildren().remove(main.getFellow(it.id.replaceAll("tab","menuitem")))
        }
      }
      maintab.setSelectedTab(tab)
//      setDisable(maintab.getTabs())
      setBookMark maintab
    }

    menupopup.appendChild(menuitem)

  }

  public void initTab(Tabbox tabbox,Pymenu pymenu,boolean isClosable) {
    if (pymenu) {
      def newTabMenu
      Tabs tabs = tabbox.getTabs();
      Tab tab = maintab.getFellowIfAny("tab_tree_${pymenu.fbh}")
      if (tab == null || tab.equals("null")) {
//        westMenu.setSize("0px")
        tab = new Tab(pymenu.sdesc);
        tab.setId("tab_tree_${pymenu.fbh}")
        tab.setClosable(isClosable);
        tabMenu.append {
          newTabMenu = menuitem(id: "menuitem_tree_${pymenu.fbh}", label: "${pymenu.sdesc}",  onClick: {Event e ->
            maintab.setSelectedTab(tab)
          })
        }
        tab.setContext(tabMenu);
        tabs.appendChild(tab);
//        setDisable(tabs)
        tab.setSelected(true);
        tab.onClose = {Event e ->
          onCloseTab(e.target,false)
        }
        tab.onRightClick = {Event e ->
          maintab.setSelectedTab(tab)
          tabMenu.getChildren().each {
            if (it.id.equals(tab.id.replaceAll("tab","menuitem"))) {
              it.checked = true
              it.checkmark = true
              ((Menuitem)tabMenu.getFellowIfAny("menuitem_close"))?.setDisabled(!tab.isClosable())
            }else{
              it.checked = false
              it.checkmark = false
            }
          }
        }
        /*      tab.onDoubleClick = {Event e ->
          if (westMenu.isOpen()) {
            westMenu.setSize("0px")
            westMenu.setSplittable(false)
            westMenu.setOpen(false)
            westMenu.setAutoscroll(false)
          }else{
            westMenu.setSplittable(true)
            westMenu.setSize("200px")
            westMenu.setOpen(true)
            westMenu.setAutoscroll(true)
          }

        }*/
        if ( isClosable){
          tab.onClick = {Event e->
            Treeitem ti = main.getFellowIfAny("treeitem_tree_${pymenu.fbh}")
            if ( ti != null ) {
              treeMenu.setSelectedItem (ti)
            }
          }
        }

        Tabpanels tabpanels = tabbox.getTabpanels();
        tabpanels.setHeight("100%");

        Tabpanel tabpanel = new Tabpanel();
        tabpanel.setId("tabpanel_tree_${pymenu.fbh}")
        //      tabpanel.appendChild(toolbars)
        tabpanels.appendChild(tabpanel);
        createTabsGridView(tabpanel, pymenu)

        //      tabpanel.setVflex("1");
        tab.getDesktop().setBookmark(tab.getId())
      } else {
        maintab.setSelectedTab(tab)
      }
    }
  }

  /**
   * 创建Tabpanel
   */
  public void createTabsGridView(Tabpanel parentTabpanel,Pymenu pymenu) {
    if (pymenu.stype.equalsIgnoreCase(MenuType.MURL)) {
      Window w = Executions.createComponents(Constants.UIDir+pymenu.surl, parentTabpanel, ["wldwzdInstance":pymenu])
      w.setParent(parentTabpanel)
    }
  }

  /**
   * 需要改成和listbox 中的Model 同步，在listbox中修改了数据，在 grid 也同步
   */
  public Menupopup getMenupopup(Grid grid,Listbox listbox,Toolbar toolbar,Tabpanel tabpanel) {
    Menupopup mp = null
    if (!listbox.getContext() && !mp) {
      Vbox vbox  = new Vbox()
      vbox.setParent(tabpanel)
      mp = new Menupopup()
      mp.setHflex("1")
      mp.setId(listbox.id.replaceAll("listbox","mp"))
      toolbar.children.each {
        def menuitem = new Menuitem(it.label,it.image)
        ComponentsCtrl.applyForward(menuitem,"onClick=${it.id}.onClick")
        mp.appendChild(menuitem)
      }
      vbox.appendChild(mp)
      listbox.setContext(mp)
//      grid.setContext(mp)
      mp.setParent(vbox)
    }
//    mp.setPage(listbox.getPage())
    return mp
  }

  def getListbox(bh){
    (Listbox) main.getFellow(bh)
  }

  /**
   * 设置 Tabs 右键菜单是否可用
   */
  public void setDisable(Tabs tabs)  {
    if (tabs.getChildren().size()<=2){
      ((Menuitem)tabMenu.getFellowIfAny("menuitem_closeOther"))?.setDisabled(true)
//      ((Menuitem)tabMenu.getFellowIfAny("menuitem_close"))?.setDisabled(true)
    }else{
      ((Menuitem)tabMenu.getFellowIfAny("menuitem_closeOther"))?.setDisabled(false)

    }
  }
  /**
   * 设置bookmark，关闭了当前的 Tab 后，需要同时调整bookmark
   */
  public void setBookMark(Tabbox tabbox) {
    Tab t = tabbox.getSelectedTab()
    if (t.getId().equals("tab_Welcome")) {
      tabbox.getDesktop().setBookmark("")
    }else{
      tabbox.getDesktop().setBookmark(t.getId())
    }

  }
  public void onCloseTab(boolean b) {
//    if (b) {
    setBookMark maintab
//    }else {
    Tab tab = maintab.getSelectedTab()
    if (tab.id.equals("tab_ihelp")) {
      return
    }
    if (main.getFellow(tab.getId().replaceAll("tab","menuitem")) !=null && !main.getFellow(tab.getId().replaceAll("tab","menuitem")).equals("null")) {
      tabMenu.getChildren().remove(main.getFellow(tab.getId().replaceAll("tab","menuitem")))
    }
    maintab.getTabs().getChildren().remove(tab)
//      Menupopup mpop = main.getFellow(tab.getContext())

    maintab.getTabpanels().getChildren().remove(maintab.getFellow(tab.getId().replaceAll("tab", "tabpanel")))
    maintab.setSelectedIndex(maintab.getTabs().getChildren().size() - 1)
    setBookMark maintab
//    }
  }
  public void onCloseTab(Tab tab,boolean b) {
    if (b) {
      setBookMark maintab
    }else {
      if (main.getFellow(tab.getId().replaceAll("tab","menuitem")) !=null && !main.getFellow(tab.getId().replaceAll("tab","menuitem")).equals("null")) {
        tabMenu.getChildren().remove(main.getFellow(tab.getId().replaceAll("tab","menuitem")))
      }
      maintab.getTabs().getChildren().remove(tab)
//      Menupopup mpop = main.getFellow(tab.getContext())

      maintab.getTabpanels().getChildren().remove(maintab.getFellow(tab.getId().replaceAll("tab", "tabpanel")))
      maintab.setSelectedIndex(maintab.getTabs().getChildren().size() - 1)
      maintab.invalidate()
//      setBookMark maintab
    }
  }

  // todo 对刷新情况，是否要考虑，对当前Tabbox上的所有tab进行初始，还是只对当前的bookmark初始
  public void onBookmarkChange_main(BookmarkEvent event) {
    if (userInfo) {
      String id = event.getBookmark()
      Tab t = (Tab) maintab.getFellowIfAny(id)
      if (t) {
        t.getDesktop().setBookmark(id)
        maintab.setSelectedTab t
      } else if (!t && id.length() > 3) {
        Pymenu zymenu1 = Pymenu.findByFbh(id.substring(3))
        initTab(maintab, zymenu1,true)
//      maintab.getDesktop().setBookmark("")
      } else if (id.equals("")) {
        List children = maintab.getTabs().getChildren()
        List newChildren = new ArrayList()
        List tabpanelChildren = new ArrayList()
        for (Tab t1: children) {
          if (t1.isClosable()) {
            newChildren.add(t1)
            tabpanelChildren.add(maintab.getFellow(t1.getId().replaceAll("tab", "tabpanel")))
          }
        }
        maintab.getTabs().getChildren().removeAll(newChildren)
        maintab.getTabpanels().getChildren().removeAll(tabpanelChildren)
        maintab.setSelectedTab(maintab.getTabs().getChildren().get(0))
      }
    }

  }

  def onSelect_selecttheme(){
    Comboitem comboitem = selecttheme.getSelectedItem()
    if (comboitem.getValue().toString().trim().equals("blue")) {
      org.zkoss.breeze.Themes.setTheme(Executions.getCurrent(), "");  // Classic Blue theme
//      Themes.setTheme(Executions.getCurrent(), Themes.BREEZE_THEME); // breeze
      Executions.sendRedirect("admin.zul");
    }
    if (comboitem.getValue().toString().trim().equals("gray")) {
//      Themes.setTheme(Executions.getCurrent(), "");  // Classic Blue theme
      org.zkoss.zkplus.theme.Themes.setTheme(Executions.getCurrent(), Themes.BREEZE_THEME); // breeze
      Executions.sendRedirect("admin.zul");
    }
//    if (comboitem.getValue().toString().trim().equals("blue")) {
//      Themes.setTheme(Executions.getCurrent(), "");  // Classic Blue theme
//      Themes.setTheme(Executions.getCurrent(), Themes.BREEZE_THEME); // breeze
//      Executions.sendRedirect("admin.zul");
//    }
  }
  /*public String getFormatString(String domain,String p,String rejectedValue,Object[] b){
    String[] np = new String[b.length];
    String t = "";
    for (int i =0;i<b.length;i++) {
      Object o =  b[i] ;
      if (o instanceof String) {
        t = o.toString();
        if (t.equals(rejectedValue)) {
          np[i] = rejectedValue;
        }else{
          np[i] = getLabel(domain.toLowerCase()+"."+t);
        }
      }else if (o instanceof Class) {
        t =  ((Class) o).getName();
        np[i] = getLabel(t.toLowerCase());
      }

    }
    return MessageFormats.format(p,np);
  }
  public String getLabel(String name) {
    return Labels.getLabel(name.toLowerCase(),name.toLowerCase()) ;
  }*/
}