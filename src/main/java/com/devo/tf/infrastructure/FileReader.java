package com.devo.tf.infrastructure;

import java.io.File;
import java.util.stream.Stream;

public interface FileReader {

    Stream<String> read(final File file);
}
