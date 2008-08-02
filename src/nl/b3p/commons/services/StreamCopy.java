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

/*
 * $Id: StreamCopy.java 2069 2005-12-01 16:42:03Z Matthijs $
 */
package nl.b3p.commons.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class StreamCopy {

    private StreamCopy() {
    }

    public static long copy(InputStream in, OutputStream out) throws IOException {
        long bytesCopied = 0;
        int read = -1;
        byte[] buf = new byte[8192];

        while ((read = in.read(buf, 0, buf.length)) != -1) {
            out.write(buf, 0, read);
            bytesCopied += read;
        }
        return bytesCopied;
    }
}
