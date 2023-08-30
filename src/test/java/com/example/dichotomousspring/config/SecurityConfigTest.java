package com.example.dichotomousspring.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = true)
@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    // Multiple points of research found many cases of people being told that people
    // do not unit test the security configuration and only use MockMvc


    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test()
    {
        assertFalse(false);
    }

    @Test
    public void optionsRequestShouldGoThrough() throws Exception {
        mockMvc.perform(options("/api/keys")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void unauthenticatedRequestToKeysWithNoJwtShouldRespondUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/keys").with(anonymous()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void requestToRandomEndpointShouldRespondUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/random"))
                .andExpect(status().isUnauthorized());
    }

//    @Test
//    public void testRequestWithCorrectAudience() throws Exception {
//        System.out.println("Running test: testRequestWithCorrectAudience");
//        System.out.println("Simulated JWT Claims: aud=https://my-resource-server.example.com, iss=https://idp.example.com");
//
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/api/keys")
//                        .with(jwt().jwt((jwt) -> jwt
//                                .claim("aud", "https://my-resource-server.example.com")
//                                .claim("iss", "https://idp.example.com"))))
//                .andExpect(status().isUnauthorized()) // replace with your expected status code
//                .andDo(mvcResult -> {
//                    System.out.println("Response Status: " + mvcResult.getResponse().getStatus());
//                });
//    }




}