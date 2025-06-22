package services;

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

                response.sendRedirect(request.getContextPath() + "/home"); // ou accueil
            } catch (NumberFormatException e) {
                request.setAttribute("error", "ID invalide");
                request.setAttribute("content", "/views/login.jsp");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/views/template/template.jsp");
                dispatcher.forward(request, response);
            }
        } else {
            request.setAttribute("error", "Veuillez remplir tous les champs");
            request.setAttribute("content", "/views/login.jsp");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/template/template.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("logout".equals(action)) {
            request.getSession().invalidate(); // détruit la session
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            request.setAttribute("content", "/views/login.jsp");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/template/template.jsp");
            dispatcher.forward(request, response);
        }
    }
}
