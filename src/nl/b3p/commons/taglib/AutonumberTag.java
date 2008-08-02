/*
 * B3P Commons Core is a library with commonly used classes for webapps.
 * Included are clieop3, oai, security, struts, taglibs and other
 * general helper classes and extensions.
 *
 * Copyright 2000 - 2008 B3Partners BV
 * 
 * This file is part of B3P Commons Core.
 * 
 * B3P Commons Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * B3P Commons Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with B3P Commons Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.b3p.commons.taglib;

import javax.servlet.jsp.JspException;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.taglib.html.*;
import java.util.*;

/**
 * Custom tag for autonumber input field.
 *
 * @author Chris van Lith
 * @version $Revision: 1.1 $ $Date: 2004/05/25 19:41:05 $
 */
public class AutonumberTag extends HiddenTag {
    // ----------------------------------------------------------- Constructors
    /**
     * Construct a new instance of this tag.
     */
    public AutonumberTag() {

        super();

    }
    // ------------------------------------------------------------- Properties
    // --------------------------------------------------------- Public Methods
    /**
     * Generate the required input tag, followed by the optional rendered text.
     * Support for <code>write</code> property since Struts 1.1.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        Object oldvalue = RequestUtils.lookup(pageContext, name, property, null);
        if (oldvalue == null || (oldvalue instanceof String && ((String) oldvalue).length() == 0)) {
            Calendar start = Calendar.getInstance();
            start.clear();
            start.set(2004, 0, 1);
            long startpunt = start.getTimeInMillis();
            long now = (long) ((new Date()).getTime() - startpunt) / 100;
//            value += Long.toHexString(now) + " ("+ now +")";
            value += Long.toString(now, Character.MAX_RADIX).toUpperCase();
        } else {
            value = null;
        }

        // Render the <html:input type="hidden"> tag as before
        return super.doStartTag();

    }

    /**
     * Release any acquired resources.
     */
    public void release() {

        super.release();

    }
}
