package servlets;

import dto.EMF;
import entities.User;
import interfaces.UserService;
import services.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet(urlPatterns = {"/users", "/admin/users", "/secretaire/users", "/barman/users"})
public class UsersManagementServlet extends HttpServlet {

    private static int roleId(HttpServletRequest req) {
        Object id = req.getSession().getAttribute("roleId");
        return (id instanceof Number) ? ((Number) id).intValue() : 4; // 1=Admin, 2=Secretaire, 3=Barman, 4=User
    }
    private static boolean isAdmin(HttpServletRequest req){ return roleId(req)==1; }
    private static boolean canEdit(HttpServletRequest req){ int r=roleId(req); return r==1 || r==2; }

    private UserService svc(HttpServletRequest req){
        EntityManager em = EMF.getEM();
        req.setAttribute("__em__", em);
        return new UserServiceImpl(em);
    }
    private void closeEm(HttpServletRequest req){
        Object em = req.getAttribute("__em__");
        if (em instanceof EntityManager) ((EntityManager) em).close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(StandardCharsets.UTF_8.name());
        if (roleId(req) > 3) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        String q = req.getParameter("q");

        // status: all | active/actifs | inactive/inactifs
        String s = req.getParameter("status");
        if (s == null) s = "all";
        s = s.toLowerCase();
        Boolean status = ("active".equals(s) || "actifs".equals(s)) ? Boolean.TRUE
                : ("inactive".equals(s) || "inactifs".equals(s)) ? Boolean.FALSE
                : null;

        int page = 1, size = 10;
        try { page = Math.max(1, Integer.parseInt(req.getParameter("page"))); } catch (Exception ignore) {}
        try { size = Math.max(5, Math.min(50, Integer.parseInt(req.getParameter("size")))); } catch (Exception ignore) {}


        java.util.List<String> excl = new java.util.ArrayList<>();
        int rid = roleId(req);
        if (rid == 2) { // secrétaire
            excl.add("ADMIN");
        } else if (rid == 3) { // barman
            excl.add("ADMIN");
            excl.add("SECRETARY");
        }

        UserService us = svc(req);
        try {
            long total = us.count(q, status, excl);
            int totalPages = (int) Math.max(1, (total + size - 1) / size);
            if (page > totalPages) page = totalPages;

            java.util.List<User> users = us.search(q, status, page, size, excl);

            req.setAttribute("users", users);
            req.setAttribute("q", q == null ? "" : q);
            req.setAttribute("status", s);
            req.setAttribute("page", page);
            req.setAttribute("size", size);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("canEdit", canEdit(req));
            req.setAttribute("isAdmin", isAdmin(req));

            // URL cible pour les formulaires POST
            req.setAttribute("postUrl", req.getServletPath());

            req.getRequestDispatcher("/views/users-admin.jsp").forward(req, resp);
        } finally {
            closeEm(req);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (roleId(req) > 3) { resp.sendError(403); return; }

        String action = req.getParameter("action");
        int id = Integer.parseInt(req.getParameter("id"));

        UserService us = svc(req);
        try {
            if ("toggleActive".equals(action) && canEdit(req)) {
                us.toggleActive(id);
            } else if ("recomputeBlacklist".equals(action) && canEdit(req)) {
                us.toggleBlacklist(id);
            } else if ("setRole".equals(action) && isAdmin(req)) {
                us.changeRole(id, req.getParameter("roleName"));
            }
        } finally {
            closeEm(req);
        }

        resp.sendRedirect(req.getRequestURI());
    }
}
