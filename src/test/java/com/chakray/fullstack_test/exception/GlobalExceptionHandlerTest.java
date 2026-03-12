package com.chakray.fullstack_test.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        webRequest = mock(WebRequest.class);
    }

    // ============== ResourceNotFoundException Tests ==============

    @Test
    @DisplayName("Should handle ResourceNotFoundException with correct status")
    void testHandleResourceNotFoundException() {
        // Arrange
        ResourceNotFoundException ex = new ResourceNotFoundException("User not found");

        // Act
        GlobalExceptionHandler.ApiError response = handler.handleNotFound(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.status());
        assertEquals("User not found", response.message());
        assertNull(response.errors());
        assertNotNull(response.timestamp());
    }

    @Test
    @DisplayName("Should handle ResourceNotFoundException with empty message")
    void testHandleResourceNotFoundExceptionEmptyMessage() {
        // Arrange
        ResourceNotFoundException ex = new ResourceNotFoundException("");

        // Act
        GlobalExceptionHandler.ApiError response = handler.handleNotFound(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.status());
        assertEquals("", response.message());
    }

    @Test
    @DisplayName("Should handle ResourceNotFoundException with long message")
    void testHandleResourceNotFoundExceptionLongMessage() {
        // Arrange
        String longMessage = "x".repeat(500);
        ResourceNotFoundException ex = new ResourceNotFoundException(longMessage);

        // Act
        GlobalExceptionHandler.ApiError response = handler.handleNotFound(ex);

        // Assert
        assertNotNull(response);
        assertEquals(longMessage, response.message());
    }

    // ============== BadRequestException Tests ==============

    @Test
    @DisplayName("Should handle BadRequestException with correct status")
    void testHandleBadRequestException() {
        // Arrange
        BadRequestException ex = new BadRequestException("Invalid input");

        // Act
        GlobalExceptionHandler.ApiError response = handler.handleBadRequest(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.status());
        assertEquals("Invalid input", response.message());
        assertNull(response.errors());
    }

    @Test
    @DisplayName("Should handle BadRequestException with field validation error")
    void testHandleBadRequestExceptionFieldValidation() {
        // Arrange
        BadRequestException ex = new BadRequestException("Email format is invalid");

        // Act
        GlobalExceptionHandler.ApiError response = handler.handleBadRequest(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.status());
        assertTrue(response.message().contains("invalid"));
    }

    // ============== UnauthorizedException Tests ==============

    @Test
    @DisplayName("Should handle UnauthorizedException with correct status")
    void testHandleUnauthorizedException() {
        // Arrange
        UnauthorizedException ex = new UnauthorizedException("Invalid credentials");

        // Act
        GlobalExceptionHandler.ApiError response = handler.handleUnauthorized(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.status());
        assertEquals("Invalid credentials", response.message());
        assertNull(response.errors());
    }

    @Test
    @DisplayName("Should handle UnauthorizedException for expired token")
    void testHandleUnauthorizedExceptionExpiredToken() {
        // Arrange
        UnauthorizedException ex = new UnauthorizedException("Token has expired");

        // Act
        GlobalExceptionHandler.ApiError response = handler.handleUnauthorized(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.status());
        assertEquals("Token has expired", response.message());
    }

    // ============== MethodArgumentNotValidException Tests ==============

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with validation errors")
    void testHandleValidationException() {
        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        FieldError fieldError1 = new FieldError("User", "email", "Email is required");
        FieldError fieldError2 = new FieldError("User", "name", "Name is required");
        List<FieldError> errors = List.of(fieldError1, fieldError2);

        org.springframework.validation.BindingResult bindingResult = mock(org.springframework.validation.BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(errors);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        // Act
        ResponseEntity<GlobalExceptionHandler.ApiError> response = handler.handleValidation(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().status());
        assertEquals("Validation error", response.getBody().message());
        assertNotNull(response.getBody().errors());
        assertEquals(2, response.getBody().errors().size());
        assertEquals("Email is required", response.getBody().errors().get("email"));
        assertEquals("Name is required", response.getBody().errors().get("name"));
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with no errors")
    void testHandleValidationExceptionNoErrors() {
        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        org.springframework.validation.BindingResult bindingResult = mock(org.springframework.validation.BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(new ArrayList<>());
        when(ex.getBindingResult()).thenReturn(bindingResult);

        // Act
        ResponseEntity<GlobalExceptionHandler.ApiError> response = handler.handleValidation(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation error", response.getBody().message());
        assertEquals(0, response.getBody().errors().size());
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with multiple field errors")
    void testHandleValidationExceptionMultipleFields() {
        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        List<FieldError> errors = List.of(
                new FieldError("User", "email", "Invalid email"),
                new FieldError("User", "phone", "Invalid phone"),
                new FieldError("User", "taxId", "Invalid tax ID"),
                new FieldError("User", "password", "Password too short")
        );
        org.springframework.validation.BindingResult bindingResult = mock(org.springframework.validation.BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(errors);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        // Act
        ResponseEntity<GlobalExceptionHandler.ApiError> response = handler.handleValidation(ex);

        // Assert
        assertNotNull(response);
        assertEquals(4, response.getBody().errors().size());
        assertEquals("Invalid email", response.getBody().errors().get("email"));
        assertEquals("Invalid phone", response.getBody().errors().get("phone"));
        assertEquals("Invalid tax ID", response.getBody().errors().get("taxId"));
        assertEquals("Password too short", response.getBody().errors().get("password"));
    }

    // ============== Generic Exception Tests ==============

    @Test
    @DisplayName("Should handle generic Exception with correct status")
    void testHandleGenericException() {
        // Arrange
        Exception ex = new RuntimeException("Unexpected error");

        // Act
        GlobalExceptionHandler.ApiError response = handler.handleGeneric(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.status());
        assertEquals("An unexpected error occurred", response.message());
        assertNull(response.errors());
    }

    @Test
    @DisplayName("Should handle IOException as generic Exception")
    void testHandleIOException() {
        // Arrange
        Exception ex = new java.io.IOException("File not found");

        // Act
        GlobalExceptionHandler.ApiError response = handler.handleGeneric(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.status());
    }

    @Test
    @DisplayName("Should handle NullPointerException as generic Exception")
    void testHandleNullPointerException() {
        // Arrange
        Exception ex = new NullPointerException("Null value encountered");

        // Act
        GlobalExceptionHandler.ApiError response = handler.handleGeneric(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.status());
    }

    // ============== ApiError Record Tests ==============

    @Test
    @DisplayName("Should create ApiError with all fields")
    void testApiErrorRecord() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.now();
        int status = 400;
        String message = "Test message";
        java.util.Map<String, String> errors = new java.util.HashMap<>();
        errors.put("field", "error");

        // Act
        GlobalExceptionHandler.ApiError apiError = new GlobalExceptionHandler.ApiError(
                timestamp, status, message, errors
        );

        // Assert
        assertNotNull(apiError);
        assertEquals(timestamp, apiError.timestamp());
        assertEquals(status, apiError.status());
        assertEquals(message, apiError.message());
        assertEquals(errors, apiError.errors());
    }

    @Test
    @DisplayName("Should create ApiError with null errors")
    void testApiErrorRecordNullErrors() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.now();

        // Act
        GlobalExceptionHandler.ApiError apiError = new GlobalExceptionHandler.ApiError(
                timestamp, 404, "Not found", null
        );

        // Assert
        assertNull(apiError.errors());
        assertEquals("Not found", apiError.message());
    }

    // ============== Timestamp Tests ==============

    @Test
    @DisplayName("Should include timestamp in ApiError")
    void testApiErrorIncludesTimestamp() {
        // Arrange
        BadRequestException ex = new BadRequestException("Test error");
        LocalDateTime beforeExecution = LocalDateTime.now();

        // Act
        GlobalExceptionHandler.ApiError response = handler.handleBadRequest(ex);
        LocalDateTime afterExecution = LocalDateTime.now();

        // Assert
        assertNotNull(response.timestamp());
        assertTrue(response.timestamp().isAfter(beforeExecution.minusSeconds(1)));
        assertTrue(response.timestamp().isBefore(afterExecution.plusSeconds(1)));
    }

    // ============== Status Codes Tests ==============

    @Test
    @DisplayName("Should return correct status codes for different exceptions")
    void testCorrectStatusCodes() {
        // Arrange
        ResourceNotFoundException notFoundEx = new ResourceNotFoundException("Not found");
        BadRequestException badRequestEx = new BadRequestException("Bad request");
        UnauthorizedException unauthorizedEx = new UnauthorizedException("Unauthorized");

        // Act
        GlobalExceptionHandler.ApiError notFoundResponse = handler.handleNotFound(notFoundEx);
        GlobalExceptionHandler.ApiError badRequestResponse = handler.handleBadRequest(badRequestEx);
        GlobalExceptionHandler.ApiError unauthorizedResponse = handler.handleUnauthorized(unauthorizedEx);

        // Assert
        assertEquals(404, notFoundResponse.status());
        assertEquals(400, badRequestResponse.status());
        assertEquals(401, unauthorizedResponse.status());
    }

}

