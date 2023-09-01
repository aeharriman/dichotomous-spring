package com.example.dichotomousspring.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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

    // Cors tests
    // tests for allowed methods
    @Test
    public void optionsShouldRespond200WithAllowMethods() throws Exception {
        mockMvc.perform(options("/api/keys")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", "GET,HEAD,OPTIONS"))
                .andExpect(header().doesNotExist("Access-Control-Allow-Method"));
    }

    @Test
    public void optionsWithoutAccessControlRequestMethodShouldStillRespond200WithAllowMethods() throws Exception {
        mockMvc.perform(options("/api/keys"))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", "GET,HEAD,OPTIONS"))
                .andExpect(header().doesNotExist("Access-Control-Allow-Method"));
    }

    @Test
    public void optionsWithPreflightConfigurationShouldRespond200WithAccessControlAllowMethods() throws Exception {
        mockMvc.perform(options("/api/keys")
                        .header("Access-Control-Request-Headers", "authorization")
                        // The browser sends this for its own sake for comparison
                        .header("Access-Control-Request-Method", "GET")
                        .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Methods", "GET"))
                .andExpect(header().doesNotExist("Allow"));
    }

    // Tests for headers
    @Test
    public void optionsWithAccessControlRequestHeadersShouldRespondWithHeadersVary() throws Exception {
        mockMvc.perform(options("/api/keys")
                        .header("Access-Control-Request-Headers", "authorization"))
                .andExpect(status().isOk())
                // No ResultMatcher existed to pick out just one
                .andExpect(header().stringValues("Vary", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"))
                .andExpect(header().doesNotExist("Access-Control-Allow-Headers"));
    }


    @Test
    public void optionsWithoutAccessControlRequestHeadersShouldStillRespondWithHeadersVary() throws Exception {
        mockMvc.perform(options("/api/keys")
                        .header("Access-Control-Request-Headers", "authorization"))
                .andExpect(status().isOk())
                .andExpect(header().stringValues("Vary", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"))
                .andExpect(header().doesNotExist("Access-Control-Allow-Headers"));
    }

    @Test
    public void optionsRequestWithPreflightConfigurationShouldAccessControlAllowHeadersFromRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.options("/api/keys")
                        .header("Access-Control-Request-Headers", "authorization")
                        .header("Access-Control-Request-Method", "GET")
                        .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Headers", "authorization"));
    }

    // Add tests for origin





    // JWT validation cannot be tested by mocking the decoding because decoding and validation are combined:
    // https://docs.spring.io/spring-security/site/docs/6.1.3/api/org/springframework/security/oauth2/jwt/NimbusJwtDecoder.html#decode(java.lang.String)


    // This will be reworked for Authorization if Authorization logic is added
//    public void testEndpointWithCorrectAudience() throws Exception {
//        // Create a JWT with the correct audience
//        Jwt jwt = Jwt.withTokenValue("token")
//                .header("alg", "none")
//                .claim("aud", audience)
//                .build();
//
//        // Mock JwtDecoder to return the JWT with the correct audience
//        Mockito.when(jwtDecoder.decode(anyString())).thenReturn(jwt);
//
//        // Perform the test and expect success
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/api/keys")
//                        .header("Authorization", "Bearer token"))
//                .andExpect(status().isOk());
//    }


}



