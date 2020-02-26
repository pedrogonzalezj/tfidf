package com.devo.tf.application;


import com.devo.tf.domain.Context;
import com.devo.tf.domain.Document;

import java.io.File;
import java.util.*;

public class DirectoryAnalysisServiceImpl implements DirectoryAnalysisService {

    private DocumentAnalysisService documentAnalysisService;

    public DirectoryAnalysisServiceImpl(DocumentAnalysisService documentAnalysisService) {
        this.documentAnalysisService = documentAnalysisService;
    }

    public void analyze(final Context context, final String folder, final Set<String> terms) {
        final File directory = new File(folder);
        if(!directory.canRead() || !directory.isDirectory()) {
            throw new BadDirectoryException();
        }
        final File[] files = directory.listFiles();
        for (final File file : files) {
            final Optional<Document> doc = documentAnalysisService.analyze(file,terms);
            doc.ifPresent(context::addDocument);
        }
    }
}
