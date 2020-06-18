package com.pm.attandancemanager.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ghanshyamnayma on 09/02/16.
 */
public class Validation {

    // email validation
    public static boolean isValidEmail(String email) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * @param pass:      String variable for password
     * @param minLength: this defines minimum length of password
     * @method called when Password validation requires
     */
    public static boolean isValidPassword(String pass, int minLength) {
        if (pass != null && pass.length() >= minLength) {
            return true;
        }
        return false;
    }

}
