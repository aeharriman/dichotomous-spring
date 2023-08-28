package com.example.dichotomousspring.key;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = KeyController.class)     // loads only your controller relevant beans
@AutoConfigureMockMvc                               // makes your mockmvc a bean
@ExtendWith(MockitoExtension.class)
class KeyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KeyService mockKeyService;

    @Autowired
    private KeyController keyControllerUnderTest;


    @Test
    public void findAllShouldCallService()
    {
        Key aKey = new Key();
        List<Key> fromService = List.of(aKey);

        when(mockKeyService.findAll()).thenReturn(fromService);
        keyControllerUnderTest.findAll();

        verify(mockKeyService).findAll();
    }

    @Test
    public void loginShouldReturnAResponseEntityOKWithListOfKeysInBody()
    {
        Key aKey = new Key();
        List<Key> fromService = List.of(aKey);

        when(mockKeyService.findAll()).thenReturn(fromService);

        ResponseEntity<List<Key>> findAllResponse = keyControllerUnderTest.findAll();

        assertThat(findAllResponse.getBody()).isEqualTo(fromService);
        assertThat(findAllResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}