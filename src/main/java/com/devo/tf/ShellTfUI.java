package com.devo.tf;

import com.devo.tf.application.TfUI;
import com.devo.tf.domain.Document;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ShellTfUI implements TfUI {

    private int maxResults;

    public ShellTfUI(int maxResults) {
        this.maxResults = maxResults;
    }

    @Override
    public void giveBackReport(Set<Document> tfidfs) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(3);
        df.setMinimumFractionDigits(0);
        List<Document> sortedDocs = new ArrayList<>(tfidfs);
        sortedDocs.sort((a,b)-> b.compareTo(a));
        for(int i = 0; i<maxResults && i<sortedDocs.size(); i++) {
            System.out.println(sortedDocs.get(i).getDocumentName() + " " + df.format(sortedDocs.get(i).getTfIdf()));
        }
        System.out.println();
    }
}
