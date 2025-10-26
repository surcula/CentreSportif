package interfaces;

import Tools.Result;
import entities.Country;

import java.util.List;

public interface CountriesService {


    /**
     * Retrieves Active Countries
     * @return
     */
    Result<List<Country>> getAllActiveCountries();
}
