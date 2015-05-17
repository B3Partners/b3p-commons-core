package nl.b3p.commons.services;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.output.CountingOutputStream;

public class SizeLimitedOutputStream extends CountingOutputStream {

    private long maxBytes = -1;

    public SizeLimitedOutputStream(OutputStream out) {
        super(out);
    }
    
    public SizeLimitedOutputStream(OutputStream out, long maxBytes) {
        super(out);
        this.maxBytes = maxBytes;
    }

    private static void maxBytesExceeded() throws IOException {
        throw new IOException("Maximum number of bytes exceeded.");
    }

    @Override
    public void write(byte[] b) throws IOException {
        if (getByteCount() + b.length > maxBytes) {
            maxBytesExceeded();
        }
        
        super.write(b); 
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {        
        if (getByteCount() + Math.min(b.length - off, len) > maxBytes) {
            maxBytesExceeded();
        }        
        
        super.write(b, off, len);   
    }

    @Override
    public void write(int b) throws IOException {
        if (getByteCount() + 1 > maxBytes) {
            maxBytesExceeded();
        }
        
        super.write(b);
    }
}