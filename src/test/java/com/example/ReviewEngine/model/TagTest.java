package com.example.ReviewEngine.model;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


class TagTest {

    @Test
    void constructor_andToString(){
        Tag tag = new Tag("tag1");
        assertEquals("tag1", tag.getName());
        assertTrue(tag.getProducts().isEmpty());

        String string = tag.toString();
        assertTrue(string.contains("name='tag1'"));
    }
}