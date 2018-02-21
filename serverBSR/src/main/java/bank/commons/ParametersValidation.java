package bank.commons;

import javax.xml.bind.ValidationException;
import java.util.Map;

/**
 * Class validating SOAP requests params
**/
public class ParametersValidation {
    /**
     * Check presence and validity of given parameters
     * @param parametersMap parameters map: key = param name, value = param value
     * @throws ValidationException if param is missing/invalid
     */
    public static void validate(Map<String, String> parametersMap) throws ValidationException {
        String errorMessage = "";

        for (Map.Entry<String, String> parameter : parametersMap.entrySet()) {
            String key = parameter.getKey();
            String value = parameter.getValue();
            if (value == null || value.length() == 0 || value.matches(".*\\p{C}.*") ||
                    (key.contains("account") && !(value.matches("[0-9]+") && value.length() == 26)) ||
                    ((key.contains("username") || key.contains("password")) && !value.matches("^[\\p{L}\\p{P}\\d]+$"))) {
                errorMessage += key + ", ";
            }
        }

        if (errorMessage.length() > 0) {
            errorMessage = errorMessage.substring(0, 1).toUpperCase() + errorMessage.substring(1, errorMessage.length() - 2);
            int index = errorMessage.lastIndexOf(",");
            if (index != -1) {
                errorMessage = errorMessage.substring(0, index) + " and" + errorMessage.substring(index + 1);
            }
            errorMessage += " is missing or invalid";
            throw new ValidationException(errorMessage);
        }
    }

}
