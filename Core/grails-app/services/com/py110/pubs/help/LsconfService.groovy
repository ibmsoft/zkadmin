package com.py110.pubs.help

import com.py110.basic.Pyconf
import com.py110.pubs.ulit.Constants

/**
 * 取参数配置服务
 */
class LsconfService {

    static transactional = true

    public String getValueByKey(String vkey){
        Pyconf pyconf = Pyconf.find(" from Cuconf where fvkey = ? ",[vkey])
        if (pyconf != null) {
            return pyconf.fval
        }else{
            return ""
        }
    }


    /**
     * 取设置参数信息
     * @return
     */
    public Map getConfigMap() {
        def configMap = [:]
        List<Pyconf> list = Pyconf.list()
        for (Pyconf l: list) {
            configMap.put(l.fvkey?.trim(),l.fval?.trim())
        }
        if (configMap.containsKey(Constants.BD_CWRQ)) {
            String stemp = configMap.get(Constants.BD_CWRQ)
            configMap.put(Constants.gsCurCwrq,stemp)
            configMap.put(Constants.gsYear,stemp.substring(4))
            configMap.put(Constants.gsMon,stemp.substring(5,6))
        }
        return configMap
    }


}
