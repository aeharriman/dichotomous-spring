package com.example.dichotomousspring;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import static java.util.Arrays.stream;

@ConfigurationPropertiesScan
@SpringBootApplication
public class DichotomousSpringApplication {

    enum DotEnv {
        PORT,
        CLIENT_ORIGIN_URL,
        AUTH0_DOMAIN,
        AUTH0_AUDIENCE
    }


    public static void main(String[] args) {
            final var dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();
            dotEnvSafeCheck(dotenv);
            SpringApplication.run(DichotomousSpringApplication.class, args);
    }

    static void dotEnvSafeCheck(Dotenv dotenv) {
        stream(DotEnv.values())
                .map(DotEnv::name)
                .filter(varName -> dotenv.get(varName, "").isEmpty())
                .findFirst()
                .ifPresent(varName -> {
                    System.exit(1);
                });
    }

}
