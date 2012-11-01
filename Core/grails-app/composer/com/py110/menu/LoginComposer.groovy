package com.py110.menu


import org.jasypt.util.password.BasicPasswordEncryptor
import javax.servlet.ServletRequest
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession
import org.zkoss.web.fn.ServletFns
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.Session
import org.zkoss.zk.ui.util.Clients
import org.zkoss.zkgrails.GrailsComposer
import org.zkoss.zul.*
import com.py110.user.Pyuser
import com.py110.pubs.help.LsconfService
import com.py110.pubs.ulit.Constants

class LoginComposer extends GrailsComposer  {
    Button login
    Textbox name
    Textbox password
    Datebox loginDate
    Session session
    HttpSession httpSession
    Window loginMain
    Pyuser  userInfo
    String errtext = ""
    String userName
    String userPassword
    String sip,epass
    LsconfService lsconfService
    def afterCompose = { window ->
        sip = ServletFns.getCurrentRequest().getRemoteAddr()
    }
    void onClick_login(){
        try{
            Clients.showBusy (message("loading.confirm"),true)
            boolean  ibPass  = false
            userName = name.getValue()
            userPassword = password.getValue()
            userInfo = Pyuser.findByFbh(userName)
            if (!userInfo){
                errtext = message("user.not.found")
                Clients.clearBusy()
            }else {
                BasicPasswordEncryptor encryptor  = new BasicPasswordEncryptor()
                if (encryptor.checkPassword(userPassword,userInfo.fpass)) {
                    ServletRequest servletRequest = (HttpServletRequest) Executions.getCurrent().getNativeRequest()
                    servletRequest.getSession(true).setAttribute("user", userInfo)
                    ibPass = true
                }else {
                    errtext = message("user.password.incorrect")
                }
            }
            if (userInfo && ibPass){
                Map configMap = lsconfService.getConfigMap()
                session  = Executions.getCurrent().getSession()
                session.setAttribute(Constants.LOGIN_USER,userInfo)
                session.setAttribute(Constants.LOGON_CONFIG,configMap)
                session.setAttribute(Constants.LOGON_DATE,loginDate.getValue())
                Executions.sendRedirect ("admin.zul")
                Clients.showBusy (message("loading.confirm"),false)
            }else {
                Clients.showBusy (message("loading.confirm"),false)
                Messagebox.show(errtext)
            }
        }catch (Exception e){
            Clients.clearBusy()
        }
    }

    void onOK_password(){
        onClick_login()
    }

    void onClick_sign () {

    }

}