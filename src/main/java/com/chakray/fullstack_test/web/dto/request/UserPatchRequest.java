package com.chakray.fullstack_test.web.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPatchRequest {

    @Email(message = "Email format is invalid")
    private String email;

    private String name;
    private String phone;
    private String password;
    private String taxId;

    @Valid
    private List<AddressRequest> addresses;

}
