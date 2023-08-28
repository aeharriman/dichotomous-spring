package com.example.dichotomousspring.key;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeyServiceTest {

    @InjectMocks
    private KeyService keyServiceUnderTest;

    @Mock
    private KeyRepository mockKeyRepository;

    @Test
    void testInitWhenRepositoryIsEmpty() {
        when(mockKeyRepository.count()).thenReturn(0L);

        keyServiceUnderTest.init();

        verify(mockKeyRepository, times(1)).saveAll(anyList());
    }


}