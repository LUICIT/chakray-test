package com.chakray.fullstack_test.util;

public final class PhoneValidator {

    private PhoneValidator() {
    }

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

    public static String normalize(String phone) {
        return phone == null ? null : phone.replaceAll("[^\\d+]", "");
    }

}
