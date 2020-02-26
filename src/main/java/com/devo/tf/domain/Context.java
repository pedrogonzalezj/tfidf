package com.devo.tf.domain;

import java.util.HashSet;
import java.util.Set;

public class Context {

    private Set<Document> documents;

    public Context() {
        documents = new HashSet<>();
    }

    public synchronized Set<Document> getDocuments() {
        return documents;
    }

    public synchronized void addDocument(final Document doc) {
        documents.add(doc);
    }
}
