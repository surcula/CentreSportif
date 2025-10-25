package controllers.helpers;

import business.ServletUtils;
import entities.UsersSubscription;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static constants.Rooting.*;

public class SubscriptionControllerHelper {

    public static void handleList(HttpServletRequest req, HttpServletResponse resp,
                                  List<UsersSubscription> list) throws ServletException, IOException {
        req.setAttribute("subscriptions", list);
        ServletUtils.forwardWithContent(req, resp, SUBSCRIPTION_JSP, TEMPLATE);
    }

    public static void handleForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ServletUtils.forwardWithContent(req, resp, SUBSCRIPTION_FORM_JSP, TEMPLATE);
    }

    public static void handleEditForm(HttpServletRequest req, HttpServletResponse resp, UsersSubscription us)
            throws ServletException, IOException {
        req.setAttribute("usersSubscription", us);
        ServletUtils.forwardWithContent(req, resp, SUBSCRIPTION_FORM_JSP, TEMPLATE);
    }
}
