package business;

import Tools.Result;
import com.sun.deploy.net.HttpRequest;
import entities.Hall;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServletUtils {
    // Log4j
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ServletUtils.class);

    /**
     * Forward the request to a jsp page
     * @param request
     * @param response
     * @param contentPath
     * @param dispatcherJSP
     * @throws ServletException
     * @throws IOException if an I/O error occurs
     */
    public static void forwardWithContent(HttpServletRequest request, HttpServletResponse response,String contentPath, String dispatcherJSP)
            throws ServletException, IOException {
        request.setAttribute("content", contentPath);
        RequestDispatcher dispatcher = request.getRequestDispatcher(dispatcherJSP);
        dispatcher.forward(request, response);
    }

    /**
     * redirect url
     * @param response
     * @param url
     * @throws IOException if an I/O error occurs
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
        log.warn(errorMessage);
        request.setAttribute("errorMessage", errorMessage);
        forwardWithContent(request, response, contentPath, dispatcherJSP);
    }

    /**
     * Forwards the request to the given JSP template with both errorS message and a content page.
     *
     * @param request the HTTP servlet request
     * @param response the HTTP servlet response
     * @param errorsMessages the error message to display
     * @param contentPath the relative path to the content JSP
     * @param dispatcherJSP the JSP template to use for forwarding
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    public static void forwardWithErrors(HttpServletRequest request, HttpServletResponse response,
                                         Map<String,String> errorsMessages, String contentPath, String dispatcherJSP)
            throws ServletException, IOException {
        request.setAttribute("successMessage", null);

        for(Map.Entry<String, String> entry : errorsMessages.entrySet()) {
            request.setAttribute(entry.getKey(), entry.getValue());
        }
        forwardWithContent(request, response, contentPath, dispatcherJSP);
    }

    /**
     * Forwards the request to the given JSP template with both success message and a content page.
     *
     * @param request the HTTP servlet request
     * @param response the HTTP servlet response
     * @param successMessage the error message to display
     * @param contentPath the relative path to the content JSP
     * @param dispatcherJSP the JSP template to use for forwarding
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    public static void forwardWithSucces(HttpServletRequest request, HttpServletResponse response,
                                         String successMessage, String contentPath, String dispatcherJSP)
            throws ServletException, IOException {
        request.setAttribute("successMessage", successMessage);
        forwardWithContent(request, response, contentPath, dispatcherJSP);
    }

    /**
     * verify the role
     * @param role
     * @return
     */
    public static boolean isFullAuthorized(String role){
        if (role == null) return false;

        switch (role.toUpperCase()) {
            case "ADMIN":
            case "SECRETARY":
            case "BARMAN":
                return true;
            default:
                return false;
        }
    }

    /**
     * Redirect with message error or success
     * @param request
     * @param response
     * @param message
     * @param type success or error
     * @param targetPath url/path
     * @throws IOException
     */
    public static void redirectWithMessage(HttpServletRequest request, HttpServletResponse response,
                                           String message, String type, String targetPath) throws IOException {
        request.getSession().setAttribute("toastMessage", message);
        request.getSession().setAttribute("toastType", type); // "success" ou "error"
        response.sendRedirect(request.getContextPath() + targetPath);
    }

    /**
     * Redirect NoAuhtorized
     * @param request
     * @param response
     * @throws IOException
     */
    public static void redirectNoAuthorized(HttpServletRequest request, HttpServletResponse response) throws IOException {
        redirectWithMessage(request,
                response,
                "Vous n'avez pas l'autorisation",
                "error",
                "/home");
    }

    /**
     * change le active
     * @param active
     * @return !active
     */
    public static boolean changeActive(boolean active){

        return !active;
    }

    /**
     * String To Integer
     * @param input
     * @return
     */
    public static Result<Integer> stringToInteger(String input) {
        try{
            return Result.ok(Integer.parseInt(input));
        }catch (NumberFormatException e) {
            Map<String, String> errors = new HashMap<>();
            errors.put("parseInt", "Impossible de convertir '" + input + "' en nombre.");
            return Result.fail(errors);
        }
    }
}
