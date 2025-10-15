package business;
/** Regroupe la construction et la validation d'un User depuis les paramètres HTTP.
 Découpe la responsabilité : le Servlet collecte les params, UserBusiness valide et construit l'objet.*/

import Tools.Result;
import entities.Address;
import entities.City;
import entities.Role;
import entities.User;
import enums.UserCivilite;
import enums.UserGender;
import javax.persistence.EntityManager;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;


/** inscription utilisateur +regroupe validations de champs ainsi que la constrution d'objets*/



public class UserBusiness {
    /**
     *
     * @param p Map contenant les paramètres du formulaire (ex: firstName, email, phone, etc.)
     *
     * @return Result.ok(User) si tous les champs sont valides, sinon Result.fail(Map<champ,erreur>)
     */
    public static Result<User> buildUserFromRequest(Map<String, String> p) {
        Map<String, String> errors = new HashMap<>();
        // recuperation champs formulaire//
        String firstName = trim(p.get("firstName"));
        String lastName  = trim(p.get("lastName"));
        String email     = trim(p.get("email")) == null ? null : trim(p.get("email")).toLowerCase();
        String phone     = trim(p.get("phone"));
        String birthdate = trim(p.get("birthdate"));
        String gender    = trim(p.get("gender"));
        String civilite  = trim(p.get("civilite"));
        String password  = p.get("password");
        String password2 = p.get("password2");

        // Adresse
        String street = trim(p.get("street"));
        String number = trim(p.get("number"));
        String box    = trim(p.get("box"));
        String cityId = trim(p.get("cityId"));

        //validation des champs (contrainte message erreur)
        if (len(firstName) < 2) errors.put("firstName", "Prénom trop court");
        if (len(lastName)  < 2) errors.put("lastName",  "Nom trop court");
        if (email == null || !email.contains("@")) errors.put("email", "Email invalide");
        if (len(phone) < 8) errors.put("phone", "Téléphone invalide");
        if (password == null || password.length() < 6) errors.put("password", "Mot de passe ≥ 6 caractères");
        if (password2 == null || !password2.equals(password)) errors.put("password2", "Confirmation différente");

        // Date de naissance min 18 ans max 120 ans
        LocalDate dob = null;
        try {
            dob = LocalDate.parse(birthdate);
            LocalDate today = LocalDate.now();

            //Vérifier si la date est dans le futur
            if (dob.isAfter(today)) {
                errors.put("birthdate", "Date de naissance dans le futur");
            }
            // âge >= 18 ans
            else if (dob.isAfter(today.minusYears(18))) {
                errors.put("birthdate", "Vous devez avoir au moins 18 ans pour vous inscrire");
            }
            // éviter les dates trop anciennes (> 120 ans)
            else if (dob.isBefore(today.minusYears(120))) {
                errors.put("birthdate", "Date de naissance invalide");
            }

        } catch (DateTimeParseException e) {
            errors.put("birthdate", "Date invalide (format : YYYY-MM-DD)");
        }

        // Civilité / Genre
        UserGender userGender = mapGender(gender);
        UserCivilite userCivilite = mapCivilite(civilite);
        if (userGender == null) errors.put("gender", "Genre requis");
        if (userCivilite == null) errors.put("civilite", "Civilité requise");

        // Adresse verification
        if (len(street) == 0) errors.put("street", "Rue obligatoire");
        Integer num = null;
        try { num = Integer.parseInt(number); }
        catch (Exception e) { errors.put("number", "Numéro invalide"); }

        if (cityId == null) errors.put("cityId", "Sélectionne une ville après vérification du code postal");

        // Si erreur, on arrête ici
        if (!errors.isEmpty()) return Result.fail(errors);

//        // Récupération de la ville
//        Integer cid = Integer.parseInt(cityId);
//        City city = em.find(City.class, cid);
//        if (city == null) {
//            errors.put("cityId", "Ville introuvable");
//            return Result.fail(errors);
//        }
        //address.setCity(city);

        // Création de l'adresse
        Address address = new Address();
        address.setStreetName(street);
        address.setStreetNumber(num);
        //verifie que la boite est vide ,sinon converti en double
        Double boxNum = 0.0;
        if (box != null && !box.isEmpty()){
            try {
                boxNum = Double.parseDouble(box);
            } catch (NumberFormatException e) {
                errors.put("box", "Boite : nombre décimal attendu");
            }
        }
        address.setBoxNumber(boxNum);

        address.setActive(true);

        // Création de l'utilisateur
        User u = new User();
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setEmail(email);
        u.setPhone(phone);
        u.setBirthdate(dob);
        u.setGender(userGender);
        u.setCivilite(userCivilite);
        u.setPassword(password); // (option : hash plus tard)
        u.setActive(true);
        u.setBlacklist(false);
        u.setAddress(address);


        // Succès
        return Result.ok(u);
    }

    // Méthodes utilitaires privées

    /**
     *
     * @param s chaine a nettoyer
     * @return chaine nettoyee ou null
     */
    private static String trim(String s) { return s == null ? null : s.trim(); }

    private static int len(String s) { return s == null ? 0 : s.length(); }

    private static UserGender mapGender(String s) {
        if (s == null) return null;
        switch (s) {
            case "M": return UserGender.MALE;
            case "F": return UserGender.FEMALE;
            case "Autre": return UserGender.OTHERS;
            default: return null;
        }
    }

    private static UserCivilite mapCivilite(String s) {
        if (s == null) return null;
        switch (s) {
            case "M": return UserCivilite.MR;
            case "Mme": return UserCivilite.MS;
            case "Dr": return UserCivilite.MISS;
            case "Autre": return UserCivilite.MISS;
            default: return null;
        }

    }


    public String HashPAssword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] test = digest.digest(password.getBytes());
        return test.toString();
    }
}
