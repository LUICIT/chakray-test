package com.chakray.fullstack_test.security.service;

import com.chakray.fullstack_test.exception.BadRequestException;
import com.chakray.fullstack_test.exception.EncryptionException;
import com.chakray.fullstack_test.security.config.AesProperties;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class AesEncryptionService {

    private final AesProperties aesProperties;

    public AesEncryptionService(AesProperties aesProperties) {
        this.aesProperties = aesProperties;
    }

    public String encrypt(String plainText) {
        try {
            validateSecretKey();

            SecretKeySpec keySpec = new SecretKeySpec(
                    aesProperties.getSecretKey().getBytes(StandardCharsets.UTF_8),
                    aesProperties.getAlgorithm()
            );

            Cipher cipher = Cipher.getInstance(aesProperties.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception ex) {
            throw new EncryptionException("Error encrypting password");
        }
    }

    private void validateSecretKey() {

        if (aesProperties.getAlgorithm() == null || aesProperties.getAlgorithm().isBlank()) {
            throw new BadRequestException("AES algorithm configuration is required");
        }

        if (aesProperties.getSecretKey() == null || aesProperties.getSecretKey().length() != 32) {
            throw new BadRequestException("Invalid AES secret key configuration. It must contain exactly 32 characters");
        }
    }

}
