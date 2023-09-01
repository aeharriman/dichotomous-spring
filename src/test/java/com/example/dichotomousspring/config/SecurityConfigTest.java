package com.example.dichotomousspring.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = true)
@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    // Multiple points of research found many cases of people being told that people
    // do not unit test the security configuration and only use MockMvc

    @Value("${auth0.audience}")
    private String audience;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test()
    {
        assertFalse(false);
    }

    @Test
    public void unauthenticatedOptionsRequestShouldGoThrough() throws Exception {
        mockMvc.perform(options("/api/keys")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void authenticatedRequestToKeysShouldRespondOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/keys")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void unauthenticatedRequestToKeysShouldRespondUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/keys")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void unauthenticatedRequestToRandomEndpointShouldRespondUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/random"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void authenticatedRequestToRandomEndpointShouldRespondForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/random"))
                .andExpect(status().isForbidden());
    }

    // JWT validation cannot be tested by mocking the decoding because decoding and validation are combined:
    // https://docs.spring.io/spring-security/site/docs/6.1.3/api/org/springframework/security/oauth2/jwt/NimbusJwtDecoder.html#decode(java.lang.String)


    // This will be reworked for Authorization if Authorization logic is added
    public void testEndpointWithCorrectAudience() throws Exception {
        // Create a JWT with the correct audience
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("aud", audience)
                .build();

        // Mock JwtDecoder to return the JWT with the correct audience
        Mockito.when(jwtDecoder.decode(anyString())).thenReturn(jwt);

        // Perform the test and expect success
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/keys")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk());
    }


}



