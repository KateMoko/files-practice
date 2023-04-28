package com.katemoko.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.katemoko.pojos.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParsingJsonTest {

    private ClassLoader cl = ParsingJsonTest.class.getClassLoader();
    List<String> mainCharacters = Arrays.asList("Bilbo Baggins", "Gandalf", "Thorin Oakenshield", "Smaug");

    @Test
    @DisplayName("Проверка JSON")
    void parseBookDataFromJsonTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream is = cl.getResourceAsStream("book.json");
             InputStreamReader reader = new InputStreamReader(is)) {
            Book book = objectMapper.readValue(reader, Book.class);
            assertEquals("John Ronald Reuel Tolkien", book.getAuthor());
            assertEquals("The Hobbit, or There and Back Again", book.getTitle());
            assertEquals("Children's fantasy", book.getGenre());
            assertEquals(mainCharacters, book.getMainCharacters());
            assertEquals(1937, book.getEditions().get(0).getPublishedYear());
            assertEquals("George Allen &Unwin Ltd", book.getEditions().get(0).getPublisher());
            assertEquals(1500, book.getEditions().get(0).getNumberOfCopies());
            assertEquals(1937, book.getEditions().get(1).getPublishedYear());
            assertEquals("George Allen &Unwin Ltd", book.getEditions().get(1).getPublisher());
            assertEquals(2300, book.getEditions().get(1).getNumberOfCopies());
            assertEquals(1938, book.getEditions().get(2).getPublishedYear());
            assertEquals("Houghton Mifflin Company", book.getEditions().get(2).getPublisher());
            assertEquals(5000, book.getEditions().get(2).getNumberOfCopies());
        }
    }
}
