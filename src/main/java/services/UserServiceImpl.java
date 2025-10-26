package services;

import Tools.Result;
import dto.EMF;
import entities.Role;
import entities.User;
import interfaces.UserService;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Implémentation JPA de UserService. */
public class UserServiceImpl implements UserService {
    private static final Logger log = Logger.getLogger(UserService.class);
    private final EntityManager em;

    /** Conformité avec ton projet: le EM est injecté par le constructeur. */
    public UserServiceImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Result<Boolean> emailAvailable(String email) {
        Long count = em.createNamedQuery("User.countByEmail", Long.class)
                .setParameter("email", email)
                .getSingleResult();
        return Result.ok(count == 0);
    }

    @Override
    public Result<User> create(User user) {
        try {
            em.persist(user);
            log.info("User created id=" + user.getId());
            return Result.ok(user);
        } catch (Exception e) {
            log.error("create user", e);
            Map<String, String> errors = new HashMap<>();
            errors.put("create", e.getMessage());
            return Result.fail(errors);
        }
    }

    @Override
    public User findByEmail(String email) {
        try {
            return em.createNamedQuery("User.findByEmail", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /* ==== AJOUTS pour l’admin/users ==== */

    @Override
    public List<User> findAllOrdered() {
        return em.createNamedQuery("User.findAllOrdered", User.class).getResultList();
    }

    @Override
    public List<User> search(String q) {
        return em.createNamedQuery("User.searchQ", User.class)
                .setParameter("q", q)
                .getResultList();
    }

    @Override
    public List<User> search(String q, Boolean status, int page, int size) {

        return search(q, status, page, size, null);
    }

    @Override
    public long count(String q, Boolean status) {

        return count(q, status, null);
    }

    @Override
    public List<User> search(String q, Boolean status, int page, int size, java.util.List<String> excludedRoles) {
        boolean noExcl = (excludedRoles == null || excludedRoles.isEmpty());
        return (noExcl
                ? em.createNamedQuery("User.searchQStatus", User.class)
                .setParameter("q", q == null ? "" : q)
                .setParameter("status", status)
                .setFirstResult(Math.max(0, (page - 1) * size))
                .setMaxResults(size)
                : em.createNamedQuery("User.searchQStatusExcl", User.class)
                .setParameter("q", q == null ? "" : q)
                .setParameter("status", status)
                .setParameter("excl", excludedRoles)
                .setFirstResult(Math.max(0, (page - 1) * size))
                .setMaxResults(size)
        ).getResultList();
    }

    @Override
    public long count(String q, Boolean status, java.util.List<String> excludedRoles) {
        boolean noExcl = (excludedRoles == null || excludedRoles.isEmpty());
        return (noExcl
                ? em.createNamedQuery("User.countQStatus", Long.class)
                .setParameter("q", q == null ? "" : q)
                .setParameter("status", status)
                : em.createNamedQuery("User.countQStatusExcl", Long.class)
                .setParameter("q", q == null ? "" : q)
                .setParameter("status", status)
                .setParameter("excl", excludedRoles)
        ).getSingleResult();
    }

    @Override
    public void toggleActive(int id) {
        em.getTransaction().begin();
        User u = em.find(User.class, id);
        if (u != null) u.setActive(!u.isActive());
        em.getTransaction().commit();
    }

    @Override
    public void toggleBlacklist(int id) {
        em.getTransaction().begin();
        User u = em.find(User.class, id);
        if (u != null) u.setBlacklist(!Boolean.TRUE.equals(u.getBlacklist()));
        em.getTransaction().commit();
    }

    @Override
    public void changeRole(int id, String roleName) {
        em.getTransaction().begin();
        User u = em.find(User.class, id);
        if (u != null) {
            try {
                Role r = em.createNamedQuery("Role.findByName", Role.class)
                        .setParameter("name", roleName)
                        .getSingleResult();
                u.setRole(r);
            } catch (NoResultException ignore) { /* no-op */ }
        }
        em.getTransaction().commit();
    }
}
