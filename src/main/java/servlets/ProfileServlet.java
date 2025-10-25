package servlets;

import Tools.Result;
import business.ServletUtils;
import dto.EMF;
import entities.User;
import entities.City;
import entities.Country;
import entities.Address;
import services.CitiesServiceImpl;
import services.CountriesServiceImpl;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * En GET : affiche le profil connecté et précharge les listes de pays et de villes.<br>
 * En POST : met à jour les informations du profil (identité + adresse).
 */
@WebServlet(name = "ProfileServlet", value = "/profile")
public class ProfileServlet extends HttpServlet {

    private static final String TEMPLATE = "/views/template/template.jsp";
    private static final String JSP = "/views/profile.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/login?msg=Connexion requise&type=danger");
            return;
        }

        Number idNum = (Number) s.getAttribute("userId");
        int uid = idNum.intValue();

        EntityManager em = EMF.getEM();
        try {
            // Récupération de l'utilisateur connecté
            User u = em.find(User.class, uid);
            req.setAttribute("user", u);

            // ----- Pays -----
            CountriesServiceImpl countries = new CountriesServiceImpl(em);
            Result<List<Country>> ctry = countries.getAllActiveCountries();
            req.setAttribute("countries", ctry.isSuccess() ? ctry.getData() : Collections.emptyList());

            // ----- Villes -----
            CitiesServiceImpl cities = new CitiesServiceImpl(em);
            Result<List<City>> allCities = cities.getAllActiveCities();
            req.setAttribute("cities", allCities.isSuccess() ? allCities.getData() : Collections.emptyList());

            // ----- Sélections actuelles -----
            Integer selectedCityId = (u.getAddress() != null && u.getAddress().getCity() != null)
                    ? u.getAddress().getCity().getId() : null;
            req.setAttribute("selectedCityId", selectedCityId);

            Integer selectedCountryId = (u.getAddress() != null && u.getAddress().getCity() != null
                    && u.getAddress().getCity().getCountry() != null)
                    ? u.getAddress().getCity().getCountry().getId() : null;
            req.setAttribute("selectedCountryId", selectedCountryId);

            // ----- Affichage -----
            ServletUtils.forwardWithContent(req, resp, JSP, TEMPLATE);

        } finally {
            em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/login?msg=Connexion requise&type=danger");
            return;
        }

        Number idNum = (Number) s.getAttribute("userId");
        int uid = idNum.intValue();

        EntityManager em = EMF.getEM();
        try {
            em.getTransaction().begin();

            User u = em.find(User.class, uid);
            if (u == null) {
                resp.sendRedirect(req.getContextPath() + "/login?msg=Utilisateur introuvable&type=danger");
                return;
            }

            // Identité
            u.setFirstName(req.getParameter("firstName"));
            u.setLastName(req.getParameter("lastName"));
            u.setPhone(req.getParameter("phone"));
            u.setCivilite(req.getParameter("civilite"));
            u.setGender(req.getParameter("gender"));

            String birthdate = req.getParameter("birthdate");
            if (birthdate != null && !birthdate.trim().isEmpty()) {
                u.setBirthdate(LocalDate.parse(birthdate));
            }

            // Adresse
            String street = req.getParameter("street");
            String number = req.getParameter("number");
            String cityIdStr = req.getParameter("cityId");
            String countryIdStr = req.getParameter("countryId");

            Address a = u.getAddress();
            if (a == null) {
                a = new Address();
                a.setActive(true);
                u.setAddress(a);
                em.persist(a);
            }

            a.setStreetName(street);
            try {
                a.setStreetNumber(Integer.parseInt(number));
            } catch (Exception ex) {
                a.setStreetNumber(0);
            }

            // Ville
            if (cityIdStr != null && cityIdStr.matches("\\d+")) {
                City chosen = em.find(City.class, Integer.parseInt(cityIdStr));
                if (chosen != null && chosen.isActive()) {
                    a.setCity(chosen);
                }
            }
            // Pays
            if (countryIdStr != null && countryIdStr.matches("\\d+")) {
                Country chosenCountry = em.find(Country.class, Integer.parseInt(countryIdStr));
                if (chosenCountry != null && chosenCountry.isActive()) {
                    // on met à jour le pays de la ville si nécessaire
                    if (a.getCity() != null) {
                        a.getCity().setCountry(chosenCountry);
                    }
                }
            }
            // validation de transaction
            em.getTransaction().commit();
            resp.sendRedirect(req.getContextPath() + "/profile?msg=Profil+mis+a+jour&type=success");

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) em.getTransaction().rollback();
            ServletUtils.forwardWithError(req, resp, "Erreur lors de la mise à jour du profil", JSP, TEMPLATE);
        } finally {
            if (em != null) em.close();
        }
    }
}
