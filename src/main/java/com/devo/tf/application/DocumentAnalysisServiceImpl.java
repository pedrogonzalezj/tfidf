package com.devo.tf.application;

import com.devo.tf.domain.Document;
import com.devo.tf.domain.TermFrecuencyService;

import java.io.File;
import java.util.Optional;
import java.util.Set;

public class DocumentAnalysisServiceImpl implements DocumentAnalysisService {

    private TermFrecuencyService termFrecuencyService;

    public DocumentAnalysisServiceImpl(TermFrecuencyService termFrecuencyService) {
        this.termFrecuencyService = termFrecuencyService;
    }

    public Optional<Document> analyze(File file, final Set<String> terms) {
        if (file.isFile() && file.canRead()) {
            Document.DocumentBuilder builder = new Document.DocumentBuilder().withFile(file);
            for(String term : terms) {
                termFrecuencyService.calculate(term, file).ifPresent(builder::addTerm);
            }
            return Optional.of(builder.build());
        }
        return Optional.empty();
    }
}
