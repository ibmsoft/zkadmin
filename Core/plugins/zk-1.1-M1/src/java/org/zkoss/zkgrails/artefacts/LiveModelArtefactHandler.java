/* ComposerArtefactHandler.java

Copyright (C) 2008, 2009 Chanwit Kaewkasi

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

package org.zkoss.zkgrails.artefacts;

import org.codehaus.groovy.grails.commons.*;

/**
 *
 * @author Chanwit Kaewkasi (chanwit@gmail.com)
 *
*/
public class LiveModelArtefactHandler extends ArtefactHandlerAdapter {

    public static final String TYPE = "LiveModel";

    public LiveModelArtefactHandler() {
        super(TYPE, GrailsLiveModelClass.class,
            DefaultGrailsLiveModelClass.class,
            DefaultGrailsLiveModelClass.LIVE_MODEL,
            false);
    }

    @SuppressWarnings("unchecked")
    public boolean isArtefactClass(Class clazz) {
        return super.isArtefactClass(clazz) &&
               !DomainClassArtefactHandler.isDomainClass(clazz);
    }
}
