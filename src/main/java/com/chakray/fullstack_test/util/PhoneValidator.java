package com.chakray.fullstack_test.util;

public final class PhoneValidator {

    private PhoneValidator() {
    }

    /**
     * Validates a phone number based on a simplified rule set.
     *
     * <p>The validation process works as follows:
     * <ul>
     *     <li>If the value is {@code null} or blank, it is considered invalid.</li>
     *     <li>All non-numeric characters are removed from the input.</li>
     *     <li>If the resulting number has exactly 10 digits, it is considered valid.</li>
     *     <li>If the number has more than 10 digits, the validation checks whether
     *     the last 10 digits represent a valid phone number.</li>
     * </ul>
     *
     * <p>This approach allows accepting phone numbers with country codes or
     * formatting characters such as spaces, parentheses, or dashes.</p>
     *
     * <p>Examples of accepted formats:</p>
     * <ul>
     *     <li>5512345678</li>
     *     <li>+52 55 1234 5678</li>
     *     <li>(55) 1234-5678</li>
     * </ul>
     *
     * @param phone the phone number to validate
     * @return {@code true} if the phone number is valid according to the rules;
     *         {@code false} otherwise
     */
    public static boolean isValid(String phone) {
        if (phone == null || phone.isBlank()) {
            return false;
        }

        String normalized = phone.replaceAll("\\D", "");

        if (normalized.length() == 10) {
            return true;
        }

        return normalized.length() > 10 && normalized.endsWith(normalized.substring(normalized.length() - 10));
    }

}
