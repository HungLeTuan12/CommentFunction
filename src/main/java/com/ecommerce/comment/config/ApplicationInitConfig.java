package com.ecommerce.comment.config;

import com.ecommerce.comment.constant.Role;
import com.ecommerce.comment.constant.SensitiveWords;
import com.ecommerce.comment.constant.StaticVariable;
import com.ecommerce.comment.entity.User;
import com.ecommerce.comment.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

/**
 * This class initial user when application running
 */
@Configuration
@Slf4j
public class ApplicationInitConfig {
    // admin username
    private static final String ADMIN = "admin";
    // admin password
    private static final String PASSWORD_ADMIN = "admin1234";

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     *
     * @param userRepository: used to save user to server
     * @return new admin
     */
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if(userRepository.findByUsername(ADMIN).isEmpty()) {
                var roles = new HashSet<String>();
                roles.add(ADMIN);
                User user = new User();
                user.setUsername(ADMIN);
                user.setRoles(roles);
                user.setPassword(passwordEncoder.encode(PASSWORD_ADMIN));
                userRepository.save(user);
                log.warn("Admin initialized !!");
            }
        };
    }
}
