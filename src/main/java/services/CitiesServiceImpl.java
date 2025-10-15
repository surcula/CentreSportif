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
    public CitiesServiceImpl(EntityManager em) {
        this.em = em;
    }


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
}
