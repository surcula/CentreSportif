package interfaces;

import Tools.Result;
import entities.Discount;

import java.time.Instant;
import java.util.List;

public interface DiscountService {

    /* ===== CRUD minimal ===== */
    Result<Discount> create(Discount discount);
    Result<Discount> findOne(int id);

    /* ===== Recherches ===== */
    Result<Discount> findByName(String name);
    Result<List<Discount>> findAllActives(Instant now);

    /* ===== Règles d’applicabilité ===== */
    Result<Boolean> isApplicable(Discount discount, Instant now);
}
