package com.py110.pubs.help;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: Administrator
 * Date: 2010-1-13
 * Time: 15:16:07
 */
public class HelpWindow extends Window {
    private ArrayList<String> helpDomain /*= new ArrayList<String>();*/;
    private String bh;
    private String mc;
    private Object selectedRowData;
    private Listbox LISTBOX ;
    private String sHelpTitle;


    private static final Listhead listhead = new Listhead();
    private static final Listheader bhListheader = new Listheader("编号");
    private static final Listheader mcListheader = new Listheader("名称");
    private Component component ;
    private Toolbar toolbar;
    private Toolbarbutton okToolbarbutton, cancelToolbarbutton;
    public HelpWindow(Page page){
//        setGrailsApplication(grailsApplication);
        setPage(page);
        LISTBOX = new Listbox();
        doCreateToolbar();
//        appendChild(new Tool)
//        setHelpDomain(helpDomain);
//        LISTBOX.getChildren().clear();
        this.appendChild(LISTBOX);
        init();
    }

    public HelpWindow(Event e) {
        setPage(e.getPage());
        setComponent(e.getTarget());
        init();
    }


    public HelpWindow(Page page, Component component){
//        LISTBOX.getChildren().clear();
        setPage(page);
        init();
    }

    public void doCreateToolbar() {
        toolbar = new Toolbar();
        okToolbarbutton = new Toolbarbutton("确定");
        okToolbarbutton.setImage("images/icons/accept.png");
        cancelToolbarbutton = new Toolbarbutton("取消");
        cancelToolbarbutton.setImage("images/icons/cancel.png");
        toolbar.appendChild(okToolbarbutton);
        toolbar.appendChild(cancelToolbarbutton);
        appendChild(toolbar);
        okToolbarbutton.addEventListener(Events.ON_CLICK, new EventListener() {
            public void onEvent(Event event) throws Exception {
                if (LISTBOX.getSelectedItem() == null) {
                    Messagebox.show("请先选择一条数据！", "提示", Messagebox.OK, Messagebox.INFORMATION, 0, null);
                    return;
                }
                setCompValue();
            }
        });

        cancelToolbarbutton.addEventListener(Events.ON_CLICK, new EventListener() {
            public void onEvent(Event event) throws Exception {
                 Events.postEvent(Events.ON_CLOSE, LISTBOX.getParent(), null);
            }
        });
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }
    private void setComponent(Component component) {
        this.component = component;
    }

    public void setPage(Page page){
        super.setPage(page);
    }

    public void setHelpDomain(ArrayList helpDomain){
        this.helpDomain = helpDomain;
    }

    public void setModel(ListModel model){
        LISTBOX.setModel(model);
    }
    public ListModelList getModel(){
        return (ListModelList)LISTBOX.getModel();
    }

    public void clearModel() {
        if (LISTBOX.getModel() !=null) {
            ( (ListModelList)LISTBOX.getModel()).clear();
        }
    }

    private void setBh(String bh){
        this.bh = bh;
    }
    public String getBh(){
        return this.bh;
    }

    public void init() {
        setClosable(true);
        setTitle(sHelpTitle==null || sHelpTitle.equals("")?"帮助窗口":sHelpTitle);
        setBorder("normal");
        setWidth("400px");
        setHeight("400px");
//        try {
////            bhListheader.setSort("client");
////            mcListheader.setSort("client");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (InstantiationException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }

        this.addEventListener(Events.ON_CLOSE, new EventListener() {
            public void onEvent(Event event) throws Exception {
                setCompValue();
            }
        });
        LISTBOX.setVflex("1");
        LISTBOX.setMold("paging");
        LISTBOX.getPagingChild().setMold("os");
        LISTBOX.addEventListener(Events.ON_DOUBLE_CLICK, new EventListener() {
            public void onEvent(Event event) throws Exception {
                /*    Listitem l = (Listitem) LISTBOX.getSelectedItem();
                Object v = l.getValue() ;
                if (v instanceof List) {
                    setBh(((ArrayList)v).get(0).toString());
                    setValue(getBh());
                    Events.postEvent("onClose",LISTBOX.getParent(),null);
                }*/
//                setCompValue();
                Events.postEvent(Events.ON_CLOSE, LISTBOX.getParent(), null);
            }
        });

        LISTBOX.setItemRenderer(new HelpItemRender());

        LISTBOX.appendChild(listhead);
        listhead.appendChild(bhListheader);
        listhead.appendChild(mcListheader);
    }

    public void setCompValue(){
        Listitem l = (Listitem) LISTBOX.getSelectedItem();
        if (l != null) {
            selectedRowData = l.getValue() ;
            if (selectedRowData instanceof List) {
                setBh(((ArrayList)selectedRowData).get(0).toString());
                setMc(((ArrayList)selectedRowData).get(1).toString());
                setValue(getBh());
//            Events.postEvent("onClose",LISTBOX.getParent(),null);
            }
            if (selectedRowData instanceof Map) {
                setBh(((Map)selectedRowData).get("BH").toString());
                setMc(((Map)selectedRowData).get("MC").toString());
                setValue(getBh());
//            Events.postEvent("onClose",LISTBOX.getParent(),null);
            }

        }
    }

    /*   public void loadData(){
        String domain = helpDomain.get(0);
        String bh = helpDomain.get(1);
        String mc = helpDomain.get(2);
//        Class clazz = grailsApplication.getClassForName(domain);

    }*/

    private void setValue(Object v) {
        if (component == null) return;
        if (component instanceof Textbox){
            ((Textbox)(component)).setValue(v.toString());
        }
    }

    public Object getSelectedRowData() {
        return selectedRowData;
    }
}
