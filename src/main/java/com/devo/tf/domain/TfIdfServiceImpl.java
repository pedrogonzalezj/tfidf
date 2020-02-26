package com.devo.tf.domain;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class TfIdfServiceImpl implements TfIdfService {

    public Set<Document> report(final Set<Document> documents) {
        final Set<Document> results = new HashSet<>(documents.size());
        for(Document doc : documents) {
            BigDecimal res = BigDecimal.ZERO;
            for(Term term : doc.getTerms()) {
                final BigDecimal itf = term.idf(documents);
                res = res.add(term.tfIdf(itf), Term.MC);
            }
            results.add(doc.setTfIdf(res));
        }
        return results;
    }
}
