package servlets;

import dto.EMF;
import entities.User;

import javax.persistence.EntityManager;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet(name = "UserSearchServlet", urlPatterns = {"/api/search/users"})
public class UserSearchServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("application/json; charset=UTF-8");

        String q = req.getParameter("q"); if (q == null) q = ""; q = q.trim().toLowerCase();
        int limit = 10; try { limit = Integer.parseInt(req.getParameter("limit")); } catch (Exception ignore) {}
        limit = Math.max(1, Math.min(50, limit));

        EntityManager em = EMF.getEM();
        try {
            List<User> users = em.createQuery(
                            "SELECT u FROM User u " +
                                    "WHERE LOWER(CONCAT(COALESCE(u.firstName,''),' ',COALESCE(u.lastName,''))) LIKE :q " +
                                    "   OR LOWER(u.email) LIKE :q " +
                                    "ORDER BY u.lastName ASC, u.firstName ASC", User.class)
                    .setParameter("q", "%" + q + "%")
                    .setMaxResults(limit)
                    .getResultList();

            StringBuilder json = new StringBuilder("[");
            boolean first = true;
            for (User u : users) {
                String label = ((u.getFirstName()==null?"":u.getFirstName())+" "+(u.getLastName()==null?"":u.getLastName())).trim();
                if (label.isEmpty()) label = "User #"+u.getId();
                if (u.getEmail()!=null && !u.getEmail().isEmpty()) label += " — " + u.getEmail();
                if (!first) json.append(',');
                json.append("{\"id\":").append(u.getId())
                        .append(",\"label\":\"").append(esc(label)).append("\"}");
                first = false;
            }
            json.append("]");
            resp.getWriter().write(json.toString());
        } finally { if (em != null) em.close(); }
    }
    private static String esc(String s){ if (s==null) return ""; return s.replace("\\","\\\\").replace("\"","\\\"").replace("\n","\\n").replace("\r","\\r"); }
}
