package com.devo.tf.application;

import com.devo.tf.domain.Document;
import com.devo.tf.domain.Term;
import com.devo.tf.domain.TermFrecuencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentAnalysisServiceImplTests {
    private DocumentAnalysisService service;
    private TermFrecuencyService mockTermFrecuencyService;

    @BeforeEach
    public void setup() {
        mockTermFrecuencyService = mock(TermFrecuencyService.class);
        service = new DocumentAnalysisServiceImpl(mockTermFrecuencyService);
    }

    @Test
    @DisplayName("analizes all files in directory for all terms")
    public void assertThatAnalyzesDocuments() {
        File fileA = new File("src/test/resources/document_1.txt");
        Term termA = new Term("Lorem", BigDecimal.valueOf(0.10));
        Term termB = new Term("Ipsum", BigDecimal.valueOf(0.21));
        Document docA = new Document.DocumentBuilder().withFile(fileA).addTerm(termA).addTerm(termB).build();
        when(mockTermFrecuencyService.calculate("Lorem",fileA)).thenReturn(Optional.of(termA));
        when(mockTermFrecuencyService.calculate("Ipsum",fileA)).thenReturn(Optional.of(termB));
        Optional<Document> results = service.analyze(fileA, new HashSet<String>(){{add("Lorem"); add("Ipsum");}});
        assertEquals(docA,results.get());
        verify(mockTermFrecuencyService).calculate("Lorem",fileA);
        verify(mockTermFrecuencyService).calculate("Ipsum",fileA);
    }
}
