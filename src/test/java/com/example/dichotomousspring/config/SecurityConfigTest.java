package com.example.dichotomousspring.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = true)
@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    // Multiple points of research found many cases of people being told that people
    // do not unit test the security configuration and only use MockMvc

    @Autowired
    private SecurityFilterChain securityFilterChain;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test()
    {
        assertFalse(false);
    }

    @Test
    public void unauthenticatedRequestToKeysWithNoJwtShouldRespondUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/keys").with(anonymous()))
                .andExpect(status().isUnauthorized());
    }



}