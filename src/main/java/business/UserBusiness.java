package business;
/** Regroupe la construction et la validation d'un User depuis les paramètres HTTP.
 Découpe la responsabilité : le Servlet collecte les params, UserBusiness valide et construit l'objet.*/

import Tools.Result;
import entities.Address;
import entities.User;
import enums.UserCivilite;
import java.nio.charset.StandardCharsets;
import enums.UserGender;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;


/** inscription utilisateur +regroupe validations de champs ainsi que la constrution d'objets*/



public class UserBusiness {
    private static final java.util.Set<String> CIVILITES =
            new java.util.HashSet<>(java.util.Arrays.asList("M", "Mme", "Dr", "Autre"));
    private static final java.util.Set<String> GENDERS =
            new java.util.HashSet<>(java.util.Arrays.asList("M", "F", "Autre"));

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
        String emailRaw  = trim(p.get("email"));
        String email     = (emailRaw == null) ? null : emailRaw.toLowerCase();        String phone     = trim(p.get("phone"));
        String phoneDigits = phone == null ? null : phone.replaceAll("[^0-9]","");
        String birthdate = trim(p.get("birthdate"));
        String gender    = trim(p.get("gender"));
        String civilite  = trim(p.get("civilite"));
        String password  = p.get("password");
        String password2 = p.get("password2");

        // Adresse
        String street = trim(p.get("street"));
        String number = trim(p.get("number"));
        String cityId = trim(p.get("cityId"));

        //validation des champs (contrainte message erreur)
        if (len(firstName) < 2) errors.put("firstName", "Prénom trop court");
        if (len(lastName)  < 2) errors.put("lastName",  "Nom trop court");
        if (email == null || !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            errors.put("email", "Email invalide (ex. nom@domaine.be)");
        }
        if (phoneDigits == null || phoneDigits.length() < 8) {
            errors.put("phone", "Téléphone invalide (8 chiffres minimum)");
        }
        // Vérification mot de passe sécurisé
        if (password == null || password.length() < 8) {
            errors.put("password", "Le mot de passe doit contenir au moins 8 caractères.");
        } else {
            boolean hasUppercase = password.matches(".*[A-Z].*");
            boolean hasLowercase = password.matches(".*[a-z].*");
            boolean hasDigit = password.matches(".*[0-9].*");

            if (!hasUppercase || !hasLowercase || !hasDigit) {
                errors.put("password", "Le mot de passe doit contenir au moins une majuscule, une minuscule et un chiffre.");
            }
        }

       // Vérification confirmation
        if (password2 == null || !password2.equals(password)) {
            errors.put("password2", "Les mots de passe ne correspondent pas.");
        }

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

        // Civilité / Genre : validation directe ENUM MySQL
        if (civilite == null || civilite.trim().isEmpty()) {
            errors.put("civilite", "Civilité requise");
        } else if (!CIVILITES.contains(civilite)) {
            errors.put("civilite", "Civilité invalide");
        }

        if (gender == null || gender.trim().isEmpty()) {
            errors.put("gender", "Genre requis");
        } else if (!GENDERS.contains(gender)) {
            errors.put("gender", "Genre invalide");
        }

        // Adresse verification
        if (len(street) == 0) errors.put("street", "Rue obligatoire");
        Integer num = null;
        try { num = Integer.parseInt(number); }
        catch (Exception e) { errors.put("number", "Numéro invalide"); }

        if (cityId == null)
            errors.put("cityId", "Sélectionne une ville après vérification du code postal");

        // Si erreur, on arrête ici
        if (!errors.isEmpty()) return Result.fail(errors);


        // Création de l'adresse (construction des objets)
        Address address = new Address();
        address.setStreetName(street);
        address.setStreetNumber(num);
        address.setActive(true);

        // Création de l'utilisateur
        User u = new User();
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setEmail(email);
        u.setPhone(phone);
        u.setBirthdate(dob);
        u.setGender(gender);
        u.setCivilite(civilite);
        u.setPassword(password); // (sera hashé)
        u.setActive(true);
        u.setBlacklist(false);
        u.setAddress(address);

        return Result.ok(u);
    }

    // Méthodes utilitaires privées

    /**
     *
     * @param s chaine a nettoyer retirer les espaces inutile
     * @return chaine nettoyee ou null
     */
    private static String trim(String s) { return s == null ? null : s.trim(); }

    private static int len(String s) { return s == null ? 0 : s.length(); }


    /**
     * @param password Mot de passe en texte clair
     * @return  Hash hexadécimal SHA-256
     */
    public static String hashPasswordSha256(String password) {
    try {
          MessageDigest digest = MessageDigest.getInstance("SHA-256");
          byte[] bytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

          StringBuilder sb = new StringBuilder(bytes.length * 2);
          for (byte b : bytes) sb.append(String.format("%02x", b));
          return sb.toString();

         } catch (NoSuchAlgorithmException e) {
        //Sha-256 existe toujours -> runtime si indisponible
          throw new RuntimeException("SHA-256 indisponible", e);
        }
    }
}

