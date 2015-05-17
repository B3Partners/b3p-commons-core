package nl.b3p.commons.uploadProgress;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Original : plosson on 04-janv.-2006 9:59:27 - Last modified  by $Author: plosson $ on $Date: 2006/01/05 10:09:38 $
 * @version 1.0 - Rev. $Revision: 1.1 $
 */
public interface OutputStreamListener
{
    public void start();
    public void bytesRead(int bytesRead);
    public void error(String message);
    public void done();
}
