package com.devo.tf.domain;



import java.util.Set;

public interface TfIdfService {
    Set<Document> report(Set<Document> documents);
}
