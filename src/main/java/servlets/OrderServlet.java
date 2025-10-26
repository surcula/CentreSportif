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
import entities.UsersSubscription;
import enums.OrderStatus;
import enums.Scope;
import interfaces.OrderService;
import services.OrderServiceImpl;
import services.SubscriptionServiceImpl;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static constants.Rooting.*;

@WebServlet(name = "OrderServlet", value = "/order")
public class OrderServlet extends HttpServlet {

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

        // ===== Formulaire de création standard (réservé staff) =====
        if (form != null && !"fromSubscription".equals(form)) {
            if (!fullAccess) { ServletUtils.redirectNoAuthorized(request, response); return; }
            OrderControllerHelper.handleFormDisplay(request, response);
            return;
        }

        // ===== Formulaire PRÉ-REMPLI depuis un abonnement (user OU staff) =====
        if ("fromSubscription".equals(form)) {
            if (session == null || session.getAttribute("userId") == null) {
                // même staff: on exige une session ouverte
                ServletUtils.redirectNoAuthorized(request, response);
                return;
            }

            // id de UsersSubscription en param: ?subscriptionId=XXXX
            Result<Integer> usIdRes = ParamUtils.verifyId(request.getParameter("subscriptionId"));
            if (!usIdRes.isSuccess()) {
                ServletUtils.forwardWithErrors(request, response, usIdRes.getErrors(), ORDER_JSP, TEMPLATE);
                return;
            }

            EntityManager em = EMF.getEM();
            try {
                int sessionUserId = (int) session.getAttribute("userId");
                SubscriptionServiceImpl subService = new SubscriptionServiceImpl(em);
                OrderServiceImpl orderService = new OrderServiceImpl(em);

                Result<UsersSubscription> usRes = subService.findUsersSubscription(usIdRes.getData());
                if (!usRes.isSuccess()) {
                    ServletUtils.forwardWithError(request, response, "Abonnement introuvable.", ORDER_JSP, TEMPLATE);
                    return;
                }
                UsersSubscription us = usRes.getData();

                // Ownership si non staff
                if (!fullAccess && (us.getUser() == null || us.getUser().getId() != sessionUserId)) {
                    ServletUtils.redirectNoAuthorized(request, response);
                    return;
                }

                // Devis pour pré-remplir
                int sportId = us.getSubscription().getSport().getId();
                Integer qBoxed = us.getQuantityMax(); // peut être null
                int qty = (qBoxed != null && qBoxed > 0) ? qBoxed : 1;

                Result<Map<String, Double>> quote = orderService.quoteFromSport(sportId, qty, LocalDate.now());
                if (!quote.isSuccess()) {
                    ServletUtils.forwardWithErrors(request, response, quote.getErrors(), ORDER_JSP, TEMPLATE);
                    return;
                }

                // ====== ATTRIBUTS ATTENDUS PAR order-form.jsp ======
                request.setAttribute("fromSubscription", true);

                // ✅ clé corrigée: le JSP attend "linkedUsersSubscription"
                request.setAttribute("linkedUsersSubscription", us);

                request.setAttribute("prefillUser", us.getUser());
                request.setAttribute("prefillSport", us.getSubscription().getSport());
                request.setAttribute("unitPrice", quote.getData().get("unit"));
                request.setAttribute("quantity", qty);
                request.setAttribute("total", quote.getData().get("total"));

                OrderControllerHelper.handleFormDisplay(request, response);
                return;

            } catch (Exception e) {
                log.error("Erreur GET /order?form=fromSubscription", e);
                ServletUtils.forwardWithError(request, response, e.getMessage(), ORDER_JSP, TEMPLATE);
                return;
            } finally {
                if (em != null) em.close();
            }
        }

        // ===== Édition : autoriser STAFF ou PROPRIÉTAIRE =====
        if (editForm != null) {
            Result<Integer> orderUpdateId = ParamUtils.verifyId(editForm);
            if (!orderUpdateId.isSuccess()) {
                ServletUtils.forwardWithError(request, response, "Id commande invalide.", ORDER_JSP, TEMPLATE);
                return;
            }

            EntityManager em = EMF.getEM();
            try {
                OrderService orderService = new OrderServiceImpl(em);
                Result<Order> result = orderService.findOne(orderUpdateId.getData());
                if (!result.isSuccess()) {
                    ServletUtils.forwardWithErrors(request, response, result.getErrors(), ORDER_FORM_JSP, TEMPLATE);
                    return;
                }

                Order order = result.getData();

                if (!fullAccess) {
                    Integer sessionUserId = (session != null) ? (Integer) session.getAttribute("userId") : null;
                    if (sessionUserId == null || order.getUser() == null || order.getUser().getId() != sessionUserId) {
                        ServletUtils.redirectNoAuthorized(request, response);
                        return;
                    }
                }

                OrderControllerHelper.handleEditForm(request, response, order);
                return;

            } catch (Exception e) {
                log.error("Erreur GET editForm", e);
                ServletUtils.forwardWithError(request, response, e.getMessage(), ORDER_JSP, TEMPLATE);
                return;
            } finally {
                if (em != null) em.close();
            }
        }

        // ===== Liste : réservé staff =====
        if (!fullAccess) {
            ServletUtils.redirectNoAuthorized(request, response);
            return;
        }

        EntityManager em = EMF.getEM();
        try {
            OrderService orderService = new OrderServiceImpl(em);
            OrderBusiness orderBusiness = new OrderBusiness(orderService);

            Result<Integer> pageRes = ParamUtils.stringToInteger(request.getParameter("page"));
            int page = pageRes.isSuccess() ? pageRes.getData() : 1;
            page = Math.max(1, page);

            Result<Integer> sizeRes = ParamUtils.stringToInteger(request.getParameter("size"));
            int size = sizeRes.isSuccess() ? sizeRes.getData() : 10;
            size = Math.max(1, Math.min(20, size));

            Result<Page<Order>> result = orderBusiness.getAllOrdersPaged(page, size, Scope.ALL);
            if (result.isSuccess()) {
                Page<Order> p = result.getData();
                request.setAttribute("orders", p.getContent());
                request.setAttribute("page", p.getPage());
                request.setAttribute("size", p.getSize());
                request.setAttribute("totalPages", p.getTotalPages());
                request.setAttribute("totalElements", p.getTotalElements());
                request.setAttribute("fullAccess", true);

                OrderControllerHelper.handleList(request, response, (List<Order>) p.getContent());
            } else {
                ServletUtils.forwardWithErrors(request, response, result.getErrors(), ORDER_JSP, TEMPLATE);
            }
            return;

        } catch (Exception e) {
            log.error("Erreur liste commandes", e);
            ServletUtils.forwardWithError(request, response, e.getMessage(), ORDER_JSP, TEMPLATE);
            return;
        } finally {
            if (em != null) em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String ctx = request.getContextPath();
        HttpSession session = request.getSession(false);
        String role = (session != null && session.getAttribute("role") != null)
                ? session.getAttribute("role").toString()
                : null;

        String action = request.getParameter("action");
        String orderIdRaw = request.getParameter("orderId");
        log.info("OrderServlet.doPost action=" + action + " orderId=" + orderIdRaw + " ctx=" + ctx);

        /* ========= One-click pay depuis l’abonnement (user + staff) ========= */
        if ("quickPayFromSubscription".equals(action)) {
            Result<Integer> subIdRes = ParamUtils.verifyId(request.getParameter("subscriptionId")); // UsersSubscription.id attendu
            if (!subIdRes.isSuccess()) {
                ServletUtils.forwardWithErrors(request, response, subIdRes.getErrors(), ORDER_JSP, TEMPLATE);
                return;
            }

            EntityManager em = EMF.getEM();
            try {
                OrderServiceImpl orderService = new OrderServiceImpl(em);
                SubscriptionServiceImpl subService = new SubscriptionServiceImpl(em);

                Result<UsersSubscription> usRes = subService.findUsersSubscription(subIdRes.getData());
                if (!usRes.isSuccess()) {
                    ServletUtils.forwardWithErrors(request, response, usRes.getErrors(), ORDER_JSP, TEMPLATE);
                    return;
                }
                UsersSubscription us = usRes.getData();

                boolean fullAccess = ServletUtils.isFullAuthorized(role);
                if (!fullAccess) {
                    Integer sessionUserId = (Integer) (session != null ? session.getAttribute("userId") : null);
                    if (sessionUserId == null || us.getUser() == null || us.getUser().getId() != sessionUserId) {
                        ServletUtils.redirectNoAuthorized(request, response);
                        return;
                    }
                }

                int sportId = us.getSubscription().getSport().getId();
                Integer qBoxed = us.getQuantityMax();
                int qty = (qBoxed != null && qBoxed > 0) ? qBoxed : 1;

                Result<Map<String, Double>> quote = orderService.quoteFromSport(sportId, qty, LocalDate.now());
                if (!quote.isSuccess()) {
                    ServletUtils.forwardWithErrors(request, response, quote.getErrors(), ORDER_JSP, TEMPLATE);
                    return;
                }

                em.getTransaction().begin();

                Order o = new Order();
                o.setUser(us.getUser());
                o.setDateOrder(Instant.now());
                o.setTotalPrice(quote.getData().get("total"));
                o.setStatus(OrderStatus.ONHOLD);
                em.persist(o);

                // ✅ CORRECTION: lier avec l'ID du UsersSubscription, pas Subscription
                Result<Order> linkRes = orderService.linkUserSubscription(o.getId(), us.getId());
                if (!linkRes.isSuccess()) {
                    em.getTransaction().rollback();
                    ServletUtils.forwardWithErrors(request, response, linkRes.getErrors(), ORDER_JSP, TEMPLATE);
                    return;
                }

                Result<Order> payRes = orderService.confirmBySubscription(o.getId());
                if (!payRes.isSuccess()) {
                    em.getTransaction().rollback();
                    ServletUtils.forwardWithErrors(request, response, payRes.getErrors(), ORDER_JSP, TEMPLATE);
                    return;
                }

                em.getTransaction().commit();
                response.sendRedirect(request.getContextPath() + "/order?editForm=" + o.getId());
                return;

            } catch (Exception e) {
                if (em != null && em.getTransaction().isActive()) em.getTransaction().rollback();
                ServletUtils.forwardWithError(request, response, e.getMessage(), ORDER_JSP, TEMPLATE);
                return;
            } finally {
                if (em != null) em.close();
            }
        }

        /* ========= Le reste est réservé au staff ========= */
        if (!ServletUtils.isFullAuthorized(role)) {
            ServletUtils.redirectNoAuthorized(request, response);
            return;
        }

        // Appliquer une réduction
        if ("applyDiscount".equals(action)) {
            Result<OrderDiscountForm> form = OrderBusiness.initApplyDiscountForm(
                    request.getParameter("orderId"),
                    request.getParameter("discountId")
            );
            if (!form.isSuccess()) {
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
                response.sendRedirect(ctx + "/order?editForm=" + form.getData().getOrderId());
                return;

            } catch (Exception e) {
                if (em != null && em.getTransaction().isActive()) em.getTransaction().rollback();
                ServletUtils.forwardWithError(request, response, e.getMessage(), ORDER_JSP, TEMPLATE);
                return;
            } finally {
                if (em != null) em.close();
            }
        }

        // Payer via abonnement (depuis editForm)
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
                response.sendRedirect(ctx + "/order?editForm=" + form.getData().getOrderId());
                return;
            } catch (Exception e) {
                if (em != null && em.getTransaction().isActive()) em.getTransaction().rollback();
                ServletUtils.forwardWithError(request, response, e.getMessage(), ORDER_JSP, TEMPLATE);
                return;
            } finally {
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
                response.sendRedirect(ctx + "/order?editForm=" + form.getData().getOrderId());
                return;
            } catch (Exception e) {
                if (em != null && em.getTransaction().isActive()) em.getTransaction().rollback();
                ServletUtils.forwardWithError(request, response, e.getMessage(), ORDER_JSP, TEMPLATE);
                return;
            } finally {
                if (em != null) em.close();
            }
        }

        // Annuler
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
                response.sendRedirect(ctx + "/order?editForm=" + form.getData().getOrderId());
                return;
            } catch (Exception e) {
                if (em != null && em.getTransaction().isActive()) em.getTransaction().rollback();
                ServletUtils.forwardWithError(request, response, e.getMessage(), ORDER_JSP, TEMPLATE);
                return;
            } finally {
                if (em != null) em.close();
            }
        }

        // Supprimer non payée
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
                response.sendRedirect(ctx + "/order");
                return;
            } catch (Exception e) {
                if (em != null && em.getTransaction().isActive()) em.getTransaction().rollback();
                ServletUtils.forwardWithError(request, response, e.getMessage(), ORDER_JSP, TEMPLATE);
                return;
            } finally {
                if (em != null) em.close();
            }
        }

        ServletUtils.forwardWithError(request, response, "Action inconnue.", ORDER_JSP, TEMPLATE);
    }
}
