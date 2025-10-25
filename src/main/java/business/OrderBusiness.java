package business;

import Tools.Result;
import dto.OrderActionForm;
import dto.OrderDiscountForm;
import dto.Page;
import entities.Discount;
import entities.Order;
import entities.OrdersDiscount;
import enums.Scope;
import interfaces.OrderService;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Business Order – même approche que SportBusiness :
 * - validations de formulaire (initXXXForm)
 * - pagination (getAllOrdersPaged)
 * - petits helpers de calcul
 */
public class OrderBusiness {

    private static final Logger log = Logger.getLogger(OrderBusiness.class);

    private final OrderService orderService;

    /** IMPORTANT : constructeur requis par la servlet */
    public OrderBusiness(OrderService orderService) {
        this.orderService = orderService;
    }

    /* ======= Pagination ALL Orders (comme SportBusiness) ======= */
    public Result<Page<Order>> getAllOrdersPaged(int page, int size, Scope scope) {
        int pageNumber = Math.max(1, page);
        int pageSize   = Math.max(1, size);
        int offset     = (pageNumber - 1) * pageSize;

        // Ici pas de notion ACTIVE/ALL pour Order → on liste toutes
        Result<List<Order>> content = orderService.getAllOrders(offset, pageSize);
        if (!content.isSuccess()) {
            log.error(content.getErrors());
            return Result.fail(content.getErrors());
        }

        Result<Long> total = orderService.countAllOrders();
        if (!total.isSuccess()) {
            log.error(total.getErrors());
            return Result.fail(total.getErrors());
        }

        Page<Order> pageObj = Page.of(content.getData(), pageNumber, pageSize, total.getData());
        log.info("Pagination réussie[orders] → " + pageObj);
        return Result.ok(pageObj);
    }

    /* ================== VALIDATIONS FORMULAIRES ================== */

    // Appliquer une remise (orderId, discountId)
    public static Result<OrderDiscountForm> initApplyDiscountForm(String strOrderId, String strDiscountId) {
        Map<String, String> errors = new HashMap<String, String>();

        Result<String> orderIdStr    = ValidateForm.stringIsEmpty(strOrderId, "errorOrderId", "Order ID", errors);
        Result<String> discountIdStr = ValidateForm.stringIsEmpty(strDiscountId, "errorDiscountId", "Discount ID", errors);
        if (!orderIdStr.isSuccess())    errors.putAll(orderIdStr.getErrors());
        if (!discountIdStr.isSuccess()) errors.putAll(discountIdStr.getErrors());

        Result<Integer> orderId    = orderIdStr.isSuccess()
                ? ValidateForm.parseInteger(orderIdStr.getData(), "errorOrderId", "Order ID", errors)
                : Result.fail(errors);

        Result<Integer> discountId = discountIdStr.isSuccess()
                ? ValidateForm.parseInteger(discountIdStr.getData(), "errorDiscountId", "Discount ID", errors)
                : Result.fail(errors);

        if (orderId.isSuccess() && orderId.getData().intValue() <= 0)
            errors.put("errorOrderId", "Order ID doit être un entier positif.");

        if (discountId.isSuccess() && discountId.getData().intValue() <= 0)
            errors.put("errorDiscountId", "Discount ID doit être un entier positif.");

        if (!errors.isEmpty()) return Result.fail(errors);

        OrderDiscountForm f = new OrderDiscountForm();
        f.setOrderId(orderId.getData().intValue());
        f.setDiscountId(discountId.getData().intValue());
        return Result.ok(f);
    }

    // Actions simples (payOnline, paySubscription, cancel, cancelUnpaid) → besoin d’un orderId
    public static Result<OrderActionForm> initOrderActionForm(String strOrderId) {
        Map<String, String> errors = new HashMap<String, String>();

        Result<String> orderIdStr = ValidateForm.stringIsEmpty(strOrderId, "errorOrderId", "Order ID", errors);
        if (!orderIdStr.isSuccess()) errors.putAll(orderIdStr.getErrors());

        Result<Integer> orderId = orderIdStr.isSuccess()
                ? ValidateForm.parseInteger(orderIdStr.getData(), "errorOrderId", "Order ID", errors)
                : Result.fail(errors);

        if (orderId.isSuccess() && orderId.getData().intValue() <= 0)
            errors.put("errorOrderId", "Order ID doit être un entier positif.");

        if (!errors.isEmpty()) return Result.fail(errors);

        OrderActionForm f = new OrderActionForm();
        f.setOrderId(orderId.getData().intValue());
        return Result.ok(f);
    }

    /* ======= Calcul du total avec remises (cumul multiplicatif simple) ======= */
    public static Result<Double> computeDiscountedTotal(double base, List<OrdersDiscount> ods) {
        double result = base;
        if (ods != null) {
            for (OrdersDiscount od : ods) {
                Discount d = od.getDiscount();
                if (d != null && d.isActive()) {
                    result = result * (1.0 - (d.getPercent() / 100.0));
                }
            }
        }
        if (result < 0.0) result = 0.0;
        return Result.ok(result);
    }
}
