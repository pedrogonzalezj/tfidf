package com.devo.tf.domain;


import com.devo.tf.infrastructure.FileReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.mockito.Mockito.*;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class DocumentAnalysisServiceImplTests {
    private DocumentAnalysisService service;
    private FileReader mockFileReader;
    private File fileA;

    @BeforeEach
    public void setup() {

        mockFileReader = mock(FileReader.class);
        service = new DocumentAnalysisServiceImpl(mockFileReader);

        fileA = new File("src/test/resources/document_1.txt");
    }

    @Test
    @DisplayName("analizes all files in directory for all terms")
    public void assertThatAnalyzesDocuments() {
        when(mockFileReader.read(fileA)).thenReturn(Stream.of("Lorem Ipsum of simply,"," dummy text, of the funny lorem Of Oz"));
        Term tA = new Term("lorem",BigDecimal.valueOf(2).divide(BigDecimal.valueOf(12),Term.MC));
        Term tB = new Term("ipsum",BigDecimal.valueOf(1).divide(BigDecimal.valueOf(12),Term.MC));
        Term tC = new Term("of",BigDecimal.valueOf(3).divide(BigDecimal.valueOf(12),Term.MC));
        Document docA = new Document.DocumentBuilder().withFile(fileA).build();
        Optional<Document> mayBeDoc = service.analyze(fileA, new HashSet<String>(){{add("Lorem"); add("Ipsum");add("jam");add("of");}});
        Document resultsDoc = mayBeDoc.get();
        assertEquals(docA,resultsDoc);
        assertEquals(3,resultsDoc.getTerms().size());
        assertThat(resultsDoc.getTerms(),containsInAnyOrder(
                samePropertyValuesAs(tA),
                samePropertyValuesAs(tB),
                samePropertyValuesAs(tC)
        ));
    }
}
