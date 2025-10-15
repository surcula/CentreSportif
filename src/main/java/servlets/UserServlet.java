package servlets;
/** Contoleur HTTP pour l'inscription des utilisateurs*/
import Tools.Result;
import business.ServletUtils;
import business.UserBusiness;
import dto.EMF;
import entities.Address;
import entities.City;
import entities.User;
import services.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static constants.Rooting.TEMPLATE;
import static constants.Rooting.USER_FORM_JSP;

@WebServlet(name = "UserServlet", value = "/user")
public class UserServlet extends HttpServlet {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(UserServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        javax.persistence.EntityManager em = null;
        try {
            em = dto.EMF.getEM();
            java.util.List<entities.Country> countries = em.createQuery(
                    "SELECT c FROM Country c WHERE c.active = true ORDER BY c.countryName", entities.Country.class
            ).getResultList();
            req.setAttribute("countries", countries);
            req.setAttribute("countryList", countries);
            req.setAttribute("allCountries", countries);
        } catch (Throwable t) {
            org.apache.log4j.Logger.getLogger(UserServlet.class).error("EMF init / load countries failed", t);
            // on n’empêche pas l’affichage de la page si EM indispo
        } finally {
            if (em != null) em.close();
        }

        business.ServletUtils.forwardWithContent(req, resp, constants.Rooting.USER_FORM_JSP, constants.Rooting.TEMPLATE);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String,String> p = new HashMap<>();
        req.getParameterMap().forEach((k,v) -> p.put(k, v != null && v.length>0 ? v[0] : null));
        String action = p.get("action");

        EntityManager em = null;
        try {
            // Sécuriser EMF.getEM() pour éviter le 500
            try {
                em = EMF.getEM();
            } catch (Throwable t) {
                log.error("Erreur lors de l'initialisation de EMF", t);
                req.setAttribute("formError", "Impossible de se connecter à la base de données.");
                ServletUtils.forwardWithContent(req, resp, USER_FORM_JSP, TEMPLATE);
                return;
            }

            // ---------- A) CHECK ZIP : charger les villes ----------
            if ("checkZip".equals(action)) {
                String zipStr = p.get("zip");
                try {
                    int zip = Integer.parseInt(zipStr);
                    List<City> cities = em.createQuery(
                            "SELECT c FROM City c WHERE c.zipCode = :z AND c.active = true ORDER BY c.cityName",
                            City.class
                    ).setParameter("z", zip).getResultList();

                    // villes -> JSP (avec alias compatibles)
                    req.setAttribute("cities", cities);
                    req.setAttribute("cityList", cities);
                    req.setAttribute("allCities", cities);
                } catch (Exception e) {
                    Map<String, String> errors = new HashMap<>();
                    errors.put("zip", "Code postal invalide");
                    req.setAttribute("errors", errors);
                }

                // pays -> JSP (avec alias compatibles)
                List<entities.Country> countries = em.createQuery(
                        "SELECT c FROM Country c WHERE c.active = true ORDER BY c.countryName", entities.Country.class
                ).getResultList();
                req.setAttribute("countries", countries);
                req.setAttribute("countryList", countries);
                req.setAttribute("allCountries", countries);

                // conserver les valeurs saisies
                req.setAttribute("old", p);
                req.setAttribute("zip", p.get("zip"));

                ServletUtils.forwardWithContent(req, resp, USER_FORM_JSP, TEMPLATE);
                return;
            }

            // ---------- B) SUBMIT : inscription ----------
            if ("submit".equals(action)) {
                UserServiceImpl userService = new UserServiceImpl(em);

                // Unicité email
                Result<Boolean> emailOK = userService.emailAvailable(p.get("email"));
                if (!emailOK.isSuccess() || !emailOK.getData()) {
                    Map<String, String> errors = new HashMap<>();
                    errors.put("email", "Email déjà utilisé");
                    req.setAttribute("errors", errors);
                    req.setAttribute("old", p);

                    // recharger pays (+ alias)
                    List<entities.Country> countries = em.createQuery(
                            "SELECT c FROM Country c WHERE c.active = true ORDER BY c.countryName", entities.Country.class
                    ).getResultList();
                    req.setAttribute("countries", countries);
                    req.setAttribute("countryList", countries);
                    req.setAttribute("allCountries", countries);

                    // recharger villes si zip connu
                    String oldZip = p.get("zip");
                    if (oldZip != null && oldZip.matches("\\d+")) {
                        List<City> cities = em.createQuery(
                                "SELECT c FROM City c WHERE c.zipCode = :z AND c.active = true ORDER BY c.cityName",
                                City.class
                        ).setParameter("z", Integer.parseInt(oldZip)).getResultList();
                        req.setAttribute("cities", cities);
                        req.setAttribute("cityList", cities);
                        req.setAttribute("allCities", cities);
                    }

                    ServletUtils.forwardWithContent(req, resp, USER_FORM_JSP, TEMPLATE);
                    return;
                }

                // Construction + validations
                Result<User> built = UserBusiness.buildUserFromRequest(p, em);
                if (!built.isSuccess()) {
                    req.setAttribute("errors", built.getErrors());
                    req.setAttribute("old", p);

                    // recharger pays (+ alias)
                    List<entities.Country> countries = em.createQuery(
                            "SELECT c FROM Country c WHERE c.active = true ORDER BY c.countryName", entities.Country.class
                    ).getResultList();
                    req.setAttribute("countries", countries);
                    req.setAttribute("countryList", countries);
                    req.setAttribute("allCountries", countries);

                    // recharger villes si zip connu
                    String oldZip = p.get("zip");
                    if (oldZip != null && oldZip.matches("\\d+")) {
                        List<City> cities = em.createQuery(
                                "SELECT c FROM City c WHERE c.zipCode = :z AND c.active = true ORDER BY c.cityName",
                                City.class
                        ).setParameter("z", Integer.parseInt(oldZip)).getResultList();
                        req.setAttribute("cities", cities);
                        req.setAttribute("cityList", cities);
                        req.setAttribute("allCities", cities);
                    }

                    ServletUtils.forwardWithContent(req, resp, USER_FORM_JSP, TEMPLATE);
                    return;
                }

                // Persist
                User user = built.getData();
                Address address = user.getAddress();

                em.getTransaction().begin();
                em.persist(address);
                em.persist(user);
                em.getTransaction().commit();

                log.info("Inscription OK : " + user.getEmail());
                ServletUtils.redirectToURL(resp, req.getContextPath() + "/login2?registered=1");
                return;
            }

            // ---------- C) Fallback ----------
            req.setAttribute("old", p);

            // recharger pays (+ alias)
            List<entities.Country> countries = em.createQuery(
                    "SELECT c FROM Country c WHERE c.active = true ORDER BY c.countryName", entities.Country.class
            ).getResultList();
            req.setAttribute("countries", countries);
            req.setAttribute("countryList", countries);
            req.setAttribute("allCountries", countries);

            // recharger villes si zip connu
            String oldZip = p.get("zip");
            if (oldZip != null && oldZip.matches("\\d+")) {
                List<City> cities = em.createQuery(
                        "SELECT c FROM City c WHERE c.zipCode = :z AND c.active = true ORDER BY c.cityName",
                        City.class
                ).setParameter("z", Integer.parseInt(oldZip)).getResultList();
                req.setAttribute("cities", cities);
                req.setAttribute("cityList", cities);
                req.setAttribute("allCities", cities);
            }

            ServletUtils.forwardWithContent(req, resp, USER_FORM_JSP, TEMPLATE);

        } catch (Exception e) {
            log.error("Erreur inscription", e);
            if (em != null && em.getTransaction().isActive()) em.getTransaction().rollback();
            ServletUtils.forwardWithError(req, resp, "Problème lors de l'inscription", USER_FORM_JSP, TEMPLATE);
        } finally {
            if (em != null) em.close();
        }
    }

}
