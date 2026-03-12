package com.chakray.fullstack_test.service;

import com.chakray.fullstack_test.domain.model.Address;
import com.chakray.fullstack_test.domain.model.User;
import com.chakray.fullstack_test.domain.repository.UserRepository;
import com.chakray.fullstack_test.exception.BadRequestException;
import com.chakray.fullstack_test.exception.ResourceNotFoundException;
import com.chakray.fullstack_test.security.service.AesEncryptionService;
import com.chakray.fullstack_test.web.dto.request.AddressRequest;
import com.chakray.fullstack_test.web.dto.request.UserCreateRequest;
import com.chakray.fullstack_test.web.dto.request.UserPatchRequest;
import com.chakray.fullstack_test.web.dto.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("UserService Tests")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AesEncryptionService aesEncryptionService;

    @InjectMocks
    private UserService userService;

    private UUID userId;
    private User testUser;
    private UserCreateRequest createRequest;
    private List<AddressRequest> addressRequests;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        
        addressRequests = List.of(
                new AddressRequest(1L, "Home", "123 Main St", "MX")
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
                .id(userId)
                .email("john@example.com")
                .name("John Doe")
                .phone("5512345678")
                .password("encryptedPassword")
                .taxId("AARR990101XXX")
                .createdAt("2026-03-12")
                .addresses(List.of(
                        Address.builder()
                                .id(1L)
                                .name("Home")
                                .street("123 Main St")
                                .countryCode("MX")
                                .build()
                ))
                .build();
    }

    // ============== getUsers Tests ==============

    @Test
    @DisplayName("Should get all users without filter or sort")
    void testGetUsersWithoutFilterOrSort() {
        // Arrange
        List<User> users = List.of(testUser);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserResponse> responses = userService.getUsers(null, null);

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Should get users sorted by id")
    void testGetUsersSortedById() {
        // Arrange
        User user2 = User.builder()
                .id(UUID.randomUUID())
                .name("Jane")
                .email("jane@example.com")
                .phone("5587654321")
                .password("encrypted")
                .taxId("BBBB990102YYY")
                .createdAt("12-03-2026")
                .addresses(List.of())
                .build();
        List<User> users = List.of(user2, testUser);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserResponse> responses = userService.getUsers("id", null);

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());
    }

    @Test
    @DisplayName("Should get users sorted by name")
    void testGetUsersSortedByName() {
        // Arrange
        List<User> users = List.of(testUser);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserResponse> responses = userService.getUsers("name", null);

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
    }

    @Test
    @DisplayName("Should get users sorted by email")
    void testGetUsersSortedByEmail() {
        // Arrange
        List<User> users = List.of(testUser);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserResponse> responses = userService.getUsers("email", null);

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
    }

    @Test
    @DisplayName("Should get users sorted by phone")
    void testGetUsersSortedByPhone() {
        // Arrange
        List<User> users = List.of(testUser);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserResponse> responses = userService.getUsers("phone", null);

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
    }

    @Test
    @DisplayName("Should get users sorted by tax_id")
    void testGetUsersSortedByTaxId() {
        // Arrange
        List<User> users = List.of(testUser);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserResponse> responses = userService.getUsers("tax_id", null);

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
    }

    @Test
    @DisplayName("Should get users sorted by created_at")
    void testGetUsersSortedByCreatedAt() {
        // Arrange
        List<User> users = List.of(testUser);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserResponse> responses = userService.getUsers("created_at", null);

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
    }

    @Test
    @DisplayName("Should throw BadRequestException with invalid sort field")
    void testGetUsersInvalidSortField() {
        // Arrange
        List<User> users = List.of(testUser);
        when(userRepository.findAll()).thenReturn(users);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> userService.getUsers("invalid", null));
    }

    @Test
    @DisplayName("Should filter users by contains operator")
    void testGetUsersFilterByContains() {
        // Arrange
        List<User> users = List.of(testUser);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserResponse> responses = userService.getUsers(null, "email+co+john");

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
    }

    @Test
    @DisplayName("Should filter users by equals operator")
    void testGetUsersFilterByEquals() {
        // Arrange
        List<User> users = List.of(testUser);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserResponse> responses = userService.getUsers(null, "email+eq+john@example.com");

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
    }

    @Test
    @DisplayName("Should filter users by startsWith operator")
    void testGetUsersFilterByStartsWith() {
        // Arrange
        List<User> users = List.of(testUser);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserResponse> responses = userService.getUsers(null, "name+sw+John");

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
    }

    @Test
    @DisplayName("Should filter users by endsWith operator")
    void testGetUsersFilterByEndsWith() {
        // Arrange
        List<User> users = List.of(testUser);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserResponse> responses = userService.getUsers(null, "name+ew+Doe");

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
    }

    @Test
    @DisplayName("Should throw BadRequestException with invalid filter format")
    void testGetUsersInvalidFilterFormat() {
        // Arrange
        List<User> users = List.of(testUser);
        when(userRepository.findAll()).thenReturn(users);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> userService.getUsers(null, "invalid_format"));
    }

    @Test
    @DisplayName("Should throw BadRequestException with invalid filter operator")
    void testGetUsersInvalidFilterOperator() {
        // Arrange
        List<User> users = List.of(testUser);
        when(userRepository.findAll()).thenReturn(users);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> userService.getUsers(null, "email+invalid+john"));
    }

    @Test
    @DisplayName("Should throw BadRequestException with invalid filter field")
    void testGetUsersInvalidFilterField() {
        // Arrange
        List<User> users = List.of(testUser);
        when(userRepository.findAll()).thenReturn(users);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> userService.getUsers(null, "invalid_field+co+value"));
    }

    @Test
    @DisplayName("Should return empty list when filter matches nothing")
    void testGetUsersFilterMatchesNothing() {
        // Arrange
        List<User> users = List.of(testUser);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserResponse> responses = userService.getUsers(null, "email+co+nonexistent");

        // Assert
        assertNotNull(responses);
        assertEquals(0, responses.size());
    }

    // ============== createUser Tests ==============

    @Test
    @DisplayName("Should create user successfully")
    void testCreateUserSuccess() {
        // Arrange
        when(userRepository.existsByTaxId("AARR990101XXX")).thenReturn(false);
        when(aesEncryptionService.encrypt("password123")).thenReturn("encryptedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserResponse response = userService.createUser(createRequest);

        // Assert
        assertNotNull(response);
        assertEquals("john@example.com", response.getEmail());
        assertEquals("John Doe", response.getName());
        verify(userRepository).existsByTaxId("AARR990101XXX");
        verify(aesEncryptionService).encrypt("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw BadRequestException when taxId already exists")
    void testCreateUserTaxIdAlreadyExists() {
        // Arrange
        when(userRepository.existsByTaxId("AARR990101XXX")).thenReturn(true);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> userService.createUser(createRequest));
        assertEquals("tax_id must be unique", assertThrows(BadRequestException.class, () -> userService.createUser(createRequest)).getMessage());
    }

    @Test
    @DisplayName("Should throw BadRequestException when taxId format is invalid")
    void testCreateUserInvalidTaxIdFormat() {
        // Arrange
        createRequest.setTaxId("INVALID");

        // Act & Assert
        assertThrows(BadRequestException.class, () -> userService.createUser(createRequest));
        assertEquals("Invalid tax_id format", assertThrows(BadRequestException.class, () -> userService.createUser(createRequest)).getMessage());
    }

    @Test
    @DisplayName("Should throw BadRequestException when phone format is invalid")
    void testCreateUserInvalidPhoneFormat() {
        // Arrange
        createRequest.setPhone("123");

        // Act & Assert
        assertThrows(BadRequestException.class, () -> userService.createUser(createRequest));
        assertEquals("Invalid phone format", assertThrows(BadRequestException.class, () -> userService.createUser(createRequest)).getMessage());
    }

    @Test
    @DisplayName("Should normalize taxId when creating user")
    void testCreateUserNormalizeTaxId() {
        // Arrange
        createRequest.setTaxId("  aarr990101xxx  ");
        when(userRepository.existsByTaxId("AARR990101XXX")).thenReturn(false);
        when(aesEncryptionService.encrypt("password123")).thenReturn("encryptedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserResponse response = userService.createUser(createRequest);

        // Assert
        assertNotNull(response);
        verify(userRepository).existsByTaxId("AARR990101XXX");
    }

    // ============== patchUser Tests ==============

    @Test
    @DisplayName("Should patch user email successfully")
    void testPatchUserEmail() {
        // Arrange
        UserPatchRequest patchRequest = new UserPatchRequest();
        patchRequest.setEmail("newemail@example.com");
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserResponse response = userService.patchUser(userId, patchRequest);

        // Assert
        assertNotNull(response);
        verify(userRepository).findById(userId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should patch user name successfully")
    void testPatchUserName() {
        // Arrange
        UserPatchRequest patchRequest = new UserPatchRequest();
        patchRequest.setName("Jane Doe");
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserResponse response = userService.patchUser(userId, patchRequest);

        // Assert
        assertNotNull(response);
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Should patch user phone successfully")
    void testPatchUserPhone() {
        // Arrange
        UserPatchRequest patchRequest = new UserPatchRequest();
        patchRequest.setPhone("5587654321");
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserResponse response = userService.patchUser(userId, patchRequest);

        // Assert
        assertNotNull(response);
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Should throw BadRequestException when patching with invalid phone")
    void testPatchUserInvalidPhone() {
        // Arrange
        UserPatchRequest patchRequest = new UserPatchRequest();
        patchRequest.setPhone("123");
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> userService.patchUser(userId, patchRequest));
    }

    @Test
    @DisplayName("Should patch user password successfully")
    void testPatchUserPassword() {
        // Arrange
        UserPatchRequest patchRequest = new UserPatchRequest();
        patchRequest.setPassword("newpassword");
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(aesEncryptionService.encrypt("newpassword")).thenReturn("encryptedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserResponse response = userService.patchUser(userId, patchRequest);

        // Assert
        assertNotNull(response);
        verify(aesEncryptionService).encrypt("newpassword");
    }

    @Test
    @DisplayName("Should patch user taxId successfully")
    void testPatchUserTaxId() {
        // Arrange
        UserPatchRequest patchRequest = new UserPatchRequest();
        patchRequest.setTaxId("BBBB990102YYY");
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByTaxIdAndIdNot("BBBB990102YYY", userId)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserResponse response = userService.patchUser(userId, patchRequest);

        // Assert
        assertNotNull(response);
        verify(userRepository).existsByTaxIdAndIdNot("BBBB990102YYY", userId);
    }

    @Test
    @DisplayName("Should throw BadRequestException when patching with invalid taxId format")
    void testPatchUserInvalidTaxIdFormat() {
        // Arrange
        UserPatchRequest patchRequest = new UserPatchRequest();
        patchRequest.setTaxId("INVALID");
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> userService.patchUser(userId, patchRequest));
    }

    @Test
    @DisplayName("Should throw BadRequestException when patching with duplicate taxId")
    void testPatchUserDuplicateTaxId() {
        // Arrange
        UserPatchRequest patchRequest = new UserPatchRequest();
        patchRequest.setTaxId("BBBB990102YYY");
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByTaxIdAndIdNot("BBBB990102YYY", userId)).thenReturn(true);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> userService.patchUser(userId, patchRequest));
    }

    @Test
    @DisplayName("Should patch user addresses successfully")
    void testPatchUserAddresses() {
        // Arrange
        List<AddressRequest> newAddresses = List.of(
                new AddressRequest(2L, "Work", "456 Work St", "US")
        );
        UserPatchRequest patchRequest = new UserPatchRequest();
        patchRequest.setAddresses(newAddresses);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserResponse response = userService.patchUser(userId, patchRequest);

        // Assert
        assertNotNull(response);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user not found on patch")
    void testPatchUserNotFound() {
        // Arrange
        UserPatchRequest patchRequest = new UserPatchRequest();
        patchRequest.setEmail("newemail@example.com");
        
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.patchUser(userId, patchRequest));
    }

    @Test
    @DisplayName("Should not update empty name field on patch")
    void testPatchUserEmptyNameNotUpdated() {
        // Arrange
        UserPatchRequest patchRequest = new UserPatchRequest();
        patchRequest.setName("   ");
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserResponse response = userService.patchUser(userId, patchRequest);

        // Assert
        assertNotNull(response);
    }

    @Test
    @DisplayName("Should update only provided fields on patch")
    void testPatchUserOnlyProvidedFields() {
        // Arrange
        UserPatchRequest patchRequest = new UserPatchRequest();
        patchRequest.setEmail("new@example.com");
        patchRequest.setName(null);
        patchRequest.setPhone(null);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserResponse response = userService.patchUser(userId, patchRequest);

        // Assert
        assertNotNull(response);
        verify(userRepository).save(any(User.class));
    }

    // ============== deleteUser Tests ==============

    @Test
    @DisplayName("Should delete user successfully")
    void testDeleteUserSuccess() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository).findById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent user")
    void testDeleteUserNotFound() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(userId));
    }

}

