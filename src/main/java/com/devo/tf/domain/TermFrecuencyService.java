package com.devo.tf.domain;


import java.util.List;
import java.util.Optional;

public interface TermFrecuencyService {

    Optional<Term> calculate(final String term, final List<String> words);
}
