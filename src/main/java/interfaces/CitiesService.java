package interfaces;

import Tools.Result;
import entities.City;

import java.util.List;

public interface CitiesService {
    /**
     * Retrieves Active Cities
     * @return
     */
    Result<List<City>> getAllActiveCities();
}
