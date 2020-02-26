package com.devo.tf.domain;

import com.devo.tf.infrastructure.FileReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TermFrecuencyServiceImplTests {

    private TermFrecuencyService service;

    @BeforeEach
    public void setup() {
        service = new TermFrecuencyServiceImpl();
    }

    @Test
    @DisplayName("term frecuency value is the right one")
    public void assertThatCanCalculateTermFrecuency() {
        final List<String> words = new ArrayList<>();
        words.add("lorem");
        words.add("ipsum");
        words.add("of");
        words.add("simply");
        words.add("dummy");
        words.add("text");
        words.add("of");
        words.add("the");
        Optional<Term>  maybeTerm = service.calculate("ipsum", words);
        assertEquals((float)1/8,maybeTerm.get().getFrecuency().floatValue(),0.01f);
        maybeTerm = service.calculate("of", words);
        assertEquals((float)2/8,maybeTerm.get().getFrecuency().floatValue(),0.01f);
        maybeTerm = service.calculate("jam", words);
        assertFalse(maybeTerm.isPresent());
    }
}
