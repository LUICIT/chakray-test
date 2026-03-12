package com.chakray.fullstack_test.domain.model;

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
public class User {

    private UUID id;
    private String email;
    private String name;
    private String phone;
    private String password;
    private String taxId;
    private String createdAt;
    private List<Address> addresses;

}
