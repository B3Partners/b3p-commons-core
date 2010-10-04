/**
 * B3partners B.V. http://www.b3partners.nl
 * @author Roy
 * Created on 1-okt-2010, 17:26:28
 *
 */
package nl.b3p.commons.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Servlet for getting files secure from the host.
 */
public class GetServiceFileSecureServlet extends GetServiceFileServlet {
    private static Log log = LogFactory.getLog(GetServiceFileSecureServlet.class);
    private static final String HASHSTRING="secretHashString";
    private static final String CODE="code";

    private static String hashString=null;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        // Zet de logger
        if (log.isInfoEnabled()) {
            log.info("Initializing Secure File Getter");
        }
        hashString=config.getInitParameter(HASHSTRING);
        if (hashString==null || hashString.length()==0){
            hashString=null;
            log.error("SecretHashString initParameter not set !!!");
        }
        
    }
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException, UnsupportedEncodingException{
        if (hashString==null){
            log.error("SecretHashString initParameter not set !!!");
            writeErrorMessage(response, "Servlet not configured properly, see log file");
            return;
        }
        String code=request.getParameter(CODE);
        String fileString=request.getParameter(FILE);
        //Check if the file is found
        File file = getCorrectedFile(fileString);
        if (file==null){
            writeFileNotFoundMessage(response, fileString);
            return;
        }
        /*if no code, then check if the requested url starts with http://localhost
         * for allowing this on localhost
         */
        if (code==null || code.length()==0){
            if (request.getRequestURL().toString().startsWith("http://localhost")){
                writeFileToHtml(request, response, file);
                return;
            }
        }
        String hashedFile;
        try {
            hashedFile = getFileHash(fileString);
            if (hashedFile.equals(code)){
                super.processRequest(request,response);
            }else{
                writeErrorMessage(response,"Access not allowed for this file.");
            }
        }
        catch (NoSuchAlgorithmException ex) {
            throw new ServletException("Error while getting file, cause: ",ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    /**
     * Helper to create coded getters
     */
    public static void main (String args[]) throws NoSuchAlgorithmException, UnsupportedEncodingException, IOException{
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        System.out.println("Secret HashString: ");
        String hash=br.readLine();

        System.out.println("File path: ");
        String filePath = br.readLine();

        String code=getHash(hash+filePath);
        String params="?"+FILE+"="+filePath + "&"+CODE+"="+code;
        System.out.println(params);        
        
    }
    /**
     * Return the hash for this file
     **/
    private String getFileHash(String filePath) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        return getHash(hashString+filePath);
    }

    /**
     * Create and return hash
     */
    private static String getHash(String stringToHash) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(stringToHash.getBytes("UTF-8"));
        byte[] md5hash = md.digest();
        return new String(Hex.encodeHex(md5hash));        
    }
    /**
     * Create file url with code
     */
    @Override
    protected String createFileUrl(HttpServletRequest request,File f){
        String url=super.createFileUrl(request,f);
        try{
            url+="&"+CODE+"="+getFileHash(f.getAbsolutePath());
        }catch(Exception e){
            log.error("Error while creating file url");            
        }
        return url;
    }
}
