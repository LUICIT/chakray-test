package com.chakray.fullstack_test.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DateUtil Tests")
class DateUtilTest {

    @Test
    @DisplayName("Should return current date and time in Madagascar timezone")
    void testNowInMadagascar() {
        // Act
        String result = DateUtil.nowInMadagascar();

        // Assert
        assertNotNull(result);
        assertFalse(result.isBlank());
        // Validate format dd-MM-yyyy HH:mm
        assertTrue(result.matches("\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}"));
    }

    @Test
    @DisplayName("Should return formatted string with expected pattern")
    void testNowInMadagascarFormat() {
        // Act
        String result = DateUtil.nowInMadagascar();

        // Assert
        assertNotNull(result);
        String[] parts = result.split(" ");
        assertEquals(2, parts.length);
        
        String datePart = parts[0];
        String timePart = parts[1];
        
        String[] dateParts = datePart.split("-");
        assertEquals(3, dateParts.length);
        assertEquals(2, dateParts[0].length()); // day
        assertEquals(2, dateParts[1].length()); // month
        assertEquals(4, dateParts[2].length()); // year
        
        String[] timeParts = timePart.split(":");
        assertEquals(2, timeParts.length);
    }

    @Test
    @DisplayName("Should return non-null and non-empty result")
    void testNowInMadagascarNotNull() {
        // Act
        String result = DateUtil.nowInMadagascar();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

}

