/*
 * $Id: StreamCopy.java 2069 2005-12-01 16:42:03Z Matthijs $
 */

package nl.b3p.commons.services;

import java.io.*;

public class StreamCopy {

    private StreamCopy() {
    }

    public static long copy(InputStream in, OutputStream out) throws IOException {
        long bytesCopied = 0;
        int read = -1;
        byte[] buf = new byte[8192];

        while((read = in.read(buf, 0, buf.length)) != -1) {
            out.write(buf, 0, read);
            bytesCopied += read;
        }
        return bytesCopied;
    }
}
