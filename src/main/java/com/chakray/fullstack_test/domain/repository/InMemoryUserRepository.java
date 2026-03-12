package com.chakray.fullstack_test.domain.repository;

import com.chakray.fullstack_test.domain.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final ConcurrentMap<UUID, User> storage = new ConcurrentHashMap<>();

    @Override
    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<User> findByTaxId(String taxId) {
        if (taxId == null || taxId.isBlank()) {
            return Optional.empty();
        }

        return storage.values().stream()
                .filter(user -> user.getTaxId() != null)
                .filter(user -> user.getTaxId().equalsIgnoreCase(taxId.trim()))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }

        return storage.values().stream()
                .filter(user -> user.getEmail() != null)
                .filter(user -> user.getEmail().equalsIgnoreCase(email.trim()))
                .findFirst();
    }

    @Override
    public boolean existsByTaxId(String taxId) {
        return findByTaxId(taxId).isPresent();
    }

    @Override
    public boolean existsByTaxIdAndIdNot(String taxId, UUID id) {
        if (taxId == null || taxId.isBlank() || id == null) {
            return false;
        }

        return storage.values().stream()
                .filter(user -> user.getTaxId() != null)
                .anyMatch(user ->
                        user.getTaxId().equalsIgnoreCase(taxId.trim())
                                && !user.getId().equals(id)
                );
    }

    @Override
    public User save(User user) {
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }

}
