package com.devo.tf.infrastructure;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

public class FileReaderImpl implements FileReader {

    public Stream<String> read(final File file) {
        try {
            return Files.lines(file.toPath());
        } catch (IOException ioex) {
            return Stream.empty();
        }
    }
}
