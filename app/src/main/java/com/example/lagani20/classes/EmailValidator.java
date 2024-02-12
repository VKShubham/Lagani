package com.example.lagani20.classes;
import java.util.regex.Pattern;

public class EmailValidator {
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    // Pattern object to compile the regex
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    // Method to check if a string is in email format
    public static boolean isValidEmail(String email) {
        return pattern.matcher(email).matches();
    }
}
