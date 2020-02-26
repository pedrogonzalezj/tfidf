package com.devo.tf;



import com.devo.tf.application.*;
import com.devo.tf.domain.*;
import com.devo.tf.infrastructure.DirectoryListener;
import com.devo.tf.infrastructure.FileReader;
import com.devo.tf.infrastructure.FileReaderImpl;
import org.apache.commons.cli.*;


import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;


public class App {

    public static void main( String[] args ) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("Exited from Tf/If checker program")));
        final Options options = commandLineOptions();
        try {
            final CommandLineParser cmd = new DefaultParser();
            final CommandLine line  = cmd.parse(options, args);
            final String directory  = line.getOptionValue("directory");
            final String tt         = line.getOptionValue("TT");
            final String maxResults = line.getOptionValue("max");
            final String period     = line.getOptionValue("period");
            final Set<String> terms = new HashSet<>(Arrays.asList(tt.split(" ")));
            final Path folder       = Paths.get(directory);
            final Integer iPeriod   = Integer.parseInt(period);
            // DI
            final Context ctx = new Context();
            final FileReader fileReader = new FileReaderImpl();
            final TfUI ui = new ShellTfUI(Integer.parseInt(maxResults));
            final TermFrecuencyService termFrecuencyService = new TermFrecuencyServiceImpl();
            final TfIdfService tfIdfService = new TfIdfServiceImpl();
            final DocumentAnalysisService documentAnalysisService = new DocumentAnalysisServiceImpl(termFrecuencyService,fileReader);
            final DirectoryAnalysisService service = new DirectoryAnalysisServiceImpl(documentAnalysisService);
            // Start
            service.analyze(ctx,directory,terms);
            // Schedule report generation
            final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            executor.scheduleWithFixedDelay(new ScheduledReporter(tfIdfService,ui,ctx), iPeriod, iPeriod, TimeUnit.SECONDS);
            // Listen to changes in directory
            final WatchService watchService = FileSystems.getDefault().newWatchService();
            folder.register(watchService, ENTRY_CREATE);
            new DirectoryListener(watchService,documentAnalysisService,terms,folder,ctx).run();
        } catch (ParseException | NumberFormatException | IOException ex) {
            System.out.println(ex.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("tf-idf analysis", options);
            System.exit(1);
        }
    }

    private static Options commandLineOptions() {
        final Options options = new Options();
        final Option optionD = Option.builder("d")
                .argName("d")
                .longOpt("directory")
                .hasArg(true)
                .numberOfArgs(1)
                .required(true)
                .type(File.class)
                .desc("The directory where the files to analyze resides")
                .build();
        final Option optionTT = Option.builder("t")
                .argName("t")
                .longOpt("TT")
                .hasArg(true)
                .numberOfArgs(1)
                .required(true)
                .type(String.class)
                .desc("The term to use")
                .build();
        final Option optionN = Option.builder("n")
                .argName("n")
                .longOpt("max")
                .hasArg(true)
                .numberOfArgs(1)
                .required(true)
                .type(Integer.class)
                .desc("Max number of results to show")
                .build();
        final Option optionP = Option.builder("p")
                .argName("p")
                .longOpt("period")
                .hasArg(true)
                .numberOfArgs(1)
                .required(true)
                .type(Integer.class)
                .desc("The time period in seconds between reports")
                .build();
        options.addOption(optionD);
        options.addOption(optionTT);
        options.addOption(optionN);
        options.addOption(optionP);
        return options;
    }
}
