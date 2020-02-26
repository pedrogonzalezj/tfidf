package com.devo.tf.application;

import com.devo.tf.domain.Document;
import com.devo.tf.domain.Term;
import com.devo.tf.domain.TermFrecuencyService;
import com.devo.tf.infrastructure.FileReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentAnalysisServiceImplTests {
    private DocumentAnalysisService service;
    private TermFrecuencyService mockTermFrecuencyService;
    private FileReader mockFileReader;
    private File fileA;
    private List<String> words;

    @BeforeEach
    public void setup() {
        mockTermFrecuencyService = mock(TermFrecuencyService.class);
        mockFileReader = mock(FileReader.class);
        service = new DocumentAnalysisServiceImpl(mockTermFrecuencyService,mockFileReader);

        fileA = new File("src/test/resources/document_1.txt");
        words = new ArrayList<>();
        words.add("lorem");
        words.add("ipsum");
        words.add("of");
        words.add("simply");
        words.add("dummy");
        words.add("text");
        words.add("of");
        words.add("the");
    }

    @Test
    @DisplayName("analizes all files in directory for all terms")
    public void assertThatAnalyzesDocuments() {
        when(mockFileReader.read(fileA)).thenReturn(Stream.of("Lorem Ipsum of simply,"," dummy text, of the "));
        Term termA = new Term("Lorem", BigDecimal.valueOf(0.10));
        Term termB = new Term("Ipsum", BigDecimal.valueOf(0.21));
        Document docA = new Document.DocumentBuilder().withFile(fileA).addTerm(termA).addTerm(termB).build();
        when(mockTermFrecuencyService.calculate("Lorem",words)).thenReturn(Optional.of(termA));
        when(mockTermFrecuencyService.calculate("Ipsum",words)).thenReturn(Optional.of(termB));
        Optional<Document> results = service.analyze(fileA, new HashSet<String>(){{add("Lorem"); add("Ipsum");}});
        assertEquals(docA,results.get());
        verify(mockTermFrecuencyService).calculate("Lorem",words);
        verify(mockTermFrecuencyService).calculate("Ipsum",words);
    }
}
