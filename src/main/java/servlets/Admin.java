package servlets;

import business.ServletUtils;

import javax.mail.Session;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import static constants.Rooting.*;

@WebServlet(name = "Admin", value = "/admin")
public class Admin extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String role = (session != null && session.getAttribute("role") != null)
                ? session.getAttribute("role").toString()
                : null;

        if(ServletUtils.isFullAuthorized(role)){
            request.setAttribute("content", "/views/adminHub.jsp");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/template/template.jsp");
            dispatcher.forward(request, response);
        }else{
            ServletUtils.redirectWithMessage(request,
                    response,
                    "Vous n'avez pas l'autorisation.",
                    "error",
                    "/home");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
 