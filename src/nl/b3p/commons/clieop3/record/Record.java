/*
 * $Id: Record.java 2202 2005-12-14 16:14:18Z Matthijs $
 */

package nl.b3p.commons.clieop3.record;

import nl.b3p.commons.clieop3.PadUtils;
import nl.b3p.commons.clieop3.ClieOp3OutputStream;

public abstract class Record {
    
    private String recordCode;
    private char variantCode;
    
    public Record(String recordCode, char variantCode) {
        this.recordCode = recordCode;
        this.variantCode = variantCode;
    }
    
    public String getRecordCode() {
        return recordCode;
    }
    
    public char getVariantCode() {
        return variantCode;
    }
    
    protected abstract void appendRecordContents(StringBuffer buf);
    
    public String getRecordData() {
        StringBuffer buf = new StringBuffer(ClieOp3OutputStream.RECORD_LENGTH);
        buf.append(recordCode);
        buf.append(variantCode);
        appendRecordContents(buf);
        PadUtils.padRecord(buf, ClieOp3OutputStream.RECORD_LENGTH);
        return buf.toString();
    }
}
