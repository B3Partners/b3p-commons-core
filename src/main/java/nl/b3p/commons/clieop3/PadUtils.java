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
 * $Id: PadUtils.java 1401 2005-09-05 16:11:38Z Matthijs $
 */
package nl.b3p.commons.clieop3;

public class PadUtils {

    public static final char[] PADDING_SPACES;
    public static final char[] PADDING_ZEROES;
    private static final int PAD_PART_SIZE = 10;
    

    static {
        PADDING_SPACES = new char[PAD_PART_SIZE];
        fillChars(PADDING_SPACES, PADDING_SPACES.length, ' ');

        PADDING_ZEROES = new char[PAD_PART_SIZE];
        fillChars(PADDING_ZEROES, PADDING_ZEROES.length, '0');
    }

    private static void fillChars(char[] dest, int count, char ch) {
        for (int i = 0; i < count; i++) {
            dest[i] = ch;
        }
    }

    private PadUtils() {
    }

    public static void pad(int count, char[] padding, StringBuffer appendTo) {
        while (count > 0) {
            int padLen = Math.min(count, padding.length);
            appendTo.append(padding, 0, padLen);
            count -= padLen;
        }
    }

    /**
     * Indien str even lang of langer dan length kap str af tot length, anders
     * vul str aan met padding tot length.
     */
    public static void postPadAppend(String str, int length, StringBuffer appendTo, char[] padding) {
        int len = str.length();

        if (len == length) {
            appendTo.append(str);
        } else if (len > length) {
            appendTo.append(str.substring(0, length));
        } else {
            appendTo.append(str);
            pad(length - len, padding, appendTo);
        }
    }

    public static void prePadAppend(String str, int length, StringBuffer appendTo, char[] padding) {
        int len = str.length();

        if (len > length) {
            throw new IllegalArgumentException("value \"" + str + "\" does not fit in field of length " + length);
        }

        if (len < length) {
            pad(length - len, padding, appendTo);
        }
        appendTo.append(str);
    }

    public static void padText(String text, int length, StringBuffer appendTo) {
        postPadAppend(text, length, appendTo, PADDING_SPACES);
    }

    public static void padNumber(String number, int length, StringBuffer appendTo) {
        prePadAppend(number, length, appendTo, PADDING_ZEROES);
    }

    public static void padRecord(StringBuffer appendTo, int length) {
        pad(length - appendTo.length(), PadUtils.PADDING_SPACES, appendTo);
    }
}
