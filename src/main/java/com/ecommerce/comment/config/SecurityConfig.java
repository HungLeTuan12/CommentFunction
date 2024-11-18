package com.ecommerce.comment.config;

import com.ecommerce.comment.constant.Role;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
// Config endpoint, role, permission
public class SecurityConfig {
    private static final String ADMIN = "SCOPE_ADMIN";
    private static final String[] PUBLIC_URLS = {
            "/api/comments/**",
            "/api/users/**",
            "/auth/log-in",
    };

    private static final String[] PRIVATE_URLS = {
            "/api/v1/private/**",
            "/private/**"
    };
    // Sign key is string that includes 32 characters used for checking validation jwt
    @NonFinal
    @Value("${security.jwt.secret}")
    protected String SIGNER_KEY;

    /**
     *
     * @param csrf: bảo vệ endpoint bị tấn công
     *            authorizeHttpRequest:  config public url and private url, role
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(Customizer.withDefaults())
                .authorizeHttpRequests(requests ->
                        requests
                                .requestMatchers(HttpMethod.POST, PUBLIC_URLS).permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/log-in").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/users/**").hasAuthority(ADMIN)
                                .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
        ;
        return httpSecurity.build();
    }

    /**
     * This function decode jwt and validation jwt
     */
    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
    }

    /**
     * decode password
     * @return new password
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
