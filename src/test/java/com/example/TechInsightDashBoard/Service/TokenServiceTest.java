package com.example.TechInsightDashBoard.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.TechInsightDashBoard.Entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    private static final String SECRET = "a-test-secret-that-is-long-enough-for-hmac";
    private TokenService service;

    @BeforeEach
    void setUp() {
        service = new TokenService();
        ReflectionTestUtils.setField(service, "securityKey", SECRET);
    }

    @Test
    void createsTokenWhoseSubjectIsUserEmailAndCanValidateIt() {
        UserEntity user = UserEntity.builder().id(17L).username("Ana")
                .email("ana@example.com").password("hash").build();

        String token = service.creatingToken(user);

        assertEquals("ana@example.com", service.validateToken(token));
    }

    @Test
    void rejectsTokenSignedWithDifferentSecret() {
        String token = JWT.create().withSubject("ana@example.com")
                .withExpiresAt(Instant.now().plusSeconds(60))
                .sign(Algorithm.HMAC256("a-different-test-secret-long-enough"));

        assertThrows(JWTVerificationException.class, () -> service.validateToken(token));
    }

    @Test
    void rejectsExpiredToken() {
        String token = JWT.create().withSubject("ana@example.com")
                .withExpiresAt(Instant.now().minusSeconds(60))
                .sign(Algorithm.HMAC256(SECRET));

        assertThrows(JWTVerificationException.class, () -> service.validateToken(token));
    }
}
