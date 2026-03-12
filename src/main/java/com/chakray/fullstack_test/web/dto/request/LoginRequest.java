package com.chakray.fullstack_test.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Tax ID is required")
    private String taxId;

    @NotBlank(message = "Password is required")
    private String password;

}
