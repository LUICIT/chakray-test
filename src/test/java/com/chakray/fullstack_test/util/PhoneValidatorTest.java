package com.chakray.fullstack_test.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PhoneValidator Tests")
class PhoneValidatorTest {

    @Test
    @DisplayName("Should return false when phone is null")
    void testIsValidWithNullPhone() {
        assertFalse(PhoneValidator.isValid(null));
    }

    @Test
    @DisplayName("Should return false when phone is blank")
    void testIsValidWithBlankPhone() {
        assertFalse(PhoneValidator.isValid(""));
        assertFalse(PhoneValidator.isValid("   "));
    }

    @Test
    @DisplayName("Should return true when phone has exactly 10 digits")
    void testIsValidWithExactly10Digits() {
        assertTrue(PhoneValidator.isValid("5512345678"));
    }

    @Test
    @DisplayName("Should return true when phone with formatting characters has exactly 10 digits")
    void testIsValidWithFormattingCharacters() {
        assertTrue(PhoneValidator.isValid("+52 55 1234 5678"));
        assertTrue(PhoneValidator.isValid("(55) 1234-5678"));
        assertTrue(PhoneValidator.isValid("+52-55-1234-5678"));
        assertTrue(PhoneValidator.isValid("55.1234.5678"));
    }

    @Test
    @DisplayName("Should return true when phone has more than 10 digits with valid last 10")
    void testIsValidWithMoreThan10Digits() {
        assertTrue(PhoneValidator.isValid("525512345678")); // 12 digits, ends with 5512345678
    }

    @Test
    @DisplayName("Should return false when phone has less than 10 digits")
    void testIsValidWithLessThan10Digits() {
        assertFalse(PhoneValidator.isValid("123456789")); // 9 digits
        assertFalse(PhoneValidator.isValid("12345"));
    }

    @Test
    @DisplayName("Should return true when phone has more than 10 digits but last 10 are valid")
    void testIsValidWithMoreThan10DigitsButValid() {
        assertTrue(PhoneValidator.isValid("12345678901")); // 11 digits, ends with 2345678901
    }

    @Test
    @DisplayName("Should ignore non-numeric characters")
    void testIsValidIgnoresNonNumericCharacters() {
        assertTrue(PhoneValidator.isValid("+52 (55) 1234-5678"));
        assertTrue(PhoneValidator.isValid("55 1234 5678"));
    }

    @Test
    @DisplayName("Should return false when phone has only letters")
    void testIsValidWithOnlyLetters() {
        assertFalse(PhoneValidator.isValid("abcdefghij"));
    }

    @Test
    @DisplayName("Should return false when phone has mixed letters and numbers but less than 10 digits")
    void testIsValidWithMixedButInsufficientDigits() {
        assertFalse(PhoneValidator.isValid("abc123def45"));
    }

}

