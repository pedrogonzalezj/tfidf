package com.devo.tf.application;

import com.devo.tf.domain.Context;
import com.devo.tf.domain.Document;
import com.devo.tf.domain.TfIdfService;

import java.util.Set;

public class ScheduledReporter implements Runnable {

    private Context ctx;

    private TfIdfService service;
    private TfUI ui;

    public ScheduledReporter(TfIdfService service,
                    TfUI ui,
                    Context ctx) {
        this.ctx = ctx;
        this.ui = ui;
        this.service = service;
    }

    @Override
    public void run() {
        final Set<Document> results = service.report(ctx.getDocuments());
        ui.giveBackReport(results);
    }
}
