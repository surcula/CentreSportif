package interfaces;

import Tools.Result;
import entities.UsersSubscription;

import java.time.LocalDate;
import java.util.List;

public interface SubscriptionService {

    /* ===== Affectation (back-office) ===== */
    Result<UsersSubscription> assignToUser(int subscriptionId, int userId,
                                           LocalDate start, LocalDate end, int quantity);

    /* ===== Recherches ===== */
    Result<UsersSubscription> findUsersSubscription(int id);
    Result<List<UsersSubscription>> findByUser(int userId);

    /* ===== Règles + opérations de solde ===== */
    Result<Boolean> isValid(UsersSubscription us, LocalDate now);
    Result<Boolean> debit(UsersSubscription us, int qty);   // qty tokens / unités
    Result<Boolean> credit(UsersSubscription us, int qty);
}
