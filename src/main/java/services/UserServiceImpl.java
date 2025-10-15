package services;
/** jpa de UserService + EntityManager */

import Tools.Result;
import entities.User;
import interfaces.UserService;
import org.apache.log4j.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.HashMap;
import java.util.Map;

public class UserServiceImpl implements UserService {
    private static final Logger log = Logger.getLogger(UserService.class);
    private final EntityManager em;

    public UserServiceImpl(EntityManager em) { this.em = em;}

    @Override
    public Result<Boolean> emailAvailable(String email) {
        Long count = em.createNamedQuery("User.countByEmail", Long.class)
                .setParameter("email", email)
                .getSingleResult();
        return Result.ok(count ==0);
    }
    @Override
    public Result<User> create(User user){
        try{
            em.persist(user);
            log.info("User created id=" + user.getId());
            return Result.ok(user);
        } catch (Exception e) {
            log.error("create user", e);
            Map<String,String> errors = new HashMap<>();
            errors.put("create", e.getMessage());
            return Result.fail(errors);
        }
    }
    @Override
    public User findByEmail(String email){
        try {
            return em.createNamedQuery("User.findByEmail", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
