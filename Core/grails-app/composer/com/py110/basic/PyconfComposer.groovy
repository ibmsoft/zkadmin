package com.py110.basic

import org.zkoss.zkgrails.GrailsComposer
import org.zkoss.zul.*
import org.zkoss.zk.ui.event.InputEvent
import org.zkoss.zk.ui.Executions
import com.py110.user.Pyuser
import com.py110.pubs.ulit.Ulits
import com.py110.pubs.ulit.Constants

/**
 * 汇率设置
 */
class PyconfComposer extends GrailsComposer {

    Window parent
    Listbox listboxPane
    Pyuser userInfo
    Toolbarbutton tbbfresh,tbbimport,tbbadd,tbbdel,tbbsave/*,tbbuser*/
    String sortTj = "fvkey"
    def afterCompose = { window ->
        userInfo = Ulits.getUser()
        listboxPane.setNonselectableTags("")
        if (userInfo.fbh != null) {
            listboxPane.setItemRenderer(new HlconfRenderer())
            listboxPane.setModel(new ListModelList(Pyconf.list()))
        }
    }

    def onClick_tbbadd(){
        Pyconf pyconf = new Pyconf()
        pyconf.version = 1
        ListModelList listModelList =  listboxPane.getModel()
        listModelList.add(pyconf)
    }

    def onClick_tbbsave(){
        ListModelList listModelList =  listboxPane.getModel()
        for (Pyconf pyconf: listModelList) {
            if (pyconf.isDirty() && pyconf.version == null) {
                pyconf.merge(flush:true)
            }else{
                pyconf.save(flush:true)
            }

        }
    }

    def onClick_tbbdel(){
        Listitem listitem = listboxPane.getSelectedItem()
        Pyconf pyconf = (Pyconf) listitem.getValue()
        pyconf.delete()
    }
    public class HlconfRenderer implements  ListitemRenderer{
        int i =0
        void render(Listitem listitem, Object o) {
            i++
            Pyconf pyconf = (Pyconf) o
            listitem.setValue(o)
            listitem.append{
                listcell(label:i.toString())
                listcell{
                    textbox(value:pyconf.fvkey,readonly:pyconf.version == 1?false:true,inplace:true,hflex:"1",onChange:{ InputEvent e->
                        pyconf.fvkey = e.value
                    })
                }
                listcell{
                    textbox(value:pyconf.fval,inplace:true,hflex:"1",onChange:{ InputEvent e->
                        pyconf.fval = e.value
                    })
/*
                    switch (pyconf.fvkey){
                        case "ChinaBank_Time":
                            String sV = pyconf.fval?.trim()
                            if ( sV ==null || sV.isEmpty() || sV.equals("")) {
                                pyconf.fval ="12:00:00"
                                pyconf.save(flush:true)
                            }
                            timebox(format:"hh:mm:ss",value:new Date().parse("yyyy-MM-dd hh:mm:ss","2011-01-01 ${pyconf.fval}"),inplace:true,hflex:"1",onChange:{ InputEvent e->
                                pyconf.fval = e.value
                            })
                            break;
                        default :
                            textbox(value:pyconf.fval,inplace:true,hflex:"1",onChange:{ InputEvent e->
                                pyconf.fval = e.value
                            })
                            break
                    }
*/

                }
                listcell{
                    textbox(value:pyconf.fnote,inplace:true,hflex:"1",onChange:{ InputEvent e->
                        pyconf.fnote = e.value
                    })
                }
            }
        }

    }
}
