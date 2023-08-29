package com.example.dichotomousspring;

import com.github.stefanbirkner.systemlambda.SystemLambda;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.WHEN_AVAILABLE)
class DichotomousSpringApplicationTests {

    @Mock
    Dotenv dotenv;

    @Test
    void contextLoads()
    {
    }

    @Test
    public void dotEnvSafeCheckShouldExitApplicationWithA1WhenEnvVariableMissing() throws Exception
    {
        Dotenv dotenvMock = mock(Dotenv.class);

        when(dotenvMock.get(anyString(), anyString())).thenReturn("");

        int exitCode = SystemLambda.catchSystemExit(() -> {
            DichotomousSpringApplication.dotEnvSafeCheck(dotenvMock);
        });

        assertEquals(1, exitCode);
    }


}
