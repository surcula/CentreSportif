package servlets;

import Tools.ParamUtils;
import Tools.Result;
import business.ServletUtils;
import business.SubscriptionBusiness;
import controllers.helpers.SubscriptionControllerHelper;
import dto.EMF;
import dto.UsersSubscriptionAssignForm;
import dto.UsersSubscriptionUpdateForm;
import entities.UsersSubscription;
import services.SubscriptionServiceImpl;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static constants.Rooting.SUBSCRIPTION_FORM_JSP;
import static constants.Rooting.SUBSCRIPTION_JSP;
import static constants.Rooting.TEMPLATE;

@WebServlet(name = "SubscriptionServlet", value = "/subscription")
public class SubscriptionServlet extends HttpServlet {

    private static final org.apache.log4j.Logger log =
            org.apache.log4j.Logger.getLogger(SubscriptionServlet.class);

    // ===================== GET =====================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String form = request.getParameter("form");
        String editForm = request.getParameter("editForm");

        String role = (session != null && session.getAttribute("role") != null)
                ? session.getAttribute("role").toString() : null;
        boolean fullAccess = ServletUtils.isFullAuthorized(role);

        // --- Form assignation (staff) ---
        if (form != null) {
            if (!fullAccess) { ServletUtils.redirectNoAuthorized(request, response); return; }
            SubscriptionControllerHelper.handleForm(request, response);
            return;
        }

        // --- Form édition (staff) ---
        if (editForm != null) {
            if (!fullAccess) { ServletUtils.redirectNoAuthorized(request, response); return; }

            Result<Integer> idRes = ParamUtils.verifyId(editForm);
            if (!idRes.isSuccess()) {
                ServletUtils.forwardWithErrors(request, response, idRes.getErrors(), SUBSCRIPTION_JSP, TEMPLATE);
                return;
            }

            EntityManager em = null;
            try {
                em = EMF.getEM();
                SubscriptionServiceImpl service = new SubscriptionServiceImpl(em);
                Result<UsersSubscription> usRes = service.findUsersSubscription(idRes.getData());
                if (usRes.isSuccess()) {
                    SubscriptionControllerHelper.handleEditForm(request, response, usRes.getData());
                } else {
                    ServletUtils.forwardWithErrors(request, response, usRes.getErrors(), SUBSCRIPTION_JSP, TEMPLATE);
                }
            } catch (Exception e) {
                log.error("Erreur chargement abonnement (editForm)", e);
                ServletUtils.forwardWithError(request, response, e.getMessage(), SUBSCRIPTION_JSP, TEMPLATE);
            } finally {
                if (em != null) em.close();
            }
            return;
        }

        // --- Liste abonnements (RBAC) ---
        if (session == null || session.getAttribute("userId") == null) {
            ServletUtils.redirectWithMessage(request, response,
                    "Veuillez vous connecter pour voir vos abonnements.",
                    "warning", "/login");
            return;
        }
        int userId = (int) session.getAttribute("userId");

        EntityManager em = null;
        try {
            em = EMF.getEM();
            SubscriptionServiceImpl service = new SubscriptionServiceImpl(em);

            List<UsersSubscription> data;
            if (fullAccess) {
                // staff : tous (tri sur id)
                data = em.createQuery(
                        "SELECT us FROM UsersSubscription us ORDER BY us.id DESC",
                        UsersSubscription.class
                ).getResultList();
            } else {
                // user : les siens
                Result<List<UsersSubscription>> res = service.findByUser(userId);
                if (!res.isSuccess()) {
                    ServletUtils.forwardWithErrors(request, response, res.getErrors(), SUBSCRIPTION_JSP, TEMPLATE);
                    return;
                }
                data = res.getData();
            }

            request.setAttribute("fullAccess", fullAccess);
            SubscriptionControllerHelper.handleList(request, response, data);

        } catch (Exception e) {
            log.error("Erreur chargement abonnements (liste)", e);
            ServletUtils.forwardWithError(request, response, e.getMessage(), SUBSCRIPTION_JSP, TEMPLATE);
        } finally {
            if (em != null) em.close();
        }
    }

    // ===================== POST =====================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String action = request.getParameter("action");
        String idParam = request.getParameter("usersSubscriptionId");

        String role = (session != null && session.getAttribute("role") != null)
                ? String.valueOf(session.getAttribute("role")) : null;
        boolean fullAccess = ServletUtils.isFullAuthorized(role);

        // A partir de maintenant : actions STAFF uniquement
        if (!(session != null && fullAccess)) {
            ServletUtils.redirectNoAuthorized(request, response);
            return;
        }

        // --- (De)activation rapide ---
        if ("activer".equals(action) || "delete".equals(action)) {
            EntityManager em = null;
            try {
                Result<Integer> idRes = ParamUtils.verifyId(idParam);
                if (!idRes.isSuccess()) {
                    ServletUtils.forwardWithErrors(request, response, idRes.getErrors(), SUBSCRIPTION_JSP, TEMPLATE);
                    return;
                }

                em = EMF.getEM();
                SubscriptionServiceImpl service = new SubscriptionServiceImpl(em);

                em.getTransaction().begin();
                Result<UsersSubscription> usRes = service.findUsersSubscription(idRes.getData());
                if (!usRes.isSuccess()) {
                    ServletUtils.forwardWithError(request, response, "Abonnement introuvable", SUBSCRIPTION_JSP, TEMPLATE);
                    em.getTransaction().rollback();
                    return;
                }
                UsersSubscription us = usRes.getData();
                us.setActive(ServletUtils.changeActive(us.isActive()));
                em.merge(us);
                em.getTransaction().commit();

                ServletUtils.redirectWithMessage(request, response,
                        "Abonnement mis à jour", "success", "/subscription");
                return;

            } catch (Exception e) {
                log.error("Erreur (de)activation abonnement", e);
                ServletUtils.forwardWithError(request, response, e.getMessage(), SUBSCRIPTION_JSP, TEMPLATE);
            } finally {
                // rollback si nécessaire
                // et close
                // (sécurité en cas d'exception)
                //noinspection ConstantConditions
                if (em != null) {
                    if (em.getTransaction().isActive()) em.getTransaction().rollback();
                    em.close();
                }
            }
            return;
        }

        // --- CREATE (assignation STAFF) ---
        if ("assign".equals(action) && idParam == null) {
            EntityManager em = null;
            try {
                String userIdRaw = request.getParameter("userId");
                String subIdRaw  = request.getParameter("subscriptionId");
                if (userIdRaw == null || userIdRaw.isEmpty() || subIdRaw == null || subIdRaw.isEmpty()) {
                    ServletUtils.forwardWithError(request, response,
                            "Sélectionne un utilisateur et un abonnement dans la liste.",
                            SUBSCRIPTION_FORM_JSP, TEMPLATE);
                    return;
                }

                Result<UsersSubscriptionAssignForm> formRes = SubscriptionBusiness.initAssignForm(
                        userIdRaw,
                        subIdRaw,
                        request.getParameter("startDate"),
                        request.getParameter("endDate"),
                        request.getParameter("quantity"),
                        request.getParameter("active")
                );
                if (!formRes.isSuccess()) {
                    ServletUtils.forwardWithErrors(request, response, formRes.getErrors(), SUBSCRIPTION_FORM_JSP, TEMPLATE);
                    return;
                }

                em = EMF.getEM();
                SubscriptionServiceImpl service = new SubscriptionServiceImpl(em);

                em.getTransaction().begin();
                UsersSubscriptionAssignForm f = formRes.getData();
                Result<UsersSubscription> created = service.assignToUser(
                        f.getSubscriptionId(), f.getUserId(), f.getStartDate(), f.getEndDate(), f.getQuantity()
                );
                if (!created.isSuccess()) {
                    em.getTransaction().rollback();
                    ServletUtils.forwardWithErrors(request, response, created.getErrors(), SUBSCRIPTION_FORM_JSP, TEMPLATE);
                    return;
                }
                em.getTransaction().commit();

                ServletUtils.redirectWithMessage(request, response,
                        "Abonnement assigné avec succès", "success", "/subscription");
                return;

            } catch (Exception e) {
                log.error("Erreur assignation abonnement", e);
                ServletUtils.forwardWithError(request, response, e.getMessage(), SUBSCRIPTION_FORM_JSP, TEMPLATE);
            } finally {
                if (em != null) {
                    if (em.getTransaction().isActive()) em.getTransaction().rollback();
                    em.close();
                }
            }
            return;
        }

        // --- UPDATE (prolongation/solde/actif STAFF) ---
        EntityManager em = null;
        try {
            Result<Integer> idRes = ParamUtils.verifyId(idParam);
            if (!idRes.isSuccess()) {
                ServletUtils.forwardWithErrors(request, response, idRes.getErrors(), SUBSCRIPTION_FORM_JSP, TEMPLATE);
                return;
            }

            Result<UsersSubscriptionUpdateForm> updRes = SubscriptionBusiness.initUpdateForm(
                    request.getParameter("usersSubscriptionId"),
                    request.getParameter("startDate"),
                    request.getParameter("endDate"),
                    request.getParameter("quantity"),
                    request.getParameter("active")
            );
            if (!updRes.isSuccess()) {
                ServletUtils.forwardWithErrors(request, response, updRes.getErrors(), SUBSCRIPTION_FORM_JSP, TEMPLATE);
                return;
            }

            em = EMF.getEM();
            SubscriptionServiceImpl service = new SubscriptionServiceImpl(em);

            em.getTransaction().begin();
            Result<UsersSubscription> usRes = service.findUsersSubscription(idRes.getData());
            if (!usRes.isSuccess()) {
                ServletUtils.forwardWithError(request, response, "Abonnement introuvable", SUBSCRIPTION_FORM_JSP, TEMPLATE);
                em.getTransaction().rollback();
                return;
            }

            UsersSubscription us = usRes.getData();
            UsersSubscriptionUpdateForm f = updRes.getData();
            if (f.getStartDate() != null) us.setStartDate(f.getStartDate());
            if (f.getEndDate() != null)   us.setEndDate(f.getEndDate());
            if (f.getQuantity() != null)  us.setQuantityMax(f.getQuantity());
            us.setActive(f.isActive());

            em.merge(us);
            em.getTransaction().commit();

            ServletUtils.redirectWithMessage(request, response,
                    "Abonnement mis à jour", "success", "/subscription");

        } catch (Exception e) {
            log.error("Erreur mise à jour abonnement", e);
            ServletUtils.forwardWithError(request, response, e.getMessage(), SUBSCRIPTION_FORM_JSP, TEMPLATE);
        } finally {
            if (em != null) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                em.close();
            }
        }
    }
}
