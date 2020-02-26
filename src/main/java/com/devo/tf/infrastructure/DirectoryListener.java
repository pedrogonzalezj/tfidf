package com.devo.tf.infrastructure;


import com.devo.tf.domain.DocumentAnalysisService;
import com.devo.tf.domain.Context;
import com.devo.tf.domain.Document;

import java.nio.file.*;
import java.util.Optional;
import java.util.Set;


public class DirectoryListener implements Runnable {

    private Context context;
    private DocumentAnalysisService documentAnalysisService;
    private WatchService watcher;
    private Path dir;
    private Set<String> terms;

    public DirectoryListener(WatchService watcher,
                             DocumentAnalysisService documentAnalysisService,
                             Set<String> terms,
                             Path dir,
                             Context context) {
        this.documentAnalysisService = documentAnalysisService;
        this.terms = terms;
        this.watcher = watcher;
        this.dir = dir;
        this.context = context;
    }

    @Override
    public void run() {
        // wait for key to be signaled
        WatchKey key;
        for (;;) {
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    continue;
                }
                WatchEvent<Path> ev = (WatchEvent<Path>)event;
                Path filename = ev.context();
                Path child = dir.resolve(filename);
                Optional<Document> doc = documentAnalysisService.analyze(child.toFile(),terms);
                doc.ifPresent(d -> context.addDocument(d));
            }
            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
    }
}
