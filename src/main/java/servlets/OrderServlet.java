package servlets;

import Tools.ParamUtils;
import Tools.Result;
import business.OrderBusiness;
import business.ServletUtils;
import controllers.helpers.OrderControllerHelper;
import dto.EMF;
import dto.Page;
import dto.OrderActionForm;
import dto.OrderDiscountForm;
import entities.Order;
import enums.Scope;
import interfaces.OrderService;
import services.OrderServiceImpl;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

import static constants.Rooting.*;

/**
 * Servlet pour la gestion des commandes (Orders)
 * Style identique à SportServlet : mêmes utilitaires, même orchestration, même gestion EMF.
 */
@WebServlet(name = "OrderServlet", value = "/order")
public class OrderServlet extends HttpServlet {

    // Log4j
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(OrderServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String form = request.getParameter("form");
        String editForm = request.getParameter("editForm");
        String role = (session != null && session.getAttribute("role") != null)
                ? session.getAttribute("role").toString()
                : null;
        boolean fullAccess = ServletUtils.isFullAuthorized(role);

        // --- Formulaire de création ---
        if (form != null) {
            if (!fullAccess) {
                ServletUtils.redirectNoAuthorized(request, response);
                return;
            }
            log.info("Form: " + form);
            OrderControllerHelper.handleFormDisplay(request, response);
            return;

            // --- Formulaire d'édition ---
        } else if (editForm != null) {
            log.info("editForm: " + editForm);
            if (!fullAccess) {
                ServletUtils.redirectNoAuthorized(request, response);
                return;
            }

            Result<Integer> orderUpdateId = ParamUtils.verifyId(editForm);
            if (!orderUpdateId.isSuccess()) {
                log.error("Erreur lors de la conversion de l'id de la commande");
                ServletUtils.forwardWithError(request, response, "Erreur lors de la conversion de l'id de la commande", ORDER_JSP, TEMPLATE);
                return;
            }

            EntityManager em = EMF.getEM();
            try {
                OrderService orderService = new OrderServiceImpl(em);
                Result<Order> result = orderService.findOne(orderUpdateId.getData());
                if (result.isSuccess()) {
                    OrderControllerHelper.handleEditForm(request, response, result.getData());
                } else {
                    ServletUtils.forwardWithErrors(request, response, result.getErrors(), ORDER_FORM_JSP, TEMPLATE);
                }
                return;
            } catch (Exception e) {
                log.error("Erreur lors du chargement de la commande en édition", e);
                ServletUtils.forwardWithError(request, response, e.getMessage(), ORDER_JSP, TEMPLATE);
                return;
            } finally {
                if (em != null) em.close();
            }

            // --- Liste + pagination ---
        } else {
            EntityManager em = EMF.getEM();
            try {
                OrderService orderService = new OrderServiceImpl(em);
                OrderBusiness orderBusiness = new OrderBusiness(orderService);

                Result<Integer> pageRes = ParamUtils.stringToInteger(request.getParameter("page"));
                int page = pageRes.isSuccess() ? Math.max(1, pageRes.getData()) : 1;

                Result<Integer> sizeRes = ParamUtils.stringToInteger(request.getParameter("size"));
                int size = sizeRes.isSuccess() ? Math.min(20, Math.max(1, sizeRes.getData())) : 10;

                // Si tu veux filtrer “actives” vs “toutes”, on peut réutiliser Scope comme pour Sport.
                // Ici, on liste toutes par défaut.
                Result<Page<Order>> result = orderBusiness.getAllOrdersPaged(page, size, Scope.ALL);

                if (result.isSuccess()) {
                    Page<Order> p = result.getData();
                    request.setAttribute("orders", p.getContent());
                    request.setAttribute("page", p.getPage());
                    request.setAttribute("size", p.getSize());
                    request.setAttribute("totalPages", p.getTotalPages());
                    request.setAttribute("totalElements", p.getTotalElements());
                    request.setAttribute("fullAccess", fullAccess);

                    OrderControllerHelper.handleList(request, response, (List<Order>) p.getContent());
                } else {
                    ServletUtils.forwardWithErrors(request, response, result.getErrors(), ORDER_JSP, TEMPLATE);
                }
                return;

            } catch (Exception e) {
                log.error("Erreur doGet order", e);
                ServletUtils.forwardWithError(request, response, e.getMessage(), ORDER_JSP, TEMPLATE);
                return;
            } finally {
                if (em != null) em.close();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Vérifier les droits (comme SportServlet)
        HttpSession session = request.getSession(false);
        if (!(session != null && ServletUtils.isFullAuthorized(session.getAttribute("role").toString()))) {
            ServletUtils.redirectNoAuthorized(request, response);
            return;
        }

        String action = request.getParameter("action");

        /* ============ Actions spécifiques aux commandes ============ */

        // Appliquer une réduction
        if ("applyDiscount".equals(action)) {
            Result<OrderDiscountForm> form = OrderBusiness.initApplyDiscountForm(
                    request.getParameter("orderId"),
                    request.getParameter("discountId")
            );
            if (!form.isSuccess()) {
                log.error("Erreur applyDiscount : " + form.getErrors());
                ServletUtils.forwardWithErrors(request, response, form.getErrors(), ORDER_JSP, TEMPLATE);
                return;
            }

            EntityManager em = EMF.getEM();
            try {
                OrderService orderService = new OrderServiceImpl(em);
                em.getTransaction().begin();
                Result<Order> res = orderService.applyDiscount(form.getData().getOrderId(), form.getData().getDiscountId());
                if (!res.isSuccess()) {
                    em.getTransaction().rollback();
                    ServletUtils.forwardWithErrors(request, response, res.getErrors(), ORDER_JSP, TEMPLATE);
                    return;
                }
                em.getTransaction().commit();
                log.info("Réduction appliquée avec succès.");
                ServletUtils.redirectWithMessage(request, response, "Réduction appliquée avec succès.", "success",
                        "/order?editForm=" + form.getData().getOrderId());
                return;

            } catch (Exception e) {
                log.error("Erreur applyDiscount : " + e.getMessage(), e);
                ServletUtils.forwardWithError(request, response, e.getMessage(), ORDER_JSP, TEMPLATE);
                return;
            } finally {
                if (em != null && em.getTransaction().isActive()) em.getTransaction().rollback();
                if (em != null) em.close();
            }
        }

        // Payer via abonnement
        if ("paySubscription".equals(action)) {
            Result<OrderActionForm> form = OrderBusiness.initOrderActionForm(request.getParameter("orderId"));
            if (!form.isSuccess()) {
                ServletUtils.forwardWithErrors(request, response, form.getErrors(), ORDER_JSP, TEMPLATE);
                return;
            }
            EntityManager em = EMF.getEM();
            try {
                OrderService orderService = new OrderServiceImpl(em);
                em.getTransaction().begin();
                Result<Order> res = orderService.confirmBySubscription(form.getData().getOrderId());
                if (!res.isSuccess()) {
                    em.getTransaction().rollback();
                    ServletUtils.forwardWithErrors(request, response, res.getErrors(), ORDER_JSP, TEMPLATE);
                    return;
                }
                em.getTransaction().commit();
                log.info("Paiement via abonnement confirmé.");
                ServletUtils.redirectWithMessage(request, response, "Paiement via abonnement confirmé.", "success",
                        "/order?editForm=" + form.getData().getOrderId());
                return;
            } catch (Exception e) {
                log.error("Erreur paySubscription : " + e.getMessage(), e);
                ServletUtils.forwardWithError(request, response, e.getMessage(), ORDER_JSP, TEMPLATE);
                return;
            } finally {
                if (em != null && em.getTransaction().isActive()) em.getTransaction().rollback();
                if (em != null) em.close();
            }
        }

        // Payer en ligne (mock)
        if ("payOnline".equals(action)) {
            Result<OrderActionForm> form = OrderBusiness.initOrderActionForm(request.getParameter("orderId"));
            if (!form.isSuccess()) {
                ServletUtils.forwardWithErrors(request, response, form.getErrors(), ORDER_JSP, TEMPLATE);
                return;
            }
            EntityManager em = EMF.getEM();
            try {
                OrderService orderService = new OrderServiceImpl(em);
                em.getTransaction().begin();
                Result<Order> res = orderService.confirmByOnlinePayment(form.getData().getOrderId());
                if (!res.isSuccess()) {
                    em.getTransaction().rollback();
                    ServletUtils.forwardWithErrors(request, response, res.getErrors(), ORDER_JSP, TEMPLATE);
                    return;
                }
                em.getTransaction().commit();
                log.info("Paiement en ligne confirmé.");
                ServletUtils.redirectWithMessage(request, response, "Paiement en ligne confirmé.", "success",
                        "/order?editForm=" + form.getData().getOrderId());
                return;
            } catch (Exception e) {
                log.error("Erreur payOnline : " + e.getMessage(), e);
                ServletUtils.forwardWithError(request, response, e.getMessage(), ORDER_JSP, TEMPLATE);
                return;
            } finally {
                if (em != null && em.getTransaction().isActive()) em.getTransaction().rollback();
                if (em != null) em.close();
            }
        }

        // Annuler (payée ou non)
        if ("cancel".equals(action)) {
            Result<OrderActionForm> form = OrderBusiness.initOrderActionForm(request.getParameter("orderId"));
            if (!form.isSuccess()) {
                ServletUtils.forwardWithErrors(request, response, form.getErrors(), ORDER_JSP, TEMPLATE);
                return;
            }
            EntityManager em = EMF.getEM();
            try {
                OrderService orderService = new OrderServiceImpl(em);
                em.getTransaction().begin();
                Result<Order> res = orderService.cancel(form.getData().getOrderId());
                if (!res.isSuccess()) {
                    em.getTransaction().rollback();
                    ServletUtils.forwardWithErrors(request, response, res.getErrors(), ORDER_JSP, TEMPLATE);
                    return;
                }
                em.getTransaction().commit();
                log.info("Commande annulée avec succès.");
                ServletUtils.redirectWithMessage(request, response, "Commande annulée avec succès.", "success",
                        "/order?editForm=" + form.getData().getOrderId());
                return;
            } catch (Exception e) {
                log.error("Erreur cancel : " + e.getMessage(), e);
                ServletUtils.forwardWithError(request, response, e.getMessage(), ORDER_JSP, TEMPLATE);
                return;
            } finally {
                if (em != null && em.getTransaction().isActive()) em.getTransaction().rollback();
                if (em != null) em.close();
            }
        }

        // Supprimer une commande non payée (TC10)
        if ("cancelUnpaid".equals(action)) {
            Result<OrderActionForm> form = OrderBusiness.initOrderActionForm(request.getParameter("orderId"));
            if (!form.isSuccess()) {
                ServletUtils.forwardWithErrors(request, response, form.getErrors(), ORDER_JSP, TEMPLATE);
                return;
            }
            EntityManager em = EMF.getEM();
            try {
                OrderService orderService = new OrderServiceImpl(em);
                em.getTransaction().begin();
                Result<Order> res = orderService.cancelUnpaid(form.getData().getOrderId());
                if (!res.isSuccess()) {
                    em.getTransaction().rollback();
                    ServletUtils.forwardWithErrors(request, response, res.getErrors(), ORDER_JSP, TEMPLATE);
                    return;
                }
                em.getTransaction().commit();
                log.info("Commande non payée supprimée.");
                ServletUtils.redirectWithMessage(request, response, "Commande non payée supprimée.", "success", "/order");
                return;
            } catch (Exception e) {
                log.error("Erreur cancelUnpaid : " + e.getMessage(), e);
                ServletUtils.forwardWithError(request, response, e.getMessage(), ORDER_JSP, TEMPLATE);
                return;
            } finally {
                if (em != null && em.getTransaction().isActive()) em.getTransaction().rollback();
                if (em != null) em.close();
            }
        }

        // Action inconnue
        ServletUtils.forwardWithError(request, response, "Action inconnue.", ORDER_JSP, TEMPLATE);
    }
}
