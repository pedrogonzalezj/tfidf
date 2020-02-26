package com.devo.tf.domain;


import java.math.BigDecimal;
import java.util.*;

public class TermFrecuencyServiceImpl implements TermFrecuencyService {


    public TermFrecuencyServiceImpl() {}

    public Optional<Term> calculate(final String tt, final List<String> words) {
        long count = words.size();
        long termCount = words.parallelStream().filter(tt::equalsIgnoreCase).count();
        if (termCount == 0) {
            return Optional.empty();
        }
        final Term term = new Term(tt,BigDecimal.valueOf(termCount).divide(BigDecimal.valueOf(count),Term.MC));
        return Optional.of(term);
    }
}
