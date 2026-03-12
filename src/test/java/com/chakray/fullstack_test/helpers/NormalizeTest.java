package com.chakray.fullstack_test.helpers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Normalize Helper Tests")
class NormalizeTest {

    @Test
    @DisplayName("Should normalize taxId by trimming and converting to uppercase")
    void testNormalizeTaxIdSuccess() {
        // Arrange
        String taxId = "  aarr990101xxx  ";

        // Act
        String result = Normalize.normalizeTaxId(taxId);

        // Assert
        assertEquals("AARR990101XXX", result);
    }

    @Test
    @DisplayName("Should return null when taxId is null")
    void testNormalizeTaxIdWithNull() {
        // Act
        String result = Normalize.normalizeTaxId(null);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Should convert lowercase taxId to uppercase")
    void testNormalizeTaxIdLowercase() {
        // Act
        String result = Normalize.normalizeTaxId("aarr990101xxx");

        // Assert
        assertEquals("AARR990101XXX", result);
    }

    @Test
    @DisplayName("Should trim whitespace from taxId")
    void testNormalizeTaxIdTrimWhitespace() {
        // Act
        String result = Normalize.normalizeTaxId("   AARR990101XXX   ");

        // Assert
        assertEquals("AARR990101XXX", result);
    }

    @Test
    @DisplayName("Should handle mixed case taxId")
    void testNormalizeTaxIdMixedCase() {
        // Act
        String result = Normalize.normalizeTaxId("AarR990101xXx");

        // Assert
        assertEquals("AARR990101XXX", result);
    }

    @Test
    @DisplayName("Should handle taxId with only whitespace")
    void testNormalizeTaxIdOnlyWhitespace() {
        // Act
        String result = Normalize.normalizeTaxId("   ");

        // Assert
        assertEquals("", result);
    }

}

