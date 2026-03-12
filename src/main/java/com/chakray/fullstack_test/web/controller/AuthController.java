package com.chakray.fullstack_test.web.controller;

import com.chakray.fullstack_test.service.AuthService;
import com.chakray.fullstack_test.web.dto.request.LoginRequest;
import com.chakray.fullstack_test.web.dto.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Authentication", description = "API REST para autenticación de usuarios")
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * {@code POST /login} : Authenticate a user.
     *
     * @param loginRequest the login request payload.
     * @return the login result with authentication confirmation data.
     */
    @Operation(
            summary = "Login de usuario",
            description = "Autentica por taxId y password",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Autenticación exitosa",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponse.class)
                            )),
                    @ApiResponse(responseCode = "401", description = "Credenciales inválidas",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        LOG.info("Successful login for taxId={}", loginRequest.getTaxId());
        return ResponseEntity.ok(loginResponse);
    }

}
