package com.chakray.fullstack_test.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private UUID id;
    private String email;
    private String name;
    private String phone;
    private String taxId;
    private String createdAt;
    private List<AddressResponse> addresses;

}
