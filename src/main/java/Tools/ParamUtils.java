package Tools;

import java.util.HashMap;
import java.util.Map;

public class ParamUtils {

    /**
     * verify id STRING to INTEGER
     * @param idString
     * @return id(Integer)
     */
    public static Result<Integer> verifyId(String idString){
        Result<Integer> idResult = stringToInteger(idString);
        // On retourne directement l'erreur
        if(!idResult.isSuccess()){
            return idResult;
        }
        //On va vérifier si l'id est positif
        if(idResult.getData() > 0 ){
            return idResult;
        }else{
            Map<String, String> errors = new HashMap<>();
            errors.put("id", "Impossible de trouver un id négatif");
            return Result.fail(errors);
        }
    }

    /**
     * String To Integer
     * @param input
     * @return
     */
    public static Result<Integer> stringToInteger(String input) {
        try{
            return Result.ok(Integer.parseInt(input));
        }catch (NumberFormatException e) {
            Map<String, String> errors = new HashMap<>();
            errors.put("parseInt", "Impossible de convertir '" + input + "' en nombre.");
            return Result.fail(errors);
        }
    }

}
