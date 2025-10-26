package servlets;

import Tools.Result;
import business.ServletUtils;
import business.UserBusiness;
import dto.EMF;
import entities.Address;
import entities.City;
import entities.Country;
import entities.User;
import org.apache.log4j.Logger;
import services.CitiesServiceImpl;
import services.CountriesServiceImpl;
import services.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static constants.Rooting.*;


/**
 * HTTP gérant le formulaire d'inscription des utilisateurs.
 * Gère l'affichage initial (GET), le filtrage des villes par code postal, et la création du compte (POST)
 */
@WebServlet(name = "UserServlet", value = "/user")
public class UserServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(UserServlet.class);


    /**
     * @param req requete HTTP
     * @param resp Reponse HTTP
     * @throws ServletException  si une erreur survient dans le dispatch
     * @throws IOException si une erreur d'entrée/sortie survient
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        EntityManager em = EMF.getEM();
        try {
            //Recup countries et cities
            CountriesServiceImpl countriesService = new CountriesServiceImpl(em);
            CitiesServiceImpl citiesService = new CitiesServiceImpl(em);

            Result<List<Country>> countries = countriesService.getAllActiveCountries();
            Result<List<City>> cities = citiesService.getAllActiveCities();


            // Attachement des données à la requête pour affichage JSP
            req.setAttribute("countries", countries.isSuccess() ? countries.getData() : java.util.Collections.emptyList());
            req.setAttribute("cities", cities.isSuccess() ? cities.getData() : java.util.Collections.emptyList());

        } catch (Throwable t) {
            log.error("EMF init / load countries failed", t);
            req.setAttribute("formError", "Impossible de charger les données du formulaire.");

            // empêche pas l’affichage de la page si EM indispo
        } finally {
            if (em != null) em.close();
        }
       ServletUtils.forwardWithContent(req, resp, USER_FORM_JSP, TEMPLATE);
    }

    /**
     * @param req Requête HTTP contenant les champs du formulaire
     * @param resp Réponse HTTP
     * @throws ServletException
     * @throws IOException
     */
    // POST : traitement du formulaire
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String,String> p = new HashMap<>();
        req.getParameterMap().forEach((k,v) -> p.put(k, v != null && v.length>0 ? v[0] : null));
        String action = p.get("action");

        EntityManager em = EMF.getEM();
      try{
            // CHECK ZIP : charger les villes
            if ("checkZip".equals(action)) {
                CountriesServiceImpl countriesService = new CountriesServiceImpl(em);
                CitiesServiceImpl citiesService = new CitiesServiceImpl(em);

                String zipStr = p.get("zip");
                if (zipStr != null && zipStr.matches("\\d+")){
                    int zip = Integer.parseInt(zipStr);
                    Tools.Result<java.util.List<entities.City>> citiesRes = citiesService.getActiveByZip(zip);                    // villes -> JSP
                    req.setAttribute("cities", citiesRes.isSuccess() ? citiesRes.getData() : java.util.Collections.emptyList());
                } else {

                    Map<String, String> errors = new HashMap<>();
                    errors.put("zip", "Code postal invalide");
                    req.setAttribute("errors", errors);
                    req.setAttribute("cities", java.util.Collections.emptyList());
                }

                // pays -> JSP
                Tools.Result<java.util.List<entities.Country>> countriesRes = countriesService.getAllActiveCountries();
                req.setAttribute("countries", countriesRes.isSuccess() ? countriesRes.getData() : java.util.Collections.emptyList());

                // conserve les valeurs saisies
                req.setAttribute("old", p);
                req.setAttribute("zip", p.get("zip"));

                ServletUtils.forwardWithContent(req, resp, USER_FORM_JSP, TEMPLATE);
                return;
            }

            // SUBMIT : inscription
            if ("submit".equals(action)) {
                UserServiceImpl userService = new UserServiceImpl(em);

                // vérif Unicité email
                Result<Boolean> emailOK = userService.emailAvailable(p.get("email"));
                if (!emailOK.isSuccess() || !emailOK.getData()) {
                    Map<String, String> errors = new HashMap<>();
                    errors.put("email", "Email déjà utilisé");
                    req.setAttribute("errors", errors);
                    req.setAttribute("old", p);
                    reloadLists(req, em, p);
                    ServletUtils.forwardWithContent(req, resp, USER_FORM_JSP, TEMPLATE);
                    return;
                }


                // Construction + validations user
                Result<User> built = UserBusiness.buildUserFromRequest(p);
                if (!built.isSuccess()) {
                    req.setAttribute("errors", built.getErrors());
                    req.setAttribute("old", p);
                    reloadLists(req, em, p);
                    ServletUtils.forwardWithContent(req, resp, USER_FORM_JSP, TEMPLATE);
                    return;
                }

                // Persist
                User user = built.getData();
                Address address = user.getAddress();


               // Lier la ville choisie
                String cityIdStr = p.get("cityId");
                if (cityIdStr == null || !cityIdStr.matches("\\d+")) {
                    Map<String,String> errors = new HashMap<>();
                    errors.put("cityId", "Sélectionne une ville");
                    req.setAttribute("errors", errors);
                    req.setAttribute("old", p);
                    reloadLists(req, em, p);
                    ServletUtils.forwardWithContent(req, resp, USER_FORM_JSP, TEMPLATE);
                    return;
                }
                int cityId = Integer.parseInt(cityIdStr);
                City chosen = em.find(City.class, cityId);
                if (chosen == null || !chosen.isActive()) {
                    Map<String,String> errors = new HashMap<>();
                    errors.put("cityId", "Ville introuvable/inactive");
                    req.setAttribute("errors", errors);
                    req.setAttribute("old", p);
                    reloadLists(req, em, p);
                    ServletUtils.forwardWithContent(req, resp, USER_FORM_JSP, TEMPLATE);
                    return;
                }
                address.setCity(chosen);
                // Rôle par défaut = utilisateur (id = 4)
                entities.Role defaultRole = em.find(entities.Role.class, 4);
                if (defaultRole == null || !defaultRole.isActive()) {
                    Map<String,String> errors = new HashMap<>();
                    errors.put("role", "Rôle par défaut introuvable (id=4).");
                    req.setAttribute("errors", errors);
                    req.setAttribute("old", p);
                    reloadLists(req, em, p);
                    ServletUtils.forwardWithContent(req, resp, USER_FORM_JSP, TEMPLATE);
                    return;
                }
                user.setRole(defaultRole);
                //Hash MDP
                user.setPassword(UserBusiness.hashPasswordSha256(user.getPassword()));

                //transaction
                em.getTransaction().begin();
                em.persist(address);
                em.persist(user);
                em.getTransaction().commit();

                log.info("Inscription OK : " + user.getEmail());
                ServletUtils.redirectToURL(resp, req.getContextPath() + "/login?msg=Inscription+reussie.+Veuillez+vous+connecter.&type=success");
                return;
            }

            //  cas par defaut
            req.setAttribute("old", p);
          reloadLists(req, em, p);
          ServletUtils.forwardWithContent(req, resp, USER_FORM_JSP, TEMPLATE);

        } catch (Exception e) {
            log.error("Erreur inscription", e);
            if (em != null && em.getTransaction().isActive()) em.getTransaction().rollback();
            ServletUtils.forwardWithError(req, resp, "Problème lors de l'inscription", USER_FORM_JSP, TEMPLATE);
        } finally {
            if (em != null) em.close();
        }
    }

    /**
     * @param req requete HTTP Recharge les listes des pays et des villes après une erreur
     * @param em EntityManager pour exécuter les requêtes via les services
     * @param p Map des champs saisis dans le formulaire
     */
    // Méthode utilitaire : recharger pays / villes
    private void reloadLists(HttpServletRequest req, EntityManager em, Map<String, String> p) {
        CountriesServiceImpl countriesService = new CountriesServiceImpl(em);
        CitiesServiceImpl    citiesService    = new CitiesServiceImpl(em);

        // Pays
        Tools.Result<java.util.List<entities.Country>> countriesRes = countriesService.getAllActiveCountries();
        if (countriesRes.isSuccess()) {
            req.setAttribute("countries", countriesRes.getData());
        } else {
            req.setAttribute("countries", java.util.Collections.emptyList());
        }

        // Villes (selon le zip saisi)
        String oldZip = p.get("zip");
        if (oldZip != null && oldZip.matches("\\d+")) {
            int zip = Integer.parseInt(oldZip);
            Tools.Result<java.util.List<entities.City>> citiesRes = citiesService.getActiveByZip(zip);
            if (citiesRes.isSuccess()) {
                req.setAttribute("cities", citiesRes.getData());
            } else {
                req.setAttribute("cities", java.util.Collections.emptyList());
            }
        } else {
            req.setAttribute("cities", java.util.Collections.emptyList());
        }
    }
}
