package business;

import Tools.Result;

import java.util.HashMap;
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
    public static Result<Double> parseDouble(String input, String fieldKey, String fieldLabel, Map<String,String> errors) {

        try{
            return Result.ok(Double.parseDouble(input));
        }
        catch (NumberFormatException e){
            errors.put(fieldKey, fieldLabel + " is invalid");
            return Result.fail(errors);
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
    public static Result<String> stringIsEmpty(String input, String fieldKey, String fieldLabel, Map<String,String> errors) {
        if (input == null || input.trim().isEmpty()) {
            errors.put(fieldKey,fieldLabel + " is required.");
            return Result.fail(errors);
        }else {
            return  Result.ok(input);
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
    public static Result<Boolean> parseBoolean(String input, String fieldKey, String fieldLabel, Map<String,String> errors) {
        if (input == null || (!input.equals("1") && !input.equals("0"))) {
            errors.put(fieldKey, fieldLabel + " is required.");
            return Result.fail(errors);
        }else {
            return  Result.ok("1".equals(input));
        }
    }

    /**
     * verify maxLength and minLength
     * @param input
     * @param maxLength
     * @param minLength
     * @return
     */
    public static Result<String> stringLength(String input,int minLength, int maxLength) {
        if (input != null && input.length() <= maxLength && input.length() > minLength) {
            return Result.ok(input);
        }else{
            HashMap<String, String> errors = new HashMap<>();
            errors.put("length", "la taille doit être comprise entre : " + minLength +  " et " + maxLength);
            return  Result.fail(errors);
        }
    }

    /**
     * String to Double with errors
     * @param input data form
     * @param fieldKey error name
     * @param fieldLabel label name
     * @param errors
     * @return
     */
    public static Result<Integer> stringToInteger(String input, String fieldKey, String fieldLabel, Map<String,String> errors) {

        try{
            return Result.ok(Integer.parseInt(input));
        }
        catch (NumberFormatException e){
            errors.put(fieldKey, fieldLabel + " is invalid");
            return Result.fail(errors);
        }
    }
    public static Result<Integer> parseInteger(String input,
                                               String fieldKey,
                                               String fieldLabel,
                                               Map<String,String> errors) {
        return stringToInteger(input, fieldKey, fieldLabel, errors);
    }

}
