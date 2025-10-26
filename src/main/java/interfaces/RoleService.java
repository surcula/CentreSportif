package interfaces;

import entities.Role;
import Tools.Result;
import java.util.List;

/**
 * gestion des roles users
 */
public interface RoleService {
    Result<List<Role>> getAll(); // liste tous les roles
    Result<Role> getOneById(int id); // recup role par id
    Result<Role> getByName(String name); // recup par nom genre admin,secretaire,...
}