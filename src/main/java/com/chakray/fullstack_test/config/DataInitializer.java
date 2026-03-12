package com.chakray.fullstack_test.config;

import com.chakray.fullstack_test.domain.model.Address;
import com.chakray.fullstack_test.domain.model.User;
import com.chakray.fullstack_test.domain.repository.UserRepository;
import com.chakray.fullstack_test.security.service.AesEncryptionService;
import com.chakray.fullstack_test.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.UUID;

@Configuration
public class DataInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner loadInitialUsers(UserRepository userRepository,
                                       AesEncryptionService aesEncryptionService) {
        return args -> {
            if (!userRepository.findAll().isEmpty()) {
                return;
            }

            userRepository.save(
                    User.builder()
                            .id(UUID.randomUUID())
                            .email("user1@mail.com")
                            .name("User One")
                            .phone("5551234567")
                            .password(aesEncryptionService.encrypt("Password123"))
                            .taxId("AARR990101XXX")
                            .createdAt(DateUtil.nowInMadagascar())
                            .addresses(List.of(
                                    Address.builder()
                                            .id(1L)
                                            .name("Home")
                                            .street("Street 1")
                                            .countryCode("MX")
                                            .build()
                            ))
                            .build()
            );

            userRepository.save(
                    User.builder()
                            .id(UUID.randomUUID())
                            .email("user2@mail.com")
                            .name("User Two")
                            .phone("5559876543")
                            .password(aesEncryptionService.encrypt("Password123"))
                            .taxId("BADD900101ABC")
                            .createdAt(DateUtil.nowInMadagascar())
                            .addresses(List.of(
                                    Address.builder()
                                            .id(2L)
                                            .name("Office")
                                            .street("Street 2")
                                            .countryCode("MX")
                                            .build()
                            ))
                            .build()
            );

            userRepository.save(
                    User.builder()
                            .id(UUID.randomUUID())
                            .email("third@mail.com")
                            .name("Third User")
                            .phone("5554567890")
                            .password(aesEncryptionService.encrypt("Password123"))
                            .taxId("COSC850101DEF")
                            .createdAt(DateUtil.nowInMadagascar())
                            .addresses(List.of(
                                    Address.builder()
                                            .id(3L)
                                            .name("Main")
                                            .street("Street 3")
                                            .countryCode("US")
                                            .build()
                            ))
                            .build()
            );

            LOG.info("Initial users loaded: {}", userRepository.findAll().size());
        };
    }

}
