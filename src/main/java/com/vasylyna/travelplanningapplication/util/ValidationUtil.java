package com.vasylyna.travelplanningapplication.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {

    private static final String EMAIL_REGEX_CHECKER = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX_CHECKER);
    private static final String PASSWORD_REGEX_CHECKER = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX_CHECKER);

    public static boolean isEmailValid(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    public static boolean isPasswordStrong(String password) {
        Matcher matcher = PASSWORD_PATTERN.matcher(password);
        return matcher.matches();
    }
}
