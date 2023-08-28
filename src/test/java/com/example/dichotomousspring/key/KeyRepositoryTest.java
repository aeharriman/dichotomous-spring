package com.example.dichotomousspring.key;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class KeyRepositoryTest {

    @Autowired
    private KeyRepository keyRepositoryUnderTest;


    // Integration test
    @Test
    void initShouldSaveTheSpecificObjectsFromDefaultKeysToTheRealDB() {
        KeyService realKeyService = new KeyService(keyRepositoryUnderTest);

        assertEquals(0, keyRepositoryUnderTest.count());

        realKeyService.init();

        assertEquals(3, keyRepositoryUnderTest.count());
    }


}