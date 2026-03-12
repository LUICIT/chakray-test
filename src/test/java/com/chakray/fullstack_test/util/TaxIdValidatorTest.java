package com.chakray.fullstack_test.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TaxIdValidator Tests")
class TaxIdValidatorTest {

    @Test
    @DisplayName("Should return false when taxId is null")
    void testIsValidWithNullTaxId() {
        assertFalse(TaxIdValidator.isValid(null));
    }

    @Test
    @DisplayName("Should return false when taxId is blank")
    void testIsValidWithBlankTaxId() {
        assertFalse(TaxIdValidator.isValid(""));
        assertFalse(TaxIdValidator.isValid("   "));
    }

    @Test
    @DisplayName("Should return true with valid RFC format")
    void testIsValidWithValidRFC() {
        assertTrue(TaxIdValidator.isValid("AARR990101XXX"));
    }

    @Test
    @DisplayName("Should return true with valid RFC format in lowercase")
    void testIsValidWithValidRFCInLowercase() {
        assertTrue(TaxIdValidator.isValid("aarr990101xxx"));
    }

    @Test
    @DisplayName("Should return true with valid RFC format with mixed case")
    void testIsValidWithValidRFCInMixedCase() {
        assertTrue(TaxIdValidator.isValid("AarR990101xXx"));
    }

    @Test
    @DisplayName("Should return true with valid RFC format with whitespace")
    void testIsValidWithValidRFCWithWhitespace() {
        assertTrue(TaxIdValidator.isValid("  AARR990101XXX  "));
    }

    @Test
    @DisplayName("Should return false when taxId has less than 4 initial letters")
    void testIsValidWithLessThan4InitialLetters() {
        assertFalse(TaxIdValidator.isValid("AAR990101XXX"));
        assertFalse(TaxIdValidator.isValid("AA990101XXX"));
    }

    @Test
    @DisplayName("Should return false when taxId has more than 4 initial letters")
    void testIsValidWithMoreThan4InitialLetters() {
        assertFalse(TaxIdValidator.isValid("AAARR990101XXX"));
    }

    @Test
    @DisplayName("Should return false when taxId has less than 6 digits in date")
    void testIsValidWithLessThan6DateDigits() {
        assertFalse(TaxIdValidator.isValid("AARR99010XXX"));
        assertFalse(TaxIdValidator.isValid("AARR9901XXX"));
    }

    @Test
    @DisplayName("Should return false when taxId has more than 6 digits in date")
    void testIsValidWithMoreThan6DateDigits() {
        assertFalse(TaxIdValidator.isValid("AARR99010101XXX"));
    }

    @Test
    @DisplayName("Should return false when taxId has less than 3 final alphanumeric characters")
    void testIsValidWithLessThan3FinalCharacters() {
        assertFalse(TaxIdValidator.isValid("AARR990101XX"));
        assertFalse(TaxIdValidator.isValid("AARR990101X"));
    }

    @Test
    @DisplayName("Should return false when taxId has more than 3 final alphanumeric characters")
    void testIsValidWithMoreThan3FinalCharacters() {
        assertFalse(TaxIdValidator.isValid("AARR990101XXXX"));
    }

    @Test
    @DisplayName("Should return false when taxId has special characters")
    void testIsValidWithSpecialCharacters() {
        assertFalse(TaxIdValidator.isValid("AARR990101@XX"));
        assertFalse(TaxIdValidator.isValid("AARR990101-XX#"));
    }

    @Test
    @DisplayName("Should return false with only letters")
    void testIsValidWithOnlyLetters() {
        assertFalse(TaxIdValidator.isValid("AARR99XXXXXX"));
    }

    @Test
    @DisplayName("Should return false with only numbers")
    void testIsValidWithOnlyNumbers() {
        assertFalse(TaxIdValidator.isValid("123456789012"));
    }

    @ParameterizedTest
    @DisplayName("Should return true with multiple valid RFC formats")
    @ValueSource(strings = {"ABCD123456ABC", "WXYZ654321ZYX", "TEST000000000"})
    void testIsValidWithMultipleValidFormats(String taxId) {
        assertTrue(TaxIdValidator.isValid(taxId));
    }

    @ParameterizedTest
    @DisplayName("Should return false with multiple invalid RFC formats")
    @ValueSource(strings = {"123ABCD456789", "AB12345678901", "ABCD1234567", "ABCD12345678901"})
    void testIsValidWithMultipleInvalidFormats(String taxId) {
        assertFalse(TaxIdValidator.isValid(taxId));
    }

}

