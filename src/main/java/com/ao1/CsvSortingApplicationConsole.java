package com.ao1;

import org.apache.commons.cli.*;

import java.io.File;

public class CsvSortingApplicationConsole {
    public static void main(String[] args) {
        Options options = new Options();

        Option input = new Option("d", "directories", true, "a set of directories comma separated");
        input.setRequired(true);
        input.setValueSeparator(',');
        options.addOption(input);

        Option output = new Option("r", "report", true, "a file supposed to keep a report");
        output.setRequired(true);
        options.addOption(output);

        Option linesAtOnce = new Option("l", "lines", true, "an amount of csv lines to be consumed at once(default = 1000)");
        linesAtOnce.setRequired(false);
        options.addOption(linesAtOnce);

        Option splitters = new Option("s", "splitters", true, "an amount of threads for data serialization and splitting");
        splitters.setRequired(false);
        options.addOption(splitters);

        Option sorters = new Option("s", "sorters", true, "an amount of threads for data sorting");
        sorters.setRequired(false);
        options.addOption(sorters);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }

        String[] directories = cmd.getOptionValues("directories");

        File report = new File(cmd.getOptionValue("report"));

        String splittersAmount = cmd.getOptionValue("splitters");

        int splittingConveyorsAmount = splittersAmount == null ? 1 : Integer.parseInt(splittersAmount);

        String sortersAmount = cmd.getOptionValue("sorters");

        int sortingConveyorsAmount = sortersAmount == null ? 1 : Integer.parseInt(sortersAmount);

        String linesAmountAtOnce = cmd.getOptionValue("lines");

        int itemLinesAmount = linesAmountAtOnce == null ? 10000 : Integer.parseInt(linesAmountAtOnce);

        new CsvSortingApplication().sort(report, directories, sortingConveyorsAmount, splittingConveyorsAmount, itemLinesAmount, 20, 1000);
    }

}
