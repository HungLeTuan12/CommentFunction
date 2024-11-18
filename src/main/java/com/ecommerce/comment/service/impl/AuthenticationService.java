package com.ecommerce.comment.service.impl;

import com.ecommerce.comment.dto.request.AuthenticateRequest;
import com.ecommerce.comment.dto.response.AuthenticationResponse;
import com.ecommerce.comment.entity.User;
import com.ecommerce.comment.exception.AppException;
import com.ecommerce.comment.exception.UnauthorizedException;
import com.ecommerce.comment.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.StringJoiner;

@Service
// Handle all logic for authentication
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    /**
     * Sign_key is the string that has 32 characters and used for checking by using HSA512 algorithm
     */
    @NonFinal
    @Value("${security.jwt.secret}")
    protected String SIGNER_KEY;

    /**
     * Check data validation
     * @param authenticateRequest: username and password
     * @return whether password has matched
     */
    public AuthenticationResponse authenticate(AuthenticateRequest authenticateRequest) throws JOSEException {
        var user = userRepository.findByUsername(authenticateRequest.getUsername()).get();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticate =  passwordEncoder.matches(authenticateRequest.getPassword(), user.getPassword());
        if(!authenticate) {
            throw new UnauthorizedException("Authenticate failed");
        }
        var token = generateToken(user);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAuthenticated(authenticate);
        authenticationResponse.setToken(token);

        return authenticationResponse;
    }

    /**
     * Generate new token
     * @param user : user information
     * @return new jwt
     */
    private String generateToken(User user) throws JOSEException {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("tuanhung")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("scope", buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
        return jwsObject.serialize();
    }

    /**
     * Các scope in oauth2 sẽ ngăn cách nhau bởi một dấu " "
     * scope: "ADMIN USER"
     * @param user
     * @return "ADMIN USER"
     */
    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRole())) {
            user.getRole().forEach(role -> {
                stringJoiner.add(role.getName());
                if(!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
                }
            });
        }
        return stringJoiner.toString();
    }
}
