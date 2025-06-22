package services;

import entities.HistoricalSportPrice;
import interfaces.HistoricalSportPriceService;

import javax.persistence.EntityManager;
import java.util.List;

public class HistoricalSportPriceServiceImpl implements HistoricalSportPriceService {
    private final EntityManager em;

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
