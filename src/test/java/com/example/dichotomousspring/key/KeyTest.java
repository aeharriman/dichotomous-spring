package com.example.dichotomousspring.key;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class KeyTest {

    private Key keyUnderTest;

    @Test
    public void shouldSerializeWithMultilineStrings() throws JsonProcessingException
    {
        this.keyUnderTest = new Key();
        keyUnderTest.setId(1L);
        keyUnderTest.setName("Tab name");
        keyUnderTest.setKey("""
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


        String expected = "{\"name\":\"Tab name\",\"key\":\"1.a. Fish has one dorsal fin ....................... 2" +
                "\\n\\n1.b. Forked two dorsal fins ....................... 4\\n\\n2.a." +
                " Fish is spotted ....................... northern pike\\n\\n2.b." +
                " Fish is striped ....................... 3\\n\\n3.a." +
                " Fish has dark stripes ....................... tiger muskie\\n\\n3.b." +
                " Fish has light stripes ....................... muskellunge\\n\\n4.a." +
                " Fish does not have spots ....................... 5\\n\\n4.b." +
                " Fish has spots .......................  6\\n\\n5.a." +
                " Fish has large first dorsal fin ....................... walleye\\n\\n5.b. " +
                "Fish has large second dorsal fin ....................... largemouth bass\\n\\n6.a." +
                " Fish has light spots ....................... brook trout\\n\\n6.b. " +
                "Fish has dark spots ....................... 7\\n\\n7.a." +
                " Fish has more spots near or on tail ....................... cutthroat trout\\n\\n7.b. " +
                "Fish does not have more spots on or near tail .......................8\\n\\n8.a. " +
                "Fish has light area in middle .......................rainbow trout\\n\\n8.b. " +
                "Fish does not have light area in middle ....................... brown trout\"}";

        ObjectMapper mapper = new ObjectMapper();
        String actual = mapper.writeValueAsString(keyUnderTest);

        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void shouldSerializeWithoutId() throws JsonProcessingException
    {
        this.keyUnderTest = new Key();
        keyUnderTest.setId(1L);
        keyUnderTest.setName("Tab name");
        keyUnderTest.setKey("This is a key");

        ObjectMapper mapper = new ObjectMapper();
        String actual = mapper.writeValueAsString(keyUnderTest);

        assertThat(actual).doesNotContain(keyUnderTest.getId().toString());
    }

    // No POST methods yet, so not testing deserialization


}