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
 * $Id: MIMETypes.java 2202 2005-12-14 16:14:18Z Matthijs $
 */
package nl.b3p.commons.services;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class MIMETypes {

    private static final Map mimeTypes;
    

    static {
        Map m = new HashMap();

        m.put("asc", "text/plain");
        m.put("bmp", "image/bmp");
        m.put("css", "text/css");
        m.put("doc", "application/msword");
        m.put("dtd", "application/xml-dtd");
        m.put("eps", "application/postscript");
        m.put("gif", "image/gif");
        m.put("htm", "text/html");
        m.put("html", "text/html");
        m.put("jpeg", "image/jpeg");
        m.put("jpg", "image/jpeg");
        m.put("pdf", "application/pdf");
        m.put("png", "image/png");
        m.put("ppt", "application/vnd.ms-powerpoint");
        m.put("ps", "application/postscript");
        m.put("rtf", "text/rtf");
        m.put("tif", "image/tiff");
        m.put("tiff", "image/tiff");
        m.put("txt", "text/plain");
        m.put("xht", "application/xhtml+xml");
        m.put("xhtml", "application/xhtml+xml");
        m.put("xls", "application/vnd.ms-excel");
        m.put("xml", "application/xml");
        m.put("xsl", "application/xml");
        m.put("xslt", "application/xslt+xml");
        m.put("zip", "application/zip");
        m.put("hqx", "application/mac-binhex40");
        m.put("bin", "application/octet-stream");
        m.put("exe", "application/octet-stream");
        m.put("class", "application/octet-stream");
        m.put("so", "application/octet-stream");
        m.put("dll", "application/octet-stream");
        m.put("ogg", "application/ogg");
        m.put("js", "application/x-javascript");
        m.put("swf", "application/x-shockwave-flash");
        m.put("sit", "application/x-stuffit");
        m.put("tar", "application/x-tar");
        m.put("au", "audio/basic");
        m.put("snd", "audio/basic");
        m.put("mid", "audio/midi");
        m.put("midi", "audio/midi");
        m.put("mpga", "audio/mpeg");
        m.put("mp2", "audio/mpeg");
        m.put("mp3", "audio/mpeg");
        m.put("aif", "audio/x-aiff");
        m.put("aiff", "audio/x-aiff");
        m.put("aiffc", "audio/x-aiff");
        m.put("wav", "audio/x-wav");
        m.put("svg", "image/svg+xml");
        m.put("mpeg", "video/mpeg");
        m.put("mpg", "video/mpeg");
        m.put("mpe", "video/mpeg");
        m.put("qt", "video/quicktime");
        m.put("mov", "video/quicktime");
        m.put("avi", "video/x-msvideo");

        mimeTypes = Collections.unmodifiableMap(m);
    }

    /**
     * Geeft een MIME-type terug voor een bepaalde extensie indien bekend.
     * Indien niet bekend geeft null. "ext" zonder punten!
     */
    public static String getMIMETypeForExt(String ext) {
        return (String) mimeTypes.get(ext.trim().toLowerCase());
    }

    /**
     * Zoekt naar een extensie in filename en roept daarmee getMIMETypeForExt
     * aan. Indien filename geen extensie heeft returns null.
     */
    public static String getMIMETypeForFilename(String filename) {
        int dotPos = filename.lastIndexOf('.');
        int dirSepPos = Math.max(filename.lastIndexOf('\\'), filename.lastIndexOf('/'));
        if (dotPos == -1 || dotPos == filename.length() - 1 || dotPos < dirSepPos) {
            return null;
        } else {
            String ext = filename.substring(dotPos + 1);
            return getMIMETypeForExt(ext);
        }
    }
}
