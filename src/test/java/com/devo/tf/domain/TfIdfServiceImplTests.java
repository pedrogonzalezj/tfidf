package com.devo.tf.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TfIdfServiceImplTests {

    private TfIdfService service;
    private Set<Document> documents;

    private BigDecimal tfIdfDocA;
    private BigDecimal tfIdfDocB;

    @BeforeEach
    public void setup() {
        service = new TfIdfServiceImpl();
        documents = new HashSet<>();
        float frecA = 0.25f;
        float frecB = 0.10f;
        float frecC = 0.10f;
        float frecD = 0.02f;
        float frecE = 0.29f;
        Term tA = new Term("Lorem", BigDecimal.valueOf(frecA));
        Term tB = new Term("Ipsum", BigDecimal.valueOf(frecB));
        Document docA = new Document.DocumentBuilder()
                .withFile(new File("src/test/resources/document_1.txt"))
                .addTerm(tA)
                .addTerm(tB)
                .build();
        Term tC = new Term("Lorem", BigDecimal.valueOf(frecC));
        Term tD = new Term("Ipsum", BigDecimal.valueOf(frecD));
        Term tE = new Term("Aldus", BigDecimal.valueOf(frecE));
        Document docB = new Document.DocumentBuilder()
                .withFile(new File("src/test/resources/document_2.txt"))
                .addTerm(tC)
                .addTerm(tD)
                .addTerm(tE)
                .build();
        documents.add(docA);
        documents.add(docB);

        BigDecimal tfIdfA = BigDecimal.valueOf(frecA).multiply(tA.idf(documents), Term.MC);
        BigDecimal tfIdfB = BigDecimal.valueOf(frecB).multiply(tB.idf(documents), Term.MC);
        tfIdfDocA = tfIdfA.add(tfIdfB, Term.MC);
        BigDecimal tfIdfC = BigDecimal.valueOf(frecC).multiply(tC.idf(documents), Term.MC);
        BigDecimal tfIdfD = BigDecimal.valueOf(frecD).multiply(tD.idf(documents), Term.MC);
        BigDecimal tfIdfE = BigDecimal.valueOf(frecE).multiply(tE.idf(documents), Term.MC);
        tfIdfDocB = tfIdfC.add(tfIdfD, Term.MC).add(tfIdfE, Term.MC);
    }

    @Test
    @DisplayName("report calculates tfidf for all documents")
    public void assertThatCalculatesTfIdf() {
        Set<Document> results = service.report(documents);
        assertEquals(2,results.size());
        for(Document doc : results) {
            assertTrue(doc.getTfIdf().equals(tfIdfDocA) || doc.getTfIdf().equals(tfIdfDocB));
        }
    }
}
