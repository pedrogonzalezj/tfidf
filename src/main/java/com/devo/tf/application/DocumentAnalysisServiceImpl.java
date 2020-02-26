package com.devo.tf.application;

import com.devo.tf.domain.Document;
import com.devo.tf.domain.TermFrecuencyService;
import com.devo.tf.infrastructure.FileReader;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DocumentAnalysisServiceImpl implements DocumentAnalysisService {

    private Pattern lettersPattern;
    private TermFrecuencyService termFrecuencyService;
    private FileReader fileReader;

    public DocumentAnalysisServiceImpl(TermFrecuencyService termFrecuencyService, FileReader fileReader) {
        this.termFrecuencyService = termFrecuencyService;
        this.fileReader = fileReader;
        lettersPattern  = Pattern.compile("[^\\w]");
    }

    public Optional<Document> analyze(File file, final Set<String> terms) {
        if (file.isFile() && file.canRead()) {
            final Stream<String> lines  = fileReader.read(file);
            final List<String> words    = lines
                    .parallel()
                    .flatMap(l -> Stream.of(l.split(" ")))
                    .filter(w -> !"".equals(w.trim()))
                    .map(w -> lettersPattern.matcher(w.toLowerCase()).replaceAll(""))
                    .collect(Collectors.toList());
            Document.DocumentBuilder builder = new Document.DocumentBuilder().withFile(file);
            for(String term : terms) {
                termFrecuencyService.calculate(term, words).ifPresent(builder::addTerm);
            }
            return Optional.of(builder.build());
        }
        return Optional.empty();
    }
}
