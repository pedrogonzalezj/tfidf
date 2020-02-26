package com.devo.tf.domain;

import com.devo.tf.infrastructure.FileReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.io.File;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TermFrecuencyServiceImplTests {

    private TermFrecuencyService service;
    private FileReader mockFileReader;

    private File fileA;
    private File fileB;
    private File fileC;

    @BeforeEach
    public void setup() {
        mockFileReader = mock(FileReader.class);
        service = new TermFrecuencyServiceImpl(mockFileReader);
        fileA = new File("src/test/resources/document_1.txt");
        fileB = new File("src/test/resources/document_2.txt");
        fileC = new File("src/test/resources/document_3.txt");
    }

    @Test
    @DisplayName("term frecuency value is the right one")
    public void assertThatCanCalculateTermFrecuency() {
        when(mockFileReader.read(fileA)).thenReturn(Stream.of("Lorem Ipsum of simply,"," dummy text, of the "));
        Optional<Term>  maybeTerm = service.calculate("ipsum", fileA);
        assertEquals((float)1/8,maybeTerm.get().getFrecuency().floatValue(),0.01f);
        verify(mockFileReader).read(fileA);
        when(mockFileReader.read(fileB)).thenReturn(Stream.of("Lorem Ipsum of simply,"," dummy text, of the "));
        maybeTerm = service.calculate("of", fileB);
        assertEquals((float)2/8,maybeTerm.get().getFrecuency().floatValue(),0.01f);
        verify(mockFileReader).read(fileB);
        when(mockFileReader.read(fileC)).thenReturn(Stream.of("Lorem Ipsum of simply,"," dummy text, of the "));
        maybeTerm = service.calculate("jam", fileC);
        assertFalse(maybeTerm.isPresent());
        verify(mockFileReader).read(fileC);
    }
}
