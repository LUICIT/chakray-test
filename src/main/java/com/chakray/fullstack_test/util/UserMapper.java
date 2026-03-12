package com.chakray.fullstack_test.util;

import com.chakray.fullstack_test.domain.model.Address;
import com.chakray.fullstack_test.domain.model.User;
import com.chakray.fullstack_test.web.dto.request.AddressRequest;
import com.chakray.fullstack_test.web.dto.request.UserCreateRequest;
import com.chakray.fullstack_test.web.dto.response.AddressResponse;
import com.chakray.fullstack_test.web.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

public final class UserMapper {

    private UserMapper() {
    }

    public static User toUser(UserCreateRequest request, String encryptedPassword) {
        return User.builder()
                .id(UUID.randomUUID())
                .email(request.getEmail())
                .name(request.getName())
                .phone(request.getPhone())
                .password(encryptedPassword)
                .taxId(request.getTaxId().toUpperCase())
                .createdAt(DateUtil.nowInMadagascar())
                .addresses(toAddressList(request.getAddresses()))
                .build();
    }

    public static List<Address> toAddressList(List<AddressRequest> addresses) {
        return addresses.stream()
                .map(address -> Address.builder()
                        .id(address.getId())
                        .name(address.getName())
                        .street(address.getStreet())
                        .countryCode(address.getCountryCode())
                        .build())
                .toList();
    }

    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .taxId(user.getTaxId())
                .createdAt(user.getCreatedAt())
                .addresses(
                        user.getAddresses().stream()
                                .map(address -> AddressResponse.builder()
                                        .id(address.getId())
                                        .name(address.getName())
                                        .street(address.getStreet())
                                        .countryCode(address.getCountryCode())
                                        .build())
                                .toList()
                )
                .build();
    }

}
