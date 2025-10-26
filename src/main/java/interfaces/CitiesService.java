package interfaces;

import Tools.Result;
import entities.City;

import java.util.List;

public interface CitiesService {
    /**
     * Retrieves Active Cities
     * @return all the active cities
     */
    Result<List<City>> getAllActiveCities();

    /**
     * Retrieves Cities by zipcode
     * @param zip zipcode of the cities
     * @return all the zipcode of the cities
     */
    Result<List<City>> getActiveByZip(int zip);
}
