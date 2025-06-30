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

    /**
     * verify string is empty
     * @param input
     * @param fieldKey
     * @param fieldLabel
     * @param errors
     * @return
     */
    public static String stringIsEmpty(String input, String fieldKey, String fieldLabel, Map<String,String> errors) {
        if (input == null || input.trim().isEmpty()) {
            errors.put(fieldKey,fieldLabel + " is required.");
            return null;
        }else {
            return  input;
        }
    }

    /**
     * Parses a string input into a Boolean ("1" = true, "0" = false).
     * @param input
     * @param fieldKey
     * @param fieldLabel
     * @param errors
     * @return
     */
    public static Boolean parseBoolean(String input, String fieldKey, String fieldLabel, Map<String,String> errors) {
        if (input == null || (!input.equals("1") && !input.equals("0"))) {
            errors.put(fieldKey, fieldLabel + " est requis.");
            return null;
        }else {
            return  "1".equals(input);
        }
    }


}
