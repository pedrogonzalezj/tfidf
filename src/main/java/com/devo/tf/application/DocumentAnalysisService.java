package com.devo.tf.application;

import com.devo.tf.domain.Document;

import java.io.File;
import java.util.Optional;
import java.util.Set;

public interface DocumentAnalysisService {

    Optional<Document> analyze(File file, final Set<String> terms);
}
