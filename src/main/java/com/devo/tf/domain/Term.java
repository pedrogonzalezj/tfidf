package com.devo.tf.domain;


import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Set;

public class Term {

    public static final MathContext MC = new MathContext(2, RoundingMode.HALF_UP);

    private final String value;

    private final BigDecimal frecuency;

    public Term(String value, BigDecimal frecuency) {
        this.value = value;
        this.frecuency = frecuency;
    }

    public BigDecimal getFrecuency() {
        return frecuency;
    }

    public BigDecimal tfIdf(BigDecimal idf) {
        return frecuency.multiply(idf,MC);
    }

    public BigDecimal idf(final Set<Document> documents) {
        return BigDecimal.valueOf(Math.log10((double)documents.size()/countOccurrences(documents)));
    }

    private Long countOccurrences(final Set<Document> documents) {
        return documents.stream().filter(d -> d.contains(this)).count();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Term other = (Term)obj;
        return this.value.equals(other.value);
    }
}
