package com.example.dichotomousspring.key;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeyServiceTest {

    @InjectMocks
    private KeyService keyServiceUnderTest;

    @Mock
    private KeyRepository mockKeyRepository;

    // Unit tests
    @Test
    void initShouldCallSaveAllWhenRepositoryIsEmpty() {
        when(mockKeyRepository.count()).thenReturn(0L);

        keyServiceUnderTest.init();

        verify(mockKeyRepository, times(1)).saveAll(anyList());
    }

    @Test
    void initShouldNotSaveAnythingWhenRepositoryIsNotEmpty() {
        when(mockKeyRepository.count()).thenReturn(1L);

        keyServiceUnderTest.init();

        verify(mockKeyRepository, times(0)).saveAll(anyList());
    }


    // See Repository test for initShouldSaveTheSpecificObjectsFromDefaultKeys()

    @Test
    public void findAllShouldCallReposFindAllAndReturnListOfKeys() {
        Key key1 = new Key();
        Key key2 = new Key();
        List<Key> expectedKeys = Arrays.asList(key1, key2);

        when(mockKeyRepository.findAll()).thenReturn(expectedKeys);

        List<Key> actualKeys = keyServiceUnderTest.findAll();

        assertEquals(expectedKeys, actualKeys);
        verify(mockKeyRepository).findAll();
    }


}