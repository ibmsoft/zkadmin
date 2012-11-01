package com.py110.pubs.help;

import groovy.sql.GroovyRowResult;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: Administrator
 * Date: 2010-1-22
 * Time: 16:21:02
 */

class HelpItemRender implements ListitemRenderer {

    public void render(Listitem item, Object data) throws Exception {
        item.setValue(data);
        if (data instanceof List) {
            ArrayList d = (ArrayList) data;
            Listcell bhListcell = new Listcell(d.get(0).toString());
            Listcell mcListcell = new Listcell(d.get(1).toString());
            item.appendChild(bhListcell);
            item.appendChild(mcListcell);
        }
        if (data instanceof Map) {
            GroovyRowResult m = (GroovyRowResult) data;
            Listcell bhListcell = new Listcell(m.get("BH").toString());
            Listcell mcListcell = new Listcell(m.get("MC").toString());
            item.appendChild(bhListcell);
            item.appendChild(mcListcell);
        }
       /* if (data instanceof GroovyRowResult) {
            GroovyRowResult grr = (GroovyRowResult) data;
            Listcell bhListcell = new Listcell(grr.get("BH").toString());
            Listcell mcListcell = new Listcell(grr.get("MC").toString());
            item.appendChild(bhListcell);
            item.appendChild(mcListcell);
        }*/
    }
}