package com.chakray.fullstack_test.util;

import com.chakray.fullstack_test.domain.model.Address;
import com.chakray.fullstack_test.domain.model.User;
import com.chakray.fullstack_test.web.dto.request.AddressRequest;
import com.chakray.fullstack_test.web.dto.request.UserCreateRequest;
import com.chakray.fullstack_test.web.dto.response.AddressResponse;
import com.chakray.fullstack_test.web.dto.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserMapper Tests")
class UserMapperTest {

    private UserCreateRequest createRequest;
    private List<AddressRequest> addressRequests;
    private User testUser;

    @BeforeEach
    void setUp() {
        addressRequests = List.of(
                new AddressRequest(1L, "Home", "123 Main St", "MX"),
                new AddressRequest(2L, "Work", "456 Work Ave", "US")
        );

        createRequest = new UserCreateRequest(
                "john@example.com",
                "John Doe",
                "5512345678",
                "password123",
                "AARR990101XXX",
                addressRequests
        );

        testUser = User.builder()
                .id(UUID.randomUUID())
                .email("john@example.com")
                .name("John Doe")
                .phone("5512345678")
                .password("encryptedPassword")
                .taxId("AARR990101XXX")
                .createdAt("12-03-2026 10:30")
                .addresses(List.of(
                        Address.builder()
                                .id(1L)
                                .name("Home")
                                .street("123 Main St")
                                .countryCode("MX")
                                .build(),
                        Address.builder()
                                .id(2L)
                                .name("Work")
                                .street("456 Work Ave")
                                .countryCode("US")
                                .build()
                ))
                .build();
    }

    // ============== toUser Tests ==============

    @Test
    @DisplayName("Should map UserCreateRequest to User successfully")
    void testToUserSuccess() {
        // Act
        User user = UserMapper.toUser(createRequest, "encryptedPassword");

        // Assert
        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("John Doe", user.getName());
        assertEquals("5512345678", user.getPhone());
        assertEquals("encryptedPassword", user.getPassword());
        assertEquals("AARR990101XXX", user.getTaxId());
        assertNotNull(user.getCreatedAt());
        assertEquals(2, user.getAddresses().size());
    }

    @Test
    @DisplayName("Should convert taxId to uppercase when mapping")
    void testToUserConvertsTaxIdToUppercase() {
        // Arrange
        createRequest.setTaxId("aarr990101xxx");

        // Act
        User user = UserMapper.toUser(createRequest, "encryptedPassword");

        // Assert
        assertEquals("AARR990101XXX", user.getTaxId());
    }

    @Test
    @DisplayName("Should set encrypted password correctly")
    void testToUserSetsEncryptedPassword() {
        // Act
        User user = UserMapper.toUser(createRequest, "myEncryptedPassword");

        // Assert
        assertEquals("myEncryptedPassword", user.getPassword());
    }

    @Test
    @DisplayName("Should generate unique UUID for each user")
    void testToUserGeneratesUniqueUUID() {
        // Act
        User user1 = UserMapper.toUser(createRequest, "encryptedPassword");
        User user2 = UserMapper.toUser(createRequest, "encryptedPassword");

        // Assert
        assertNotEquals(user1.getId(), user2.getId());
    }

    @Test
    @DisplayName("Should set createdAt date")
    void testToUserSetsCreatedAt() {
        // Act
        User user = UserMapper.toUser(createRequest, "encryptedPassword");

        // Assert
        assertNotNull(user.getCreatedAt());
        assertFalse(user.getCreatedAt().isBlank());
    }

    @Test
    @DisplayName("Should map addresses correctly")
    void testToUserMapsAddressesCorrectly() {
        // Act
        User user = UserMapper.toUser(createRequest, "encryptedPassword");

        // Assert
        assertEquals(2, user.getAddresses().size());
        
        Address addr1 = user.getAddresses().get(0);
        assertEquals(1L, addr1.getId());
        assertEquals("Home", addr1.getName());
        assertEquals("123 Main St", addr1.getStreet());
        assertEquals("MX", addr1.getCountryCode());

        Address addr2 = user.getAddresses().get(1);
        assertEquals(2L, addr2.getId());
        assertEquals("Work", addr2.getName());
        assertEquals("456 Work Ave", addr2.getStreet());
        assertEquals("US", addr2.getCountryCode());
    }

    @Test
    @DisplayName("Should handle single address")
    void testToUserWithSingleAddress() {
        // Arrange
        List<AddressRequest> singleAddress = List.of(
                new AddressRequest(1L, "Home", "123 Main St", "MX")
        );
        createRequest.setAddresses(singleAddress);

        // Act
        User user = UserMapper.toUser(createRequest, "encryptedPassword");

        // Assert
        assertEquals(1, user.getAddresses().size());
    }

    // ============== toAddressList Tests ==============

    @Test
    @DisplayName("Should convert AddressRequest list to Address list")
    void testToAddressListSuccess() {
        // Act
        List<Address> addresses = UserMapper.toAddressList(addressRequests);

        // Assert
        assertNotNull(addresses);
        assertEquals(2, addresses.size());
        
        Address addr1 = addresses.get(0);
        assertEquals(1L, addr1.getId());
        assertEquals("Home", addr1.getName());
        assertEquals("123 Main St", addr1.getStreet());
        assertEquals("MX", addr1.getCountryCode());
    }

    @Test
    @DisplayName("Should handle empty address list")
    void testToAddressListEmpty() {
        // Act
        List<Address> addresses = UserMapper.toAddressList(List.of());

        // Assert
        assertNotNull(addresses);
        assertEquals(0, addresses.size());
    }

    @Test
    @DisplayName("Should preserve all address properties")
    void testToAddressListPreservesProperties() {
        // Act
        List<Address> addresses = UserMapper.toAddressList(addressRequests);

        // Assert
        for (int i = 0; i < addressRequests.size(); i++) {
            AddressRequest request = addressRequests.get(i);
            Address address = addresses.get(i);
            
            assertEquals(request.getId(), address.getId());
            assertEquals(request.getName(), address.getName());
            assertEquals(request.getStreet(), address.getStreet());
            assertEquals(request.getCountryCode(), address.getCountryCode());
        }
    }

    // ============== toResponse Tests ==============

    @Test
    @DisplayName("Should map User to UserResponse successfully")
    void testToResponseSuccess() {
        // Act
        UserResponse response = UserMapper.toResponse(testUser);

        // Assert
        assertNotNull(response);
        assertEquals(testUser.getId(), response.getId());
        assertEquals("john@example.com", response.getEmail());
        assertEquals("John Doe", response.getName());
        assertEquals("5512345678", response.getPhone());
        assertEquals("AARR990101XXX", response.getTaxId());
        assertEquals("12-03-2026 10:30", response.getCreatedAt());
        assertEquals(2, response.getAddresses().size());
    }

    @Test
    @DisplayName("Should map addresses in response correctly")
    void testToResponseMapsAddresses() {
        // Act
        UserResponse response = UserMapper.toResponse(testUser);

        // Assert
        assertEquals(2, response.getAddresses().size());
        
        AddressResponse addr1 = response.getAddresses().get(0);
        assertEquals(1L, addr1.getId());
        assertEquals("Home", addr1.getName());
        assertEquals("123 Main St", addr1.getStreet());
        assertEquals("MX", addr1.getCountryCode());

        AddressResponse addr2 = response.getAddresses().get(1);
        assertEquals(2L, addr2.getId());
        assertEquals("Work", addr2.getName());
        assertEquals("456 Work Ave", addr2.getStreet());
        assertEquals("US", addr2.getCountryCode());
    }

    @Test
    @DisplayName("Should handle user with empty addresses")
    void testToResponseWithEmptyAddresses() {
        // Arrange
        User userNoAddresses = User.builder()
                .id(UUID.randomUUID())
                .email("jane@example.com")
                .name("Jane Doe")
                .phone("5587654321")
                .password("encrypted")
                .taxId("BBBB990102YYY")
                .createdAt("12-03-2026 11:00")
                .addresses(List.of())
                .build();

        // Act
        UserResponse response = UserMapper.toResponse(userNoAddresses);

        // Assert
        assertNotNull(response);
        assertEquals(0, response.getAddresses().size());
    }

    @Test
    @DisplayName("Should preserve UUID in response")
    void testToResponsePreservesUUID() {
        // Act
        UserResponse response = UserMapper.toResponse(testUser);

        // Assert
        assertEquals(testUser.getId(), response.getId());
    }

    @Test
    @DisplayName("Should preserve all user properties in response")
    void testToResponsePreservesAllProperties() {
        // Act
        UserResponse response = UserMapper.toResponse(testUser);

        // Assert
        assertEquals(testUser.getId(), response.getId());
        assertEquals(testUser.getEmail(), response.getEmail());
        assertEquals(testUser.getName(), response.getName());
        assertEquals(testUser.getPhone(), response.getPhone());
        assertEquals(testUser.getTaxId(), response.getTaxId());
        assertEquals(testUser.getCreatedAt(), response.getCreatedAt());
    }

}

