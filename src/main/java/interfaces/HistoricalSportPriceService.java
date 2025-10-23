package interfaces;

import Tools.Result;
import entities.HistoricalSportPrice;

import java.util.List;

public interface HistoricalSportPriceService {


    /**
     * retrieves a historicalSportPrice by its Id
     * @param id
     * @return
     */
    Result<HistoricalSportPrice> getOneById(int id);

    /**
     * retrieves all historicalSportPrices
     * @return a list of all historicalSportPrices
     */
    Result<List<HistoricalSportPrice>> getAllHistoricalSportPrices() ;
}
