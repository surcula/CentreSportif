package services;

import Tools.Result;
import entities.City;
import entities.Country;
import interfaces.CitiesService;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CitiesServiceImpl implements CitiesService {

    private final EntityManager em;

    /**
     * @param em Cette classe interagit avec la base via  EntityManager et
     *  * exécute les requêtes définies dans l’entité
     */
    public CitiesServiceImpl(EntityManager em) {
        this.em = em;
    }


    /**
     * @return un Resultat ou une erreur. dans la liste des villes,
     *      *
     */
    @Override
    public Result<List<City>> getAllActiveCities() {
        try{
            List<City> cities = em.createNamedQuery("getAllActiveCities", City.class).getResultList();
            //LOG
            return Result.ok(cities);
        }catch(Exception ex){
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            //LOG
            return Result.fail(errors);
        }
    }

    /**
     * @param zip Retourne la liste des villes actives correspondant à un code postal donné
     * @return
     */
    @Override
    public Result<List<City>> getActiveByZip(int zip) {
        try{
            List<City> cities = em.createNamedQuery("getActiveCitiesByZip", City.class)
                    .setParameter("zip", zip)
                    .getResultList();
            return Result.ok(cities);
        }catch (Exception ex){
            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());
            return Result.fail(errors);
        }
    }
}
