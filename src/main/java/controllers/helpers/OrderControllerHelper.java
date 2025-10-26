// src/main/java/controllers/helpers/OrderControllerHelper.java
package controllers.helpers;

import business.ServletUtils;
import entities.Order;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static constants.Rooting.ORDER_FORM_JSP;
import static constants.Rooting.ORDER_JSP;
import static constants.Rooting.TEMPLATE;

public class OrderControllerHelper {

    /**
     * Load Order FORM CREATE
     */
    public static void handleFormDisplay(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletUtils.forwardWithContent(request, response, ORDER_FORM_JSP, TEMPLATE);
    }

    /**
     * Load Order Form EDIT
     */
    public static void handleEditForm(HttpServletRequest request, HttpServletResponse response, Order order)
            throws ServletException, IOException {
        request.setAttribute("order", order);
        ServletUtils.forwardWithContent(request, response, ORDER_FORM_JSP, TEMPLATE);
    }

    /**
     * Load Order Page (list)
     */
    public static void handleList(HttpServletRequest request, HttpServletResponse response, List<Order> orders)
            throws ServletException, IOException {
        request.setAttribute("orders", orders);
        ServletUtils.forwardWithContent(request, response, ORDER_JSP, TEMPLATE);
    }
}
