package com.py110.user

import org.zkoss.zkgrails.GrailsComposer

import com.py110.pubs.ulit.Ulits
import org.zkoss.zul.Toolbarbutton
import org.zkoss.zul.Listbox
import org.zkoss.zk.ui.Executions
import org.zkoss.zul.ListModelList
import org.zkoss.zul.ListitemRenderer
import org.zkoss.zul.Listitem
import org.zkoss.zk.ui.event.InputEvent
import org.jasypt.util.password.BasicPasswordEncryptor
import org.zkoss.zk.ui.event.CheckEvent

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11-8-20
 * Time: 下午3:18
 * To change this template use File | Settings | File Templates.
 */
class PyuserComposer extends GrailsComposer {
    Toolbarbutton tbbfresh,tbbadd,tbbdel,tbbsave
    Listbox listuser
    Pyuser userInfo
    Map args
    def afterCompose = { window ->
        listuser.setVflex(true)
        args = Executions.getCurrent().getArg()
        userInfo = Ulits.getUser()
        if (userInfo != null) {
            listuser.setItemRenderer(new PyuserRenderer())
            listuser.setModel(new ListModelList(Pyuser.listOrderByFbh()))
        }
    }
    def onClick_tbbsave(){
        ListModelList listModelList =  listuser.getModel()
        for (Pyuser pyuser: listModelList) {
            if (pyuser.isDirty() && pyuser.version == null) {
                pyuser.merge(flush:true)
            }else{
                pyuser.save(flush:true)
            }

        }
    }

    public  class PyuserRenderer implements  ListitemRenderer{
        void render(Listitem listitem, Object o) {
            Pyuser pyuser = (Pyuser) o
            listitem.setValue(pyuser)
            listitem.append{
                listcell{
                    textbox(value:pyuser.fbh,inplace:true,readonly:pyuser.version == null?true:false,hflex:"1",onChange:{ InputEvent ie ->
                        pyuser.fbh = ie.getValue()?.toString()
                    })
                }
                listcell{
                    textbox(value:pyuser.fname,inplace:true,hflex:"1",onChange:{ InputEvent ie ->
                        pyuser.fname = ie.getValue()?.toString()
                    })
                }
                listcell{
                    textbox(value:pyuser.fpass?.trim(),type:"password",inplace:true,hflex:"1",onChange:{ InputEvent ie ->
                        pyuser.fpass = new BasicPasswordEncryptor().encryptPassword(ie.getValue()?.toString().trim())    // toDo 加密密码
                    })
                }
                listcell{
                    textbox(value:pyuser.femail?.trim(),inplace:true,hflex:"1",onChange:{ InputEvent ie ->
                        pyuser.femail =ie.getValue()?.toString().trim()    // toDo 加密密码
                    })
                }
                listcell{
                    textbox(value:pyuser.fmobile?.trim(),inplace:true,hflex:"1",onChange:{ InputEvent ie ->
                        pyuser.fmobile = ie.getValue()?.toString().trim()    // toDo 加密密码
                    })
                }
                listcell{
                    textbox(value:pyuser.ftel?.trim(),inplace:true,hflex:"1",onChange:{ InputEvent ie ->
                        pyuser.ftel = ie.getValue()?.toString().trim()    // toDo 加密密码
                    })
                }
                listcell{
                    textbox(value:pyuser.fqq?.trim(),inplace:true,hflex:"1",onChange:{ InputEvent ie ->
                        pyuser.fqq = ie.getValue()?.toString().trim()    // toDo 加密密码
                    })
                }
                listcell{
                    textbox(value:pyuser.fzt?.trim(),inplace:true,hflex:"1",onChange:{ InputEvent ie ->
                        pyuser.fpass = ie.getValue()?.toString().trim()    // toDo 加密密码
                    })
                }
                listcell{
                    checkbox(checked:pyuser.fadmin?.equals('1')?true:false,hflex:"1",onCheck:{ CheckEvent ce->
                        pyuser.fadmin = ce.checked?"1":0
                    })
                }
                listcell{
                    textbox(value:pyuser.fip?.trim(),inplace:true,hflex:"1")
                }
                listcell{
                    textbox(value:pyuser.fnote?.trim(),inplace:true,hflex:"1",onChange:{ InputEvent ie ->
                        pyuser.fnote = ie.getValue()?.toString().trim()    // toDo 加密密码
                    })
                }
                listcell{
                    datebox(value:pyuser.dateCreated,inplace:true,hflex:"1")
                }
                listcell{
                    datebox(value:pyuser.dateCreated,inplace:true,hflex:"1")
                }
            }
        }
    }
}
