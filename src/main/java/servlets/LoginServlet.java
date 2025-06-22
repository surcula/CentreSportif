package servlets;

import business.ServletUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    private static final String TEMPLATE = "/views/template/template.jsp";
    private static final String LOGIN_JSP = "/views/login.jsp";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userIdParam = request.getParameter("userId");
        String role = request.getParameter("role");

        if (userIdParam != null && role != null) {
            try {
                int userId = Integer.parseInt(userIdParam);

                HttpSession session = request.getSession();
                session.setAttribute("userId", userId);
                session.setAttribute("role", role);

                ServletUtils.redirectToURL(response,request.getContextPath() + "/home");
            } catch (NumberFormatException e) {
                ServletUtils.forwardWithError(
                        request,
                        response,
                        "ID invalide",
                        LOGIN_JSP,
                        TEMPLATE
                );

            }
        } else {
            ServletUtils.forwardWithError(
                    request,
                    response,
                    "Veuillez remplir tous les champs",
                    LOGIN_JSP,
                    TEMPLATE
            );

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("logout".equals(action)) {
            request.getSession().invalidate(); // détruit la session
            ServletUtils.redirectToURL(response,request.getContextPath() + "/login");

        } else {
            ServletUtils.forwardWithContent(
                    request,
                    response,
                    LOGIN_JSP,
                    TEMPLATE
            );
        }
    }
}
