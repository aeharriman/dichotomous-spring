package com.example.dichotomousspring.key;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = KeyController.class)     // loads only your controller relevant beans
@AutoConfigureMockMvc(addFilters = false)                               // makes your mockmvc a bean
@ExtendWith(MockitoExtension.class)
class KeyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KeyService mockKeyService;

    @Autowired
    private KeyController keyControllerUnderTest;


    // Unit tests

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

    // Integration tests

    @Test
    public void findAllEndpointShouldRespondOKWithListOfKeys() throws Exception {
        Key key1 = new Key();
        key1.setId(1L);
        key1.setName("Name1");
        key1.setKey("Key1");

        Key key2 = new Key();
        key2.setId(2L);
        key2.setName("Name2");
        key2.setKey("Key2");

        List<Key> keys = Arrays.asList(key1, key2);
        when(mockKeyService.findAll()).thenReturn(keys);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/keys")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("Name1", "Key1", "Name2", "Key2");
    }

}