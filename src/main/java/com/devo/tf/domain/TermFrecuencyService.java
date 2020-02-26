package com.devo.tf.domain;

import java.io.File;
import java.util.Optional;

public interface TermFrecuencyService {

    Optional<Term> calculate(final String term, final File document);
}
