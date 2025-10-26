package interfaces;
/** service applicatif pour l'entité User,elles regroupent ce qui se trouve dans les servlet :creation,verification*/

import Tools.Result;
import entities.User;


public interface UserService {
    /**@return Result.ok(true) si aucun compte n'existe avec cet email.*/
    Result<Boolean> emailAvailable(String email);
    Result<User> create(User user);
    /**@resturn l'utilisateur par email ou null si inexistant.*/
    User findByEmail(String email);
}
