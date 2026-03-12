package com.chakray.fullstack_test.service;

import com.chakray.fullstack_test.domain.model.User;
import com.chakray.fullstack_test.domain.repository.UserRepository;
import com.chakray.fullstack_test.exception.UnauthorizedException;
import com.chakray.fullstack_test.security.service.AesEncryptionService;
import com.chakray.fullstack_test.web.dto.request.LoginRequest;
import com.chakray.fullstack_test.web.dto.response.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("AuthService Tests")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AesEncryptionService aesEncryptionService;

    @InjectMocks
    private AuthService authService;

    private LoginRequest loginRequest;
    private User testUser;
    private String encryptedPassword;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest("aarr990101xxx", "password123");
        testUser = User.builder()
                .id(UUID.randomUUID())
                .taxId("AARR990101XXX")
                .name("John Doe")
                .email("john@example.com")
                .password("encryptedPassword")
                .build();
        encryptedPassword = "encryptedPassword";
    }

    @Test
    @DisplayName("Should login successfully with valid credentials")
    void testLoginSuccess() {
        // Arrange
        when(userRepository.findByTaxId("AARR990101XXX")).thenReturn(Optional.of(testUser));
        when(aesEncryptionService.encrypt("password123")).thenReturn("encryptedPassword");

        // Act
        LoginResponse response = authService.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Login successful", response.getMessage());
        assertEquals("AARR990101XXX", response.getTaxId());
        assertEquals("John Doe", response.getName());
        verify(userRepository).findByTaxId("AARR990101XXX");
        verify(aesEncryptionService).encrypt("password123");
    }

    @Test
    @DisplayName("Should throw UnauthorizedException when user not found")
    void testLoginUserNotFound() {
        // Arrange
        when(userRepository.findByTaxId("AARR990101XXX")).thenReturn(Optional.empty());

        // Act & Assert
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> authService.login(loginRequest));
        assertEquals("Invalid credentials", exception.getMessage());
        verify(userRepository).findByTaxId("AARR990101XXX");
        verify(aesEncryptionService, never()).encrypt(anyString());
    }

    @Test
    @DisplayName("Should throw UnauthorizedException when password is incorrect")
    void testLoginInvalidPassword() {
        // Arrange
        when(userRepository.findByTaxId("AARR990101XXX")).thenReturn(Optional.of(testUser));
        when(aesEncryptionService.encrypt("password123")).thenReturn("wrongPassword");

        // Act & Assert
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> authService.login(loginRequest));
        assertEquals("Invalid credentials", exception.getMessage());
        verify(userRepository).findByTaxId("AARR990101XXX");
        verify(aesEncryptionService).encrypt("password123");
    }

    @Test
    @DisplayName("Should normalize taxId before querying repository")
    void testLoginNormalizeTaxId() {
        // Arrange
        LoginRequest requestWithWhitespace = new LoginRequest("  aarr990101xxx  ", "password123");
        when(userRepository.findByTaxId("AARR990101XXX")).thenReturn(Optional.of(testUser));
        when(aesEncryptionService.encrypt("password123")).thenReturn("encryptedPassword");

        // Act
        LoginResponse response = authService.login(requestWithWhitespace);

        // Assert
        assertNotNull(response);
        verify(userRepository).findByTaxId("AARR990101XXX");
    }

    @Test
    @DisplayName("Should return LoginResponse with correct user information")
    void testLoginResponseContainsCorrectData() {
        // Arrange
        when(userRepository.findByTaxId("AARR990101XXX")).thenReturn(Optional.of(testUser));
        when(aesEncryptionService.encrypt("password123")).thenReturn("encryptedPassword");

        // Act
        LoginResponse response = authService.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertTrue(response.getMessage().contains("successful"));
        assertEquals(testUser.getTaxId(), response.getTaxId());
        assertEquals(testUser.getName(), response.getName());
    }

    @Test
    @DisplayName("Should handle multiple login attempts independently")
    void testMultipleLoginAttempts() {
        // Arrange
        when(userRepository.findByTaxId("AARR990101XXX")).thenReturn(Optional.of(testUser));
        when(aesEncryptionService.encrypt("password123")).thenReturn("encryptedPassword");

        // Act
        LoginResponse response1 = authService.login(loginRequest);
        LoginResponse response2 = authService.login(loginRequest);

        // Assert
        assertNotNull(response1);
        assertNotNull(response2);
        assertEquals(response1.getTaxId(), response2.getTaxId());
        verify(userRepository, times(2)).findByTaxId("AARR990101XXX");
        verify(aesEncryptionService, times(2)).encrypt("password123");
    }

}

