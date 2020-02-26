package com.devo.tf.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class FileReaderImplTests {

    private File file;

    @BeforeEach
    public void setup() {
        file = new File("src/test/resources/document_1.txt");
    }

    @Test
    @DisplayName("assert that can load files")
    public void assertThatCanLoadFiles() {
        assumeTrue(file != null && file.exists() && file.isFile() && file.canRead());
        FileReader reader = new FileReaderImpl();
        Stream<String> results = reader.read(file);
        assertNotNull(results);
    }

    @Test
    @DisplayName("assert that read files by lines")
    public void assertThatReadFileByLines() {
        assumeTrue(file != null && file.exists() && file.isFile() && file.canRead());
        FileReader reader = new FileReaderImpl();
        Stream<String> results = reader.read(file);
        assertEquals(7,results.count());
    }
}
