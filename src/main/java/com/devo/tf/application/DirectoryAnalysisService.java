package com.devo.tf.application;

import com.devo.tf.domain.Context;

import java.util.Set;

public interface DirectoryAnalysisService {
    void analyze(final Context context, final String folder, final Set<String> terms);
}
