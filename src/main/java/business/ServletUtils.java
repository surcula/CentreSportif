package business;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServletUtils {

    /**
     * Forward the request to a jsp page
     * @param request
     * @param response
     * @param contentPath
     * @param dispatcherJSP
     * @throws ServletException
     * @throws IOException
     */
    public static void forwardWithContent(HttpServletRequest request, HttpServletResponse response,String contentPath, String dispatcherJSP) throws ServletException, IOException {
        request.setAttribute("content", contentPath);
        RequestDispatcher dispatcher = request.getRequestDispatcher(dispatcherJSP);
        dispatcher.forward(request, response);
    }

    /**
     * redirect url
     * @param response
     * @param url
     * @throws IOException
     */
    public static void redirectToURL(HttpServletResponse response, String url) throws IOException {
        response.sendRedirect(url);
    }


    /**
     * Forwards the request to the given JSP template with both an error message and a content page.
     *
     * @param request the HTTP servlet request
     * @param response the HTTP servlet response
     * @param errorMessage the error message to display
     * @param contentPath the relative path to the content JSP
     * @param dispatcherJSP the JSP template to use for forwarding
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    public static void forwardWithError(HttpServletRequest request, HttpServletResponse response,
                                        String errorMessage, String contentPath, String dispatcherJSP)
            throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        forwardWithContent(request, response, contentPath, dispatcherJSP);
    }


}
