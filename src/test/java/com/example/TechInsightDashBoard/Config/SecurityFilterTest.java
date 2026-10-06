package com.example.TechInsightDashBoard.Config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.TechInsightDashBoard.Entity.UserEntity;
import com.example.TechInsightDashBoard.Service.TokenService;
import com.example.TechInsightDashBoard.Service.UserDetailsImplementation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityFilterTest {

    @Mock TokenService tokenService;
    @Mock UserDetailsImplementation userDetailsService;
    @InjectMocks SecurityFilter filter;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void bearerTokenLoadsUserAndAuthenticatesRequest() throws Exception {
        var request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valid-token");
        var response = new MockHttpServletResponse();
        var chain = new MockFilterChain();

        UserEntity user = UserEntity.builder()
                .id(1L).username("Ana")
                .email("ana@example.com")
                .password("hash").build();

        when(tokenService.validateToken("valid-token")).thenReturn("ana@example.com");
        when(userDetailsService.loadUserByUsername("ana@example.com")).thenReturn(user);

        filter.doFilterInternal(request, response, chain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertSame(user, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        assertSame(request, chain.getRequest());
        assertEquals(200, response.getStatus());
    }

    @Test
    void invalidTokenReturnsUnauthorizedWithoutContinuingChain() throws Exception {
        var request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalid-token");
        var response = new MockHttpServletResponse();
        var chain = new MockFilterChain();
        when(tokenService.validateToken("invalid-token"))
                .thenThrow(new JWTVerificationException("bad signature"));

        filter.doFilterInternal(request, response, chain);

        assertEquals(401, response.getStatus());
        assertNull(chain.getRequest());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void missingBearerTokenContinuesAsUnauthenticatedRequest() throws Exception {
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        var chain = new MockFilterChain();

        filter.doFilterInternal(request, response, chain);

        assertSame(request, chain.getRequest());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verifyNoInteractions(tokenService, userDetailsService);
    }
}
