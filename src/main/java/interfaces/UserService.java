package interfaces;
/** Service applicatif User. */

import Tools.Result;
import entities.User;

import java.util.List;

public interface UserService {
    /** @return Result.ok(true) si aucun compte n'existe avec cet email. */
    Result<Boolean> emailAvailable(String email);

    /** Création d'un utilisateur. */
    Result<User> create(User user);

    /** @return l'utilisateur par email ou null si inexistant. */
    User findByEmail(String email);

    /* ==== AJOUTS pour l’admin/users ==== */

    /** Liste ordonnée nom/prénom. Requiert NamedQuery "User.findAllOrdered". */
    List<User> findAllOrdered();

    /** Recherche simple sur nom/prénom/email. Requiert NamedQuery "User.searchQ". */
    List<User> search(String q);

    /** Active/Désactive un utilisateur. */
    void toggleActive(int id);

    /** Bascule le statut blacklist. */
    void toggleBlacklist(int id);

    /** Change le rôle via "Role.findByName". */
    void changeRole(int id, String roleName);

    // Recherche paginée avec filtre actif (null=tous)
    List<User> search(String q, Boolean status, int page, int size);
    // Total pour la pagination
    long count(String q, Boolean status);

    List<User> search(String q, Boolean status, int page, int size, java.util.List<String> excludedRoles);
    long count(String q, Boolean status, java.util.List<String> excludedRoles);


}
