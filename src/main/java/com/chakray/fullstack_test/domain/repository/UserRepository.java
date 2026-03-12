package com.chakray.fullstack_test.domain.repository;

import com.chakray.fullstack_test.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    List<User> findAll();

    Optional<User> findById(UUID id);

    Optional<User> findByTaxId(String taxId);

    Optional<User> findByEmail(String email);

    boolean existsByTaxId(String taxId);

    boolean existsByTaxIdAndIdNot(String taxId, UUID id);

    User save(User user);

    void deleteById(UUID id);

}
