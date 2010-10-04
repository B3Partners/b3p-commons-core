/**
 * B3partners B.V. http://www.b3partners.nl
 * @author Roy
 * Created on 1-okt-2010, 17:26:28
 */

package nl.b3p.commons.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Servlet for getting files from the host. A bit insecure ;)
 */
public class GetServiceFileServlet extends HttpServlet {
    private static Log log = LogFactory.getLog(GetServiceFileServlet.class);
    public static final String FILE="file";


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        // Zet de logger
        if (log.isInfoEnabled()) {
            log.info("Initializing GetServiceFileServlet servlet");
        }
        if (!(this instanceof GetServiceFileSecureServlet))
            log.warn("Insecure file getter. Recommending the use of GetServiceFileSecureServlet");
    }
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        String fileString=request.getParameter(FILE);
        if (fileString==null || fileString.length()==0){
            writeErrorMessage(response, "No '"+FILE+"' parameter given.");
            return;
        }
        File file = getCorrectedFile(fileString);
        if (file!=null){
            if (file.isDirectory()){
                //write dir to html
                writeDirToHtml(request,response,file);
            }else{
                writeFile(response,file);
            }
        }else{
            writeFileNotFoundMessage(response,fileString);
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
     * write a file to the output
     */
    protected void writeFile(HttpServletResponse response, File file) throws IOException{
        byte[] buffer = new byte[8192];
        OutputStream out= response.getOutputStream();
        //write headers etc.
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\";");
        response.setContentLength((int) file.length());
        //write file
        FileInputStream inputStream = new FileInputStream(file);
        int len;
        while ((len = inputStream.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        inputStream.close();
        out.close();
    }
    /**
     * Write a html page as error
     **/
    protected void writeErrorMessage(HttpServletResponse response, String message) throws IOException {
       response.setContentType("text/html");
       ServletOutputStream out = response.getOutputStream();
       out.println("<html>"
               + "<head>"
               + "<title>Error</title>"
               + "</head>"
               + "<body>");
       out.println("<h1>Error getting file</h1>");
       out.println("Cause: <br>");
       out.println("<b>"+message+ "</b>");
       out.println("</body></html>");
       out.close();
    }

    /**
     *Write a html page with all the files in this dir as html link (a href)
     **/
    private void writeDirToHtml(HttpServletRequest request,HttpServletResponse response, File dir) throws IOException {
       response.setContentType("text/html");
       ServletOutputStream out = response.getOutputStream();
       out.println("<html>"
               + "<head>"
               + "<title>Directory List</title>"
               + "</head>"
               + "<body>");
       File[] files = dir.listFiles();
       for (File f :files){
           out.println("<a href=\"" +createFileUrl(request,f)+"\">"+f.getName()+"</a><br/>");
       }
       out.println("</body></html>");
       out.close();
    }
    /**
     * Create the url to the file
     */
    protected String createFileUrl(HttpServletRequest request,File f){
        String fileUrl = request.getRequestURI().toString();
        fileUrl+=fileUrl.contains("?") ?  "&":"?"; 
        fileUrl+=FILE+"="+f.getAbsolutePath();
        return fileUrl;
    }
    /**
     * Write a link to the file in html
     */
    protected void writeFileToHtml(HttpServletRequest request,HttpServletResponse response, File file) throws IOException {
       response.setContentType("text/html");
       ServletOutputStream out = response.getOutputStream();
       out.println("<html>"
               + "<head>"
               + "<title>Code generated</title>"
               + "</head>"
               + "<body>");
       out.println("<a href=\"" +createFileUrl(request,file)+"\">"+file.getName()+"</a>");
       out.println("</body></html>");
       out.close();
    }
    /**
     * Get the corrected File (relative to jvm or absolute)
     */
    protected File getCorrectedFile(String fileString){
        File file = new File(fileString);
        File relativeFile=null;
        if (!file.exists()){
            relativeFile = new File(getServletContext().getRealPath(fileString));
            if (relativeFile.exists()){
                file=relativeFile;
            }
        }
        if (file.exists())
            return file;
        else
            return null;
    }
    /**
     * Write a file not found error message
     */
    protected void writeFileNotFoundMessage(HttpServletResponse response, String fileString) throws IOException {
        File file=new File(fileString);
        File relativeFile= new File(getServletContext().getRealPath(fileString));
        writeErrorMessage(response,"File: '"+file.getAbsolutePath()+"' not found"
                    + " and <br>"
                    + "File: '"+relativeFile.getAbsolutePath()+"' not found");
    }
  
}
