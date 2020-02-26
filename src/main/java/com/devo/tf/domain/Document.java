package com.devo.tf.domain;


import java.io.File;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class Document {

    private final File file;

    private final Set<Term> terms;

    private final BigDecimal tfIdf;

    private Document(File file, Set<Term> terms) {
        this(file,terms,null);
    }

    private Document(File file, Set<Term> terms, BigDecimal tfIdf) {
        this.file = file;
        this.terms = terms;
        this.tfIdf = tfIdf;
    }

    public int compareTo(Document o) {
        return tfIdf.compareTo(o.tfIdf);
    }

    public boolean contains(final Term term) {
        return terms.contains(term);
    }

    public String getDocumentName() {
        return file.getName();
    }

    public Document setTfIdf(BigDecimal tfIdf) {
        return new Document(file,terms,tfIdf);
    }

    public BigDecimal getTfIdf() {
        return tfIdf;
    }

    public Set<Term> getTerms() {
        return terms;
    }

    @Override
    public int hashCode() {
        return file.hashCode();
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
        final Document other = (Document)obj;
        return this.file.equals(other.file);
    }

    public static class DocumentBuilder {
        private Set<Term> terms;
        private File file;

        public DocumentBuilder() {
            terms = new HashSet<>();
        }

        public DocumentBuilder withFile(File file) {
            this.file = file;
            return this;
        }

        public DocumentBuilder addTerm(Term term) {
            terms.add(term);
            return this;
        }

        public Document build() {
            return new Document(file,terms);
        }
    }
}
