package servlets;

import dto.EMF;
import entities.Subscription;

import javax.persistence.EntityManager;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet(name = "SubscriptionSearchServlet", urlPatterns = {"/api/search/subscriptions"})
public class SubscriptionSearchServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("application/json; charset=UTF-8");

        String q = req.getParameter("q"); if (q == null) q = ""; q = q.trim().toLowerCase();
        int limit = 10; try { limit = Integer.parseInt(req.getParameter("limit")); } catch (Exception ignore) {}
        limit = Math.max(1, Math.min(50, limit));

        EntityManager em = EMF.getEM();
        try {
            List<Subscription> subs = em.createQuery(
                            "SELECT s FROM Subscription s " +
                                    "WHERE LOWER(s.subscriptionName) LIKE :q " +
                                    "ORDER BY s.subscriptionName ASC", Subscription.class)
                    .setParameter("q", "%" + q + "%")
                    .setMaxResults(limit)
                    .getResultList();

            StringBuilder json = new StringBuilder("[");
            boolean first = true;
            for (Subscription s : subs) {
                String label = s.getSubscriptionName();
                if (!first) json.append(',');
                json.append("{\"id\":").append(s.getId())
                        .append(",\"label\":\"").append(esc(label)).append("\"}");
                first = false;
            }
            json.append("]");
            resp.getWriter().write(json.toString());
        } finally { if (em != null) em.close(); }
    }
    private static String esc(String s){ if (s==null) return ""; return s.replace("\\","\\\\").replace("\"","\\\"").replace("\n","\\n").replace("\r","\\r"); }
}
