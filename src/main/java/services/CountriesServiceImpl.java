package services;

import Tools.Result;
import entities.Country;
import interfaces.CountriesService;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * interagit avec la base de données pour récupérer la liste des pays actifs
 */
public class CountriesServiceImpl implements CountriesService {

    private final EntityManager em;

    /**
     * @param em
     */
    public CountriesServiceImpl(EntityManager em) {
        this.em = em;
    }

    /**
     * @return le resultat ou erreur dans la liste des pays
     */
    @Override
    public Result<List<Country>> getAllActiveCountries() {
        try{
            List<Country> countries = em.createNamedQuery("getAllActiveCountries", Country.class).getResultList();
            // LOG
            return Result.ok(countries);
        }catch(Exception ex){
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            //LOG
            return Result.fail(errors);
        }

    }
}
