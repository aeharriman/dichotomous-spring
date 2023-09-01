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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    // JWT validation cannot be tested by mocking the decoding because decoding and validation are combined:
    // https://docs.spring.io/spring-security/site/docs/6.1.3/api/org/springframework/security/oauth2/jwt/NimbusJwtDecoder.html#decode(java.lang.String)


    @Test
    public void unauthenticatedOptionsRequestShouldGoThrough() throws Exception {
        mockMvc.perform(options("/api/keys")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void authenticatedRequestToKeysShouldRespondOk() throws Exception {
        mockMvc.perform(get("/api/keys")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void unauthenticatedRequestToKeysShouldRespondUnauthorized() throws Exception {
        mockMvc.perform(get("/api/keys")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void unauthenticatedRequestToRandomEndpointShouldRespondUnauthorized() throws Exception {
        mockMvc.perform(get("/api/random"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void authenticatedRequestToRandomEndpointShouldRespondForbidden() throws Exception {
        mockMvc.perform(get("/api/random"))
                .andExpect(status().isForbidden());
    }

    // Cors tests
    // Note: all OPTIONS tests pass with
//                    .andExpect(header().stringValues("Vary", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"))
    // Tests for allowed methods:
    // Cors sees requests without a minimum of Access-Control-Request-Method and Origin as simple OPTIONS instead of preflight:
    // Note on simple OPTIONS requests: The header with allowed methods becomes Allow instead of Access-Control-Allow-Method
    // With none:
    @Test
    public void optionsWithout_AccessControlRequestMethod_WithoutOrigin_ShouldRespond200_WithAllowMethods() throws Exception {
        mockMvc.perform(options("/api/keys"))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", "GET,HEAD,OPTIONS"))
                .andExpect(header().doesNotExist("Access-Control-Allow-Methods"));
    }

    // With one:
    @Test
    public void optionsFor_NonGetMethod_WithoutOrigin_ShouldStillRespond200_WithAllowMethods() throws Exception {
        mockMvc.perform(options("/api/keys")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", "GET,HEAD,OPTIONS"))
                .andExpect(header().doesNotExist("Access-Control-Allow-Methods"));
    }

    // With the other:
    @Test
    public void optionsWithout_AccessControlRequestMethod_WithCorrectOrigin_ShouldRespond403() throws Exception {
        mockMvc.perform(options("/api/keys")
                        .header("Origin", "http://localhost:3000"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Allow"))
                .andExpect(header().doesNotExist("Access-Control-Allow-Methods"));
    }

    // Now for Preflight requests with both:
    // Note on preflight requests: The header with allowed methods becomes Access-Control-Allow-Method instead of Allow
    @Test
    public void preflightFor_NonGetMethod_WithCorrectOrigin_ShouldRespond403() throws Exception {
        mockMvc.perform(options("/api/keys")
                        .header("Origin", "http://localhost:3000")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Allow"))
                .andExpect(header().doesNotExist("Access-Control-Allow-Methods"));
    }

    @Test
    public void preflightFor_GetMethod_WithCorrectOrigin_ShouldRespond200() throws Exception {
        mockMvc.perform(options("/api/keys")
                        .header("Access-Control-Request-Method", "GET")
                        .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Methods", "GET"))
                .andExpect(header().doesNotExist("Allow"));
    }


    // tests for origin
    // simple options tests
    // Note: Simple options tests do not respond with Access-Control-Allow-Origin
    @Test
    public void optionsWithout_Origin_ShouldRespond200Without_AccessControlAllowOrigin() throws Exception {
        mockMvc.perform(options("/api/keys"))
                .andExpect(status().isOk())
                .andExpect(header().string("Allow", "GET,HEAD,OPTIONS"))
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }

    @Test
    public void optionsWith_WrongOrigin_ShouldRespond403() throws Exception {
        mockMvc.perform(options("/api/keys")
                        .header("Origin", "http://localhost:6666"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void optionsWith_CorrectOrigin_ButNoAccessControlAllowMethod_ShouldRespond403() throws Exception {
        mockMvc.perform(options("/api/keys")
                        .header("Origin", "http://localhost:3000"))
                .andExpect(status().isForbidden());
    }

//    Origin tests for preflight:
    @Test
    public void preflightWith_CorrectOrigin_ShouldRespond200With_AccessControlAllowOrigin() throws Exception {
        mockMvc.perform(options("/api/keys")
                        .header("Access-Control-Request-Method", "GET")
                        .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"));
    }

    @Test
    public void preflightWith_WrongOrigin_ShouldRespond403() throws Exception {
        mockMvc.perform(options("/api/keys")
                        .header("Access-Control-Request-Method", "GET")
                        .header("Origin", "http://localhost:6666"))
                .andExpect(status().isForbidden());
    }

    // Tests for headers
    // Simple Options:
    // Note on simple options requests: Does not contain Access-Control-Allow-Headers
    @Test
    public void optionsWith_AccessControlRequestHeaders_ShouldRespondWithout_AccessControlAllowHeaders() throws Exception {
        mockMvc.perform(options("/api/keys")
                        // This header lets the servlet know what to tell the browser is okay
                        .header("Access-Control-Request-Headers", "authorization"))
                .andExpect(status().isOk())
                // No ResultMatcher existed to pick out just one
                .andExpect(header().doesNotExist("Access-Control-Allow-Headers"));
    }

    @Test
    public void optionsWithout_AccessControlRequestHeaders_ShouldStillRespondWithout_AccessControlAllowHeaders() throws Exception {
        mockMvc.perform(options("/api/keys"))
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist("Access-Control-Allow-Headers"));
    }

//   headers tests with preflight configuration
    @Test
    public void preflightWithout_AccessControlRequestHeaders_ShouldRespondWithout_AccessControlAllowHeaders() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.options("/api/keys")
                        .header("Access-Control-Request-Method", "GET")
                        .header("Origin", "http://localhost:3000"))
                .andExpect(header().doesNotExist("Access-Control-Allow-Headers"))
                .andExpect(status().isOk());
    }

    @Test
    public void preflightWith_AccessControlRequestHeaders_ShouldRespondWithSame_AccessControlAllowHeaders() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.options("/api/keys")
                        .header("Access-Control-Request-Headers", "thisIsAHeader")
                        .header("Access-Control-Request-Method", "GET")
                        .header("Origin", "http://localhost:3000"))
                .andExpect(header().string("Access-Control-Allow-Headers", "thisIsAHeader"))
                .andExpect(status().isOk());
    }








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



