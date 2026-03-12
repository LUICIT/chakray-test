package com.chakray.fullstack_test.util;

import java.util.Locale;
import java.util.regex.Pattern;

public final class TaxIdValidator {

    private static final Pattern RFC_PATTERN = Pattern.compile(
            "^[A-Z]{4}\\d{6}[A-Z\\d]{3}$"
    );

    private TaxIdValidator() {
    }

    /**
     * Validates tax_id using a simplified RFC format for this technical test.
     * <p>
     * Supported format:
     * - Natural persons only
     * - 4 initial letters
     * - 6 digits for date
     * - 3 alphanumeric characters at the end
     * <p>
     * Example: AARR990101XXX
     */
    public static boolean isValid(String taxId) {
        if (taxId == null || taxId.isBlank()) {
            return false;
        }
        return RFC_PATTERN.matcher(taxId.trim().toUpperCase(Locale.ROOT)).matches();
    }

}
