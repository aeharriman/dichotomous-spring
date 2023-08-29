package com.example.dichotomousspring.key;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    void findAllShouldRetrieveAllKeys() {
        Key savedKey1 = keyRepositoryUnderTest.save(new Key());
        Key savedKey2 = keyRepositoryUnderTest.save(new Key());
        Key savedKey3 = keyRepositoryUnderTest.save(new Key());

        List<Key> keys = keyRepositoryUnderTest.findAll();

        assertThat(keys).contains(savedKey1, savedKey2, savedKey3);
    }

    @Test
    void findAllShouldReturnEmptyListIfNothingInDatabase() {
        List<Key> keys = keyRepositoryUnderTest.findAll();
        assertThat(keys).isEmpty();
    }


}