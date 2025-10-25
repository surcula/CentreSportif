package services;

import Tools.Result;
import entities.Role;
import interfaces.RoleService;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleServiceImpl implements RoleService {

    private final EntityManager em;
    private static final Logger log = Logger.getLogger(RoleServiceImpl.class);

    /**
     * @param em
     */
    public RoleServiceImpl(EntityManager em) {
        this.em = em;
    }

    /**
     * @return liste des roles qui existe
     */
    @Override
    public Result<List<Role>> getAll() {
        List<Role> roles = em.createNamedQuery("Role.findAll", Role.class) // <-- findAll
                .getResultList();
        return Result.ok(roles);
    }

    /**
     * @param id  Récupère un rôle à partir de son identifiant
     * @return
     */
    @Override
    public Result<Role> getOneById(int id) {
        Role r = em.find(Role.class, id);
        if (r == null) {
            log.warn("Role " + id + " not found");
            Map<String, String> err = new HashMap<>();
            err.put("notFound", "Aucun rôle trouvé avec l’ID " + id);
            return Result.fail(err);
        }
        log.info("Role " + r.getId() + " found");
        return Result.ok(r);
    }

    /**
     * @param name  Récupère un rôle à partir de son nom
     * @return
     */
    @Override
    public Result<Role> getByName(String name) {
        try {
            Role r = em.createNamedQuery("Role.findByName", Role.class)    // <-- findByName
                    .setParameter("name", name)
                    .getSingleResult();
            return Result.ok(r);
        } catch (NoResultException e) {
            Map<String,String> err = new HashMap<>();
            err.put("notFound", "Aucun rôle : " + name);
            return Result.fail(err);
        }
    }
}
