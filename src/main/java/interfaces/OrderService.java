package interfaces;

import Tools.Result;
import entities.Order;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OrderService {

    /* ===== CRUD de base ===== */
    Result<Order> create(Order order);
    Result<Order> findOne(int id);
    Result<List<Order>> findAll();

    /* ===== Pagination (même pattern que SportService) ===== */
    Result<List<Order>> getAllOrders(int offset, int limit);
    Result<Long> countAllOrders();

    /* ===== Actions métier commande ===== */
    Result<Order> applyDiscount(int orderId, int discountId);          // crée OrdersDiscount + recalcule total
    Result<Order> applyAutoDiscounts(Integer orderId, boolean clubEligible);
    Result<Order> linkUserSubscription(int orderId, int subscriptionId);// crée OrdersSubscription (Order ↔ Subscription)
    Result<Map<String, Double>> quoteFromSport(int sportId, int quantity, LocalDate forDate);
    Result<Order> confirmBySubscription(int orderId);                   // vérifie UsersSubscription & débite
    Result<Order> confirmByOnlinePayment(int orderId);                  // marque payée + génère facture (via service facture)
    Result<Order> cancel(int orderId);                                  // annule; recrédite si paiement via abo
    Result<Order> createDraftFromSubscription(int usersSubscriptionId);

    /* ===== Cas spécifique TC10 : supprimer commande non payée ===== */
    Result<Order> cancelUnpaid(int orderId);                            // supprime Order + liens si status ONHOLD

    /* ===== (optionnel) Utilitaires courants ===== */
    Result<List<Order>> findByUser(int userId, int offset, int limit);
    Result<Long> countByUser(int userId);
}
