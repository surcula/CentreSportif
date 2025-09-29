package services;

import Tools.Result;
import entities.HistoricalSportPrice;
import interfaces.HistoricalSportPriceService;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.util.List;

public class HistoricalSportPriceServiceImpl implements HistoricalSportPriceService {
    private final EntityManager em;
    // Log4j
    private static Logger log = Logger.getLogger(EntityFinderImpl.class);

    public HistoricalSportPriceServiceImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Result<HistoricalSportPrice> getOneById(int id) {

        return Result.ok(em.find(HistoricalSportPrice.class, id));

    }

    @Override
    public Result<List<HistoricalSportPrice>> getAllHistoricalSportPrices() {
        return Result.ok(em.createQuery("Select h from HistoricalSportPrice h", HistoricalSportPrice.class).getResultList());
    }
}
