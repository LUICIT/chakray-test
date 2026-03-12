package com.chakray.fullstack_test.service;

import com.chakray.fullstack_test.domain.model.User;
import com.chakray.fullstack_test.domain.repository.UserRepository;
import com.chakray.fullstack_test.exception.UnauthorizedException;
import com.chakray.fullstack_test.helpers.Normalize;
import com.chakray.fullstack_test.security.service.AesEncryptionService;
import com.chakray.fullstack_test.web.dto.request.LoginRequest;
import com.chakray.fullstack_test.web.dto.response.LoginResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AesEncryptionService aesEncryptionService;

    public AuthService(UserRepository userRepository,
                       AesEncryptionService aesEncryptionService) {
        this.userRepository = userRepository;
        this.aesEncryptionService = aesEncryptionService;
    }

    public LoginResponse login(LoginRequest request) {

        String taxId = Normalize.normalizeTaxId(request.getTaxId());

        User user = userRepository.findByTaxId(taxId).orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        String encryptedPassword = aesEncryptionService.encrypt(request.getPassword());

        if (!encryptedPassword.equals(user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        return new LoginResponse(
                "Login successful",
                user.getTaxId(),
                user.getName()
        );
    }

}
