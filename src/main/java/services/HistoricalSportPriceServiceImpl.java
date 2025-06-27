package services;

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
    public HistoricalSportPrice getOneById(int id) {
        return em.find(HistoricalSportPrice.class, id);
    }

    @Override
    public List<HistoricalSportPrice> getAllHistoricalSportPrices() {
        return em.createQuery("Select h from HistoricalSportPrice h", HistoricalSportPrice.class).getResultList();
    }
}
