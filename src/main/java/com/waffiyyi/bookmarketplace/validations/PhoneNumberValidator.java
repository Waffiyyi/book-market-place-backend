package com.waffiyyi.bookmarketplace.validations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator {
    private static final String PHONE_PATTERN = "^\\+(?:[0-9]●?){6,14}[0-9]$";

    public static boolean isValid(String phoneNumber) {
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}

