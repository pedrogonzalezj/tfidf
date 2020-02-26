package com.devo.tf.application;

import com.devo.tf.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class DirectoryAnalysisServiceImplTests {

    private DirectoryAnalysisService service;
    private DocumentAnalysisService mockDocumentAnalysisService;

    @BeforeEach
    public void setup() {
        mockDocumentAnalysisService = mock(DocumentAnalysisService.class);
        service = new DirectoryAnalysisServiceImpl(mockDocumentAnalysisService);
    }

    @Test
    @DisplayName("throws exception when directory is file or can not read it")
    public void assertThatFailsWhenFolderIsNotADirectory() {
        final Context ctx = new Context();
        final Set<String> terms = new HashSet<>();
        terms.add("Lorem");
        assertThrows(BadDirectoryException.class, () -> service.analyze(ctx,"src/test/resources/another",terms));
    }

    @Test
    @DisplayName("analizes all files in directory for all terms")
    public void assertThatAnalyzesDocuments() {
        final Context ctx = new Context();
        final Set<String> terms = new HashSet<>();
        terms.add("Lorem");
        terms.add("Ipsum");
        File fileA = new File("src/test/resources/document_1.txt");
        File fileB = new File("src/test/resources/document_2.txt");
        File fileC = new File("src/test/resources/document_3.txt");
        Term termA = new Term("Lorem", BigDecimal.valueOf(0.10));
        Term termB = new Term("Lorem", BigDecimal.valueOf(0.010));
        Document docA = new Document.DocumentBuilder().withFile(fileA).addTerm(termA).build();
        Document docB =new Document.DocumentBuilder().withFile(fileB).addTerm(termB).build();
        Document docC =new Document.DocumentBuilder().withFile(fileC).build();
        Set<Document> documents = new HashSet<>();
        documents.add(docA);
        documents.add(docB);
        documents.add(docC);
        when(mockDocumentAnalysisService.analyze(fileA,terms)).thenReturn(Optional.of(docA));
        when(mockDocumentAnalysisService.analyze(fileB,terms)).thenReturn(Optional.of(docB));
        when(mockDocumentAnalysisService.analyze(fileC,terms)).thenReturn(Optional.of(docC));
        service.analyze(ctx,"src/test/resources/",terms);
        verify(mockDocumentAnalysisService).analyze(fileA,terms);
        verify(mockDocumentAnalysisService).analyze(fileB,terms);
        verify(mockDocumentAnalysisService).analyze(fileC,terms);
        assertEquals(documents,ctx.getDocuments());
    }
}
