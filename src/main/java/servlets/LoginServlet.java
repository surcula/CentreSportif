package servlets;

import business.ServletUtils;
import dto.EMF;
import entities.User;

import javax.servlet.ServletException;
import javax.persistence.NoResultException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import javax.persistence.EntityManager;

/**
 * Servlet de connexion.
 */
@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    private static final String TEMPLATE = "/views/template/template.jsp";
    private static final String LOGIN_JSP = "/views/login.jsp";

    /**
     * @param request Soumission du formulaire de connexion
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       // invalide toute les sessions existante
        HttpSession old = request.getSession(false);
        if (old != null) old.invalidate();

        final String email    = request.getParameter("email");
        final String password = request.getParameter("password");
         // champs requis
        if (email == null || email.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            ServletUtils.forwardWithError(request, response,
                    "Veuillez remplir tous les champs", LOGIN_JSP, TEMPLATE);
            return;
        }

        EntityManager em = EMF.getEM();
        try {
            User user;
            try {
                user = em.createNamedQuery("User.findByEmailWithRole", User.class)
                        .setParameter("email", email)
                        .getSingleResult();
            } catch (NoResultException nre) {
                user = null;
            }

            if (user == null) {
                ServletUtils.forwardWithError(request, response,
                        "Identifiants invalides", LOGIN_JSP, TEMPLATE);
                return;
            }

            // Vérification du mot de passe
            String inputHash = business.UserBusiness.hashPasswordSha256(password);
            boolean ok = (user.getPassword() != null) && inputHash.equals(user.getPassword());


            if (!ok) {
                ServletUtils.forwardWithError(request, response,
                        "Identifiants invalides", LOGIN_JSP, TEMPLATE);
                return;
            }

            // Nouvelle session authentifiée
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", user.getId());
            // role
            Integer roleId = (user.getRole() != null) ? user.getRole().getId() : 4; // 4 = utilisateur
            session.setAttribute("roleId", roleId);

            String role;
            switch (roleId) {
                case 1: role = "ADMIN";      break;
                case 2: role = "SECRETARY"; break;
                case 3: role = "BARMAN";     break;
                default: role = "USER";      break;
            }
            session.setAttribute("role", role);
            // redirection selon le role
            String target = ("ADMIN".equals(role) || "SECRETARY".equals(role) || "BARMAN".equals(role))
                    ? "/home"
                    : "/users";
            ServletUtils.redirectToURL(response, request.getContextPath() + target);
        } finally {
            em.close();
        }
    }

    /**
     * @param request
     * @param response Affiche le formulaire ou traite la déconnexion.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        final String action = request.getParameter("action");

        if ("logout".equals(action)) {
            // deconnexion et détruit la session
            request.getSession().invalidate();
            ServletUtils.redirectToURL(response,request.getContextPath() + "/login");

        } else {
            //Affiche la page de connexion
            ServletUtils.forwardWithContent(request, response, LOGIN_JSP, TEMPLATE);
        }
    }
}
