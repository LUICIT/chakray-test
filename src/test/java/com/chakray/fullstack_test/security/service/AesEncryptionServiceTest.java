package com.chakray.fullstack_test.security.service;

import com.chakray.fullstack_test.exception.BadRequestException;
import com.chakray.fullstack_test.exception.EncryptionException;
import com.chakray.fullstack_test.security.config.AesProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName("AesEncryptionService Tests")
@ExtendWith(MockitoExtension.class)
class AesEncryptionServiceTest {

    @Mock(lenient = true)
    private AesProperties aesProperties;

    @InjectMocks
    private AesEncryptionService encryptionService;

    private static final String VALID_32_CHAR_KEY = "12345678901234567890123456789012";
    private static final String VALID_ALGORITHM = "AES";

    @BeforeEach
    void setUp() {
        // No configurar mocks aquí para evitar stubbings innecesarios
        // Cada test configurará los mocks que necesite
    }
    
    private void setupValidEncryption() {
        when(aesProperties.getAlgorithm()).thenReturn(VALID_ALGORITHM);
        when(aesProperties.getSecretKey()).thenReturn(VALID_32_CHAR_KEY);
    }

    @Test
    @DisplayName("Should encrypt password successfully")
    void testEncryptSuccess() {
        // Arrange
        setupValidEncryption();

        // Act
        String encrypted = encryptionService.encrypt("myPassword");

        // Assert
        assertNotNull(encrypted);
        assertFalse(encrypted.isBlank());
        assertNotEquals("myPassword", encrypted);
        // Encrypted text should be base64 encoded
        assertTrue(encrypted.matches("[A-Za-z0-9+/=]+"));
    }

    @Test
    @DisplayName("Should encrypt different passwords to different results")
    void testEncryptDifferentPasswords() {
        // Arrange
        setupValidEncryption();

        // Act
        String encrypted1 = encryptionService.encrypt("password1");
        String encrypted2 = encryptionService.encrypt("password2");

        // Assert
        assertNotNull(encrypted1);
        assertNotNull(encrypted2);
        assertNotEquals(encrypted1, encrypted2);
    }

    @Test
    @DisplayName("Should throw exception when algorithm is null")
    void testEncryptWithNullAlgorithm() {
        // Arrange
        when(aesProperties.getAlgorithm()).thenReturn(null);

        // Act & Assert
        EncryptionException exception = assertThrows(
                EncryptionException.class,
                () -> encryptionService.encrypt("test")
        );
        assertEquals("Error encrypting password", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when algorithm is blank")
    void testEncryptWithBlankAlgorithm() {
        // Arrange
        when(aesProperties.getAlgorithm()).thenReturn("   ");

        // Act & Assert
        EncryptionException exception = assertThrows(
                EncryptionException.class,
                () -> encryptionService.encrypt("test")
        );
        assertEquals("Error encrypting password", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when secret key is null")
    void testEncryptWithNullSecretKey() {
        // Arrange
        when(aesProperties.getSecretKey()).thenReturn(null);

        // Act & Assert
        EncryptionException exception = assertThrows(
                EncryptionException.class,
                () -> encryptionService.encrypt("test")
        );
        assertEquals("Error encrypting password", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when secret key length is less than 32")
    void testEncryptWithShortSecretKey() {
        // Arrange
        when(aesProperties.getSecretKey()).thenReturn("shortkey");

        // Act & Assert
        EncryptionException exception = assertThrows(
                EncryptionException.class,
                () -> encryptionService.encrypt("test")
        );
        assertEquals("Error encrypting password", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when secret key length is more than 32")
    void testEncryptWithLongSecretKey() {
        // Arrange
        when(aesProperties.getSecretKey()).thenReturn("12345678901234567890123456789012extra");

        // Act & Assert
        EncryptionException exception = assertThrows(
                EncryptionException.class,
                () -> encryptionService.encrypt("test")
        );
        assertEquals("Error encrypting password", exception.getMessage());
    }

    @Test
    @DisplayName("Should encrypt empty string")
    void testEncryptEmptyString() {
        // Arrange
        setupValidEncryption();

        // Act
        String encrypted = encryptionService.encrypt("");

        // Assert
        assertNotNull(encrypted);
        assertFalse(encrypted.isBlank());
    }

    @Test
    @DisplayName("Should encrypt string with special characters")
    void testEncryptSpecialCharacters() {
        // Arrange
        setupValidEncryption();

        // Act
        String encrypted = encryptionService.encrypt("P@ssw0rd!#$%^&*()");

        // Assert
        assertNotNull(encrypted);
        assertFalse(encrypted.isBlank());
        assertNotEquals("P@ssw0rd!#$%^&*()", encrypted);
    }

    @Test
    @DisplayName("Should encrypt long password")
    void testEncryptLongPassword() {
        // Arrange
        setupValidEncryption();
        String longPassword = "a".repeat(1000);

        // Act
        String encrypted = encryptionService.encrypt(longPassword);

        // Assert
        assertNotNull(encrypted);
        assertFalse(encrypted.isBlank());
        assertNotEquals(longPassword, encrypted);
    }

    @Test
    @DisplayName("Should throw EncryptionException on invalid algorithm")
    void testEncryptWithInvalidAlgorithm() {
        // Arrange
        when(aesProperties.getAlgorithm()).thenReturn("INVALID_ALGO");
        when(aesProperties.getSecretKey()).thenReturn(VALID_32_CHAR_KEY);

        // Act & Assert
        EncryptionException exception = assertThrows(
                EncryptionException.class,
                () -> encryptionService.encrypt("test")
        );
        assertEquals("Error encrypting password", exception.getMessage());
    }

    @Test
    @DisplayName("Should handle unicode characters")
    void testEncryptUnicodeCharacters() {
        // Arrange
        setupValidEncryption();

        // Act
        String encrypted = encryptionService.encrypt("パスワード");

        // Assert
        assertNotNull(encrypted);
        assertFalse(encrypted.isBlank());
    }

    @Test
    @DisplayName("Should encrypt with exactly 32 character key")
    void testEncryptWithExact32CharKey() {
        // Arrange
        String exact32CharKey = "abcdefghijklmnopqrstuvwxyz123456";
        when(aesProperties.getSecretKey()).thenReturn(exact32CharKey);
        when(aesProperties.getAlgorithm()).thenReturn(VALID_ALGORITHM);

        // Act
        String encrypted = encryptionService.encrypt("test");

        // Assert
        assertNotNull(encrypted);
        assertFalse(encrypted.isBlank());
    }

}

