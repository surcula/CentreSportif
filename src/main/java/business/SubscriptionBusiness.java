package business;

import Tools.Result;
import dto.UsersSubscriptionAssignForm;
import dto.UsersSubscriptionUpdateForm;
import org.apache.log4j.Logger;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class SubscriptionBusiness {

    private static final Logger log = Logger.getLogger(SubscriptionBusiness.class);

    /* ---------- Helpers ---------- */

    /** Normalise les valeurs de checkbox/booleans HTML vers "1"/"0" pour ValidateForm.parseBoolean */
    private static String normalizeActive(String raw) {
        if (raw == null) return "0";
        String v = raw.trim().toLowerCase();
        if ("1".equals(v) || "true".equals(v) || "on".equals(v) || "yes".equals(v)) return "1";
        if ("0".equals(v) || "false".equals(v) || "off".equals(v) || "no".equals(v)) return "0";
        return raw; // laisser parseBoolean gérer le message d'erreur le cas échéant
    }

    /** Parse LocalDate en gérant les messages d'erreurs */
    private static LocalDate parseDate(String raw, String key, Map<String,String> errors) {
        try {
            if (raw == null || raw.trim().isEmpty()) {
                errors.put(key, "Date manquante.");
                return null;
            }
            return LocalDate.parse(raw.trim());
        } catch (Exception e) {
            errors.put(key, "Date invalide (format attendu : yyyy-MM-dd).");
            return null;
        }
    }

    /* ---------- CREATE (assignation) ---------- */
    public static Result<UsersSubscriptionAssignForm> initAssignForm(
            String strUserId, String strSubscriptionId,
            String strStart, String strEnd, String strQuantity, String strActive) {

        Map<String,String> errors = new HashMap<>();

        // Required basiques
        Result<String> userIdStr = ValidateForm.stringIsEmpty(strUserId, "errorUserId", "User ID", errors);
        Result<String> subIdStr  = ValidateForm.stringIsEmpty(strSubscriptionId, "errorSubscriptionId", "Subscription ID", errors);
        Result<String> qtyStr    = ValidateForm.stringIsEmpty(strQuantity, "errorQuantity", "Quantity", errors);

        if (!userIdStr.isSuccess()) errors.putAll(userIdStr.getErrors());
        if (!subIdStr.isSuccess())  errors.putAll(subIdStr.getErrors());
        if (!qtyStr.isSuccess())    errors.putAll(qtyStr.getErrors());

        // Normalisation active
        String normalizedActive = normalizeActive(strActive);

        // Parse numériques
        Result<Integer> userId = ValidateForm.stringToInteger(
                userIdStr.isSuccess() ? userIdStr.getData() : "", "errorUserId", "User ID", errors);
        Result<Integer> subscriptionId = ValidateForm.stringToInteger(
                subIdStr.isSuccess() ? subIdStr.getData() : "", "errorSubscriptionId", "Subscription ID", errors);
        Result<Integer> quantity = ValidateForm.stringToInteger(
                qtyStr.isSuccess() ? qtyStr.getData() : "", "errorQuantity", "Quantity", errors);

        if (userId.isSuccess() && userId.getData() <= 0) errors.put("errorUserId","User ID doit être un entier positif.");
        if (subscriptionId.isSuccess() && subscriptionId.getData() <= 0) errors.put("errorSubscriptionId","Subscription ID doit être un entier positif.");
        if (quantity.isSuccess() && quantity.getData() < 0) errors.put("errorQuantity","Quantity doit être ≥ 0.");

        // Parse boolean
        Result<Boolean> active = ValidateForm.parseBoolean(normalizedActive, "errorActive","Active", errors);
        if (!active.isSuccess()) errors.putAll(active.getErrors());

        // Dates
        LocalDate start = parseDate(strStart, "errorStartDate", errors);
        LocalDate end   = parseDate(strEnd,   "errorEndDate",   errors);

        // Autorise même jour : end >= start
        if (start != null && end != null && end.isBefore(start)) {
            errors.put("errorDates", "La date de fin doit être postérieure ou égale à la date de début.");
        }

        if (!errors.isEmpty()) return Result.fail(errors);

        UsersSubscriptionAssignForm form = new UsersSubscriptionAssignForm();
        form.setUserId(userId.getData());
        form.setSubscriptionId(subscriptionId.getData());
        form.setStartDate(start);
        form.setEndDate(end);
        form.setQuantity(quantity.getData());
        form.setActive(active.getData());

        log.info("initAssignForm OK → userId=" + form.getUserId() + ", subId=" + form.getSubscriptionId());
        return Result.ok(form);
    }

    /* ---------- UPDATE ---------- */
    public static Result<UsersSubscriptionUpdateForm> initUpdateForm(
            String strUsersSubscriptionId, String strStart, String strEnd, String strQuantity, String strActive) {

        Map<String,String> errors = new HashMap<>();

        // ID obligatoire pour l'update
        Result<String> usIdStr = ValidateForm.stringIsEmpty(strUsersSubscriptionId, "errorUsersSubscriptionId", "UsersSubscription ID", errors);
        if (!usIdStr.isSuccess()) errors.putAll(usIdStr.getErrors());
        Result<Integer> usId = ValidateForm.stringToInteger(
                usIdStr.isSuccess() ? usIdStr.getData() : "", "errorUsersSubscriptionId", "UsersSubscription ID", errors);
        if (usId.isSuccess() && usId.getData() <= 0) errors.put("errorUsersSubscriptionId","UsersSubscription ID doit être un entier positif.");

        // Normalisation active
        String normalizedActive = normalizeActive(strActive);
        Result<Boolean> active = ValidateForm.parseBoolean(normalizedActive, "errorActive","Active", errors);
        if (!active.isSuccess()) errors.putAll(active.getErrors());

        // Quantité optionnelle (si vide → null)
        Integer qty = null;
        if (strQuantity != null && !strQuantity.trim().isEmpty()) {
            Result<Integer> q = ValidateForm.stringToInteger(strQuantity, "errorQuantity", "Quantity", errors);
            if (!q.isSuccess()) errors.putAll(q.getErrors());
            else if (q.getData() < 0) errors.put("errorQuantity","Quantity doit être ≥ 0.");
            else qty = q.getData();
        }

        // Dates optionnelles
        LocalDate start = null;
        LocalDate end   = null;
        if (strStart != null && !strStart.trim().isEmpty()) start = parseDate(strStart, "errorStartDate", errors);
        if (strEnd   != null && !strEnd.trim().isEmpty())   end   = parseDate(strEnd,   "errorEndDate",   errors);

        // end >= start si les deux fournis
        if (start != null && end != null && end.isBefore(start)) {
            errors.put("errorDates", "La date de fin doit être postérieure ou égale à la date de début.");
        }

        if (!errors.isEmpty()) return Result.fail(errors);

        UsersSubscriptionUpdateForm form = new UsersSubscriptionUpdateForm();
        form.setUsersSubscriptionId(usId.getData());
        form.setStartDate(start);
        form.setEndDate(end);
        form.setQuantity(qty);      // nullable → “ne pas modifier” si null
        form.setActive(active.getData());

        log.info("initUpdateForm OK → usId=" + form.getUsersSubscriptionId());
        return Result.ok(form);
    }
}
