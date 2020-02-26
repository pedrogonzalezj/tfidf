package com.devo.tf.domain;

import com.devo.tf.infrastructure.FileReader;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TermFrecuencyServiceImpl implements TermFrecuencyService {

    private Pattern lettersPattern;

    private FileReader fileReader;


    public TermFrecuencyServiceImpl(FileReader fileReader) {
        this.fileReader = fileReader;
        lettersPattern  = Pattern.compile("[^\\w]");
    }

    public Optional<Term> calculate(final String tt, final File document) {
        Stream<String> lines = fileReader.read(document);
        final List<String> wordList = lines.parallel().flatMap(l -> Stream.of(l.split(" "))).filter(w -> !"".equals(w.trim())).map(w -> lettersPattern.matcher(w.toLowerCase()).replaceAll("")).collect(Collectors.toList());
        long count = wordList.size();
        long termCount = wordList.parallelStream().filter(tt::equalsIgnoreCase).count();
        if (termCount == 0) {
            return Optional.empty();
        }
        final Term term = new Term(tt,BigDecimal.valueOf(termCount).divide(BigDecimal.valueOf(count),Term.MC));
        return Optional.of(term);
    }
}
