package business;

import java.util.Map;

public class ValidateForm {

    /**
     * String to Double with errors
     * @param input data form
     * @param fieldKey error name
     * @param fieldLabel label name
     * @param errors
     * @return
     */
    public static Double parseDouble(String input, String fieldKey, String fieldLabel, Map<String,String> errors) {

        try{
            return Double.parseDouble(input);
        }
        catch (NumberFormatException e){
            errors.put(fieldKey, fieldLabel + "is invalid");
            return null;
        }
    }
}
