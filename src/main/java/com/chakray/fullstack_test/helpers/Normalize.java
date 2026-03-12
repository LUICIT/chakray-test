package com.chakray.fullstack_test.helpers;

public class Normalize {

    private Normalize() {
    }

    public static String normalizeTaxId(String taxId) {
        return taxId == null ? null : taxId.trim().toUpperCase();
    }

}
