package com.devo.tf.domain;

import com.devo.tf.infrastructure.FileReader;
import java.io.File;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DocumentAnalysisServiceImpl implements DocumentAnalysisService {

    private Pattern lettersPattern;
    private FileReader fileReader;

    public DocumentAnalysisServiceImpl(FileReader fileReader) {
        this.fileReader = fileReader;
        lettersPattern  = Pattern.compile("[^\\w]");
    }

    public Optional<Document> analyze(File file, final Set<String> terms) {
        if (file.isFile() && file.canRead()) {
            final Stream<String> lines  = fileReader.read(file);
            final Map<String,Integer> words = lines
                    .parallel()
                    .flatMap(l -> Stream.of(l.split(" ")))
                    .filter(w -> !"".equals(w.trim()))
                    .map(w -> lettersPattern.matcher(w.toLowerCase()).replaceAll(""))
                    .collect(Collectors.toMap(Function.identity(),(u) -> 1, Integer::sum));
            final Document.DocumentBuilder builder = new Document.DocumentBuilder().withFile(file);
            final long count = words.values().parallelStream().mapToInt(Integer::intValue).sum();
            for(String term : terms) {
                calculate(term.toLowerCase(), words, count).ifPresent(builder::addTerm);
            }
            return Optional.of(builder.build());
        }
        return Optional.empty();
    }

    private Optional<Term> calculate(final String tt, final Map<String,Integer> words, final long docWords) {
        long termCount = words.getOrDefault(tt,0);
        if (termCount == 0) {
            return Optional.empty();
        }
        final Term term = new Term(tt, BigDecimal.valueOf(termCount).divide(BigDecimal.valueOf(docWords),Term.MC));
        return Optional.of(term);
    }
}
