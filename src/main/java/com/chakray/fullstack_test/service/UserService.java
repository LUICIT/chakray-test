package com.chakray.fullstack_test.service;

import com.chakray.fullstack_test.domain.model.User;
import com.chakray.fullstack_test.domain.repository.UserRepository;
import com.chakray.fullstack_test.exception.BadRequestException;
import com.chakray.fullstack_test.exception.ResourceNotFoundException;
import com.chakray.fullstack_test.helpers.Normalize;
import com.chakray.fullstack_test.security.service.AesEncryptionService;
import com.chakray.fullstack_test.util.PhoneValidator;
import com.chakray.fullstack_test.util.TaxIdValidator;
import com.chakray.fullstack_test.util.UserMapper;
import com.chakray.fullstack_test.web.dto.request.UserCreateRequest;
import com.chakray.fullstack_test.web.dto.request.UserPatchRequest;
import com.chakray.fullstack_test.web.dto.response.UserResponse;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AesEncryptionService aesEncryptionService;

    public UserService(UserRepository userRepository,
                       AesEncryptionService aesEncryptionService) {
        this.userRepository = userRepository;
        this.aesEncryptionService = aesEncryptionService;
    }

    public List<UserResponse> getUsers(String sortedBy, String filter) {
        List<User> users = userRepository.findAll();

        if (filter != null && !filter.isBlank()) {
            users = applyFilter(users, filter);
        }

        if (sortedBy != null && !sortedBy.isBlank()) {
            users = users.stream()
                    .sorted(getComparator(sortedBy))
                    .toList();
        }

        return users.stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    public UserResponse createUser(UserCreateRequest request) {
        // Normaliza tax id (evita duplicidad por mayúsculas/espacios)
        String taxId = Normalize.normalizeTaxId(request.getTaxId());

        validateTaxId(taxId, null);
        validatePhone(request.getPhone());

        User user = UserMapper.toUser(
                request,
                aesEncryptionService.encrypt(request.getPassword())
        );
        user.setTaxId(taxId);

        return UserMapper.toResponse(userRepository.save(user));
    }

    public UserResponse patchUser(UUID id, UserPatchRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName().trim());
        }

        if (request.getPhone() != null) {
            validatePhone(request.getPhone());
            user.setPhone(request.getPhone());
        }

        if (request.getPassword() != null) {
            user.setPassword(aesEncryptionService.encrypt(request.getPassword()));
        }

        if (request.getTaxId() != null) {
            // Normaliza tax id (evita duplicidad por mayúsculas/espacios)
            String taxId = Normalize.normalizeTaxId(request.getTaxId());

            validateTaxId(taxId, id);
            user.setTaxId(taxId);

        }

        if (request.getAddresses() != null) {
            user.setAddresses(UserMapper.toAddressList(request.getAddresses()));
        }

        return UserMapper.toResponse(userRepository.save(user));
    }

    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.deleteById(user.getId());
    }

    private void validateTaxId(String taxId, UUID currentId) {
        String normalizedTaxId = Normalize.normalizeTaxId(taxId);

        if (!TaxIdValidator.isValid(normalizedTaxId)) {
            throw new BadRequestException("Invalid tax_id format");
        }

        boolean exists = currentId == null
                ? userRepository.existsByTaxId(normalizedTaxId)
                : userRepository.existsByTaxIdAndIdNot(normalizedTaxId, currentId);

        if (exists) {
            throw new BadRequestException("tax_id must be unique");
        }
    }

    private void validatePhone(String phone) {
        if (!PhoneValidator.isValid(phone)) {
            throw new BadRequestException("Invalid phone format");
        }
    }

    private Comparator<User> getComparator(String sortedBy) {
        return switch (sortedBy) {
            case "id" -> Comparator.comparing(User::getId);
            case "email" -> Comparator.comparing(User::getEmail, String.CASE_INSENSITIVE_ORDER);
            case "name" -> Comparator.comparing(User::getName, String.CASE_INSENSITIVE_ORDER);
            case "phone" -> Comparator.comparing(User::getPhone, String.CASE_INSENSITIVE_ORDER);
            case "tax_id" -> Comparator.comparing(User::getTaxId, String.CASE_INSENSITIVE_ORDER);
            case "created_at" -> Comparator.comparing(User::getCreatedAt, String.CASE_INSENSITIVE_ORDER);
            default -> throw new BadRequestException("Invalid sortedBy field");
        };
    }

    private List<User> applyFilter(List<User> users, String filter) {
        String[] parts = filter.split("\\+", 3);

        if (parts.length != 3) {
            throw new BadRequestException("Filter format must be field+operator+value");
        }

        String field = parts[0];
        String operator = parts[1];
        String value = parts[2];

        return users.stream()
                .filter(user -> matches(getFieldValue(user, field), operator, value))
                .toList();
    }

    private String getFieldValue(User user, String field) {
        return switch (field) {
            case "id" -> user.getId().toString();
            case "email" -> user.getEmail();
            case "name" -> user.getName();
            case "phone" -> user.getPhone();
            case "tax_id" -> user.getTaxId();
            case "created_at" -> user.getCreatedAt();
            default -> throw new BadRequestException("Invalid filter field");
        };
    }

    private boolean matches(String fieldValue, String operator, String value) {
        String source = fieldValue == null ? "" : fieldValue.toLowerCase(Locale.ROOT);
        String target = value.toLowerCase(Locale.ROOT);

        return switch (operator) {
            case "co" -> source.contains(target);
            case "eq" -> source.equals(target);
            case "sw" -> source.startsWith(target);
            case "ew" -> source.endsWith(target);
            default -> throw new BadRequestException("Invalid filter operator");
        };
    }

}
