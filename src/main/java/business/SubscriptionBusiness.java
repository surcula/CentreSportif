package business;

import Tools.Result;
import dto.UsersSubscriptionAssignForm;
import dto.UsersSubscriptionUpdateForm;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class SubscriptionBusiness {

    private static final org.apache.log4j.Logger log =
            org.apache.log4j.Logger.getLogger(SubscriptionBusiness.class);

    /* ---------- CREATE (assignation) ---------- */
    public static Result<UsersSubscriptionAssignForm> initAssignForm(
            String strUserId, String strSubscriptionId,
            String strStart, String strEnd, String strQuantity, String strActive) {

        Map<String,String> errors = new HashMap<String,String>();

        // Required
        Result<String> userIdStr = ValidateForm.stringIsEmpty(strUserId, "errorUserId", "User ID", errors);
        if (!userIdStr.isSuccess()) errors.putAll(userIdStr.getErrors());

        Result<String> subIdStr = ValidateForm.stringIsEmpty(strSubscriptionId, "errorSubscriptionId", "Subscription ID", errors);
        if (!subIdStr.isSuccess()) errors.putAll(subIdStr.getErrors());

        Result<String> qtyStr = ValidateForm.stringIsEmpty(strQuantity, "errorQuantity", "Quantity", errors);
        if (!qtyStr.isSuccess()) errors.putAll(qtyStr.getErrors());

        // Parse
        Result<Integer> userId = ValidateForm.stringToInteger(userIdStr.getData(), "errorUserId", "User ID", errors);
        if (!userId.isSuccess()) errors.putAll(userId.getErrors());
        else if (userId.getData().intValue() <= 0) errors.put("errorUserId","User ID doit être un entier positif.");

        Result<Integer> subscriptionId = ValidateForm.stringToInteger(subIdStr.getData(), "errorSubscriptionId", "Subscription ID", errors);
        if (!subscriptionId.isSuccess()) errors.putAll(subscriptionId.getErrors());
        else if (subscriptionId.getData().intValue() <= 0) errors.put("errorSubscriptionId","Subscription ID doit être un entier positif.");

        Result<Integer> quantity = ValidateForm.stringToInteger(qtyStr.getData(), "errorQuantity", "Quantity", errors);
        if (!quantity.isSuccess()) errors.putAll(quantity.getErrors());
        else if (quantity.getData().intValue() <= 0) errors.put("errorQuantity","Quantity doit être > 0.");

        Result<Boolean> active = ValidateForm.parseBoolean(strActive, "errorActive","Active", errors);
        if (!active.isSuccess()) errors.putAll(active.getErrors());

        // Dates
        LocalDate start = null;
        LocalDate end = null;
        try {
            start = LocalDate.parse(strStart);
        } catch (Exception e) { errors.put("errorStartDate","Date de début invalide (format: yyyy-MM-dd)."); }
        try {
            end = LocalDate.parse(strEnd);
        } catch (Exception e) { errors.put("errorEndDate","Date de fin invalide (format: yyyy-MM-dd)."); }

        if (start != null && end != null && !end.isAfter(start)) {
            errors.put("errorDates", "La date de fin doit être postérieure à la date de début.");
        }

        if (!errors.isEmpty()) return Result.fail(errors);

        UsersSubscriptionAssignForm form = new UsersSubscriptionAssignForm();
        form.setUserId(userId.getData());
        form.setSubscriptionId(subscriptionId.getData());
        form.setStartDate(start);
        form.setEndDate(end);
        form.setQuantity(quantity.getData());
        form.setActive(active.getData());
        return Result.ok(form);
    }

    /* ---------- UPDATE ---------- */
    public static Result<UsersSubscriptionUpdateForm> initUpdateForm(
            String strStart, String strEnd, String strQuantity, String strActive) {

        Map<String,String> errors = new HashMap<String,String>();

        Result<Boolean> active = ValidateForm.parseBoolean(strActive, "errorActive","Active", errors);
        if (!active.isSuccess()) errors.putAll(active.getErrors());

        Integer qty = null;
        if (strQuantity != null && !strQuantity.trim().isEmpty()) {
            Result<Integer> q = ValidateForm.stringToInteger(strQuantity, "errorQuantity", "Quantity", errors);
            if (!q.isSuccess()) errors.putAll(q.getErrors());
            else if (q.getData().intValue() < 0) errors.put("errorQuantity","Quantity doit être >= 0.");
            else qty = q.getData();
        }

        LocalDate start = null;
        LocalDate end = null;
        try { if (strStart != null && !strStart.isEmpty()) start = LocalDate.parse(strStart); } catch (Exception e) {
            errors.put("errorStartDate","Date de début invalide (yyyy-MM-dd).");
        }
        try { if (strEnd != null && !strEnd.isEmpty()) end = LocalDate.parse(strEnd); } catch (Exception e) {
            errors.put("errorEndDate","Date de fin invalide (yyyy-MM-dd).");
        }

        if (start != null && end != null && !end.isAfter(start)) {
            errors.put("errorDates", "La date de fin doit être postérieure à la date de début.");
        }

        if (!errors.isEmpty()) return Result.fail(errors);

        UsersSubscriptionUpdateForm form = new UsersSubscriptionUpdateForm();
        form.setStartDate(start);
        form.setEndDate(end);
        form.setQuantity(qty);
        form.setActive(active.getData());
        return Result.ok(form);
    }
}
