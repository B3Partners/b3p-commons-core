/*
 * $Id: CsvOutputStream.java 2988 2006-03-24 15:57:41Z Matthijs $
 */

package nl.b3p.commons.csv;

import java.io.OutputStreamWriter;
import java.io.IOException;

import java.util.regex.*;

/** Schrijft CSV bestanden volgens RFC4180 (http://www.rfc-editor.org/rfc/rfc4180.txt).
 */
public class CsvOutputStream {
    
    private static final char QUOTE = '"';
    private static final String NEWLINE = "\n";
    
    private OutputStreamWriter output;
    private char separator;
    private boolean quoteAlways;
    
    private Pattern pattern;
    
    public CsvOutputStream(OutputStreamWriter output) {
        this(output, ',', false);
    }
    
    public CsvOutputStream(OutputStreamWriter output, char separator, boolean quoteAlways) {
        this.output = output;
        this.separator = separator;
        this.quoteAlways = quoteAlways;
        
        /* Indien een non-ASCII, CR, LF, separator of QUOTE char dan waarde quoten */
        String regexp = ".*([^\\p{ASCII}]|[\\r\\n" +
            "\\x" + Integer.toHexString(QUOTE) + 
            "\\x" + Integer.toHexString(separator) +
            "])+.*";
        pattern = Pattern.compile(regexp);
    }
    
    public void flush() throws IOException {
        output.flush();
    }
    
    private void writeValue(String value) throws IOException {
        if(value == null) {
			value = quoteAlways ? QUOTE + "" + QUOTE : "";
		} else if(quoteAlways || pattern.matcher(value).matches()) {
            value = QUOTE + value.replaceAll("\\x" + Integer.toHexString(QUOTE), QUOTE + "" + QUOTE) + QUOTE;
        }
        output.write(value);
    }
    
    public void writeRecord(String[] values) throws IOException {
        boolean first = true;
        for(int i = 0; i < values.length; i++) {
            if(first) {
                first = false;
            } else {
                output.write(separator);
            }
            writeValue(values[i]);            
        }
        output.write(NEWLINE);
    }
    
    /* TODO omschrijven naar JUnit test */
    public static void main(String[] args) throws IOException {
        CsvOutputStream csv = new CsvOutputStream(new OutputStreamWriter(System.out));
        csv.writeRecord(new String[] { "test", "t\"est", "tëst", "\"", "\"\"", "\"test\"", "te\nst", "te,st"});
        csv.flush();
    }
}
