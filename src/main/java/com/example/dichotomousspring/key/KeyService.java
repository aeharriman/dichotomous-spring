package com.example.dichotomousspring.key;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class KeyService {

    private final KeyRepository repository;

    // Spring will look for a bean that matches this type and inject our KeyRepository here
    public KeyService(KeyRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        // This logic will run once after the service is constructed and its dependencies are injected
        if (repository.count() == 0) {
            this.repository.saveAll(defaultKeys());
        }
    }

    private static List<Key> defaultKeys()
    {
        Key key1 = new Key();
        key1.setName("Default 1");
        key1.setKey("""
                1.a. found in water ................................. 2

                2.a. grows in salt water ................................. seaweed
                       \s
                2.b. does not grow in salt water .............................. water-lily
                       \s
                1.b. found on land ................................ 3
                       \s
                3.a. real plant ....................... 4
                       \s
                4.a. grows more than 50 m tall .................. fir tree
                       \s
                4.b. grows less than 50 m tall ............................ 5
                       \s
                5.a. produces yellow flowers ............................... dandelion
                       \s
                5.b. does not produce yellow flowers ..........................apple tree
                       \s
                3.b. not a real plant ............................... astroturf""");

        Key key2 = new Key();
        key2.setName("Default 2");
        key2.setKey("""
                1.a. Fish has one dorsal fin ....................... 2

                1.b. Forked two dorsal fins ....................... 4

                2.a. Fish is spotted ....................... northern pike

                2.b. Fish is striped ....................... 3

                3.a. Fish has dark stripes ....................... tiger muskie

                3.b. Fish has light stripes ....................... muskellunge

                4.a. Fish does not have spots ....................... 5

                4.b. Fish has spots .......................  6

                5.a. Fish has large first dorsal fin ....................... walleye

                5.b. Fish has large second dorsal fin ....................... largemouth bass

                6.a. Fish has light spots ....................... brook trout

                6.b. Fish has dark spots ....................... 7

                7.a. Fish has more spots near or on tail ....................... cutthroat trout

                7.b. Fish does not have more spots on or near tail .......................8

                8.a. Fish has light area in middle .......................rainbow trout

                8.b. Fish does not have light area in middle ....................... brown trout""");
        Key key3 = new Key();
        key3.setName("Default 3");
        key3.setKey("""
                1.a. bird .............. 2
                1.b. fish .................. 3
                3.a. fast boi ......... flying fish
                3.b. smooth boi ............... ray
                1.c. mammal ...................... human
                2.a. classy ........................... penguin
                2.b. efficient ...................... raptor
                2.c. regular joe ...................... 4
                4.a. blue .................... blue bird
                4.b. red ......................... cardinal""");
        return List.of(key1, key2, key3);
    }

    public List<Key> findAll() {
        return repository.findAll();
    }

    public Key create(Key key) {
        return repository.save(key);
    }



}
