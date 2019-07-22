package com.ao1;

import com.ao1.data.Item;
import com.ao1.divider.ItemsDividerByProductId;
import org.csveed.api.CsvClient;
import org.csveed.api.CsvClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class CsvSortingApplication {
    private static final Logger logger = LoggerFactory.getLogger(CsvSortingApplication.class);

    public void sort(File report, String[] directories, int sortingConveyorsAmount, int splittingConveyorsAmount, int itemLinesAmount, int groupSize, int reportSize) {

        CountDownLatch latch = new CountDownLatch(1);

        ItemsSorterManagerStopMergeGet sorter = new ItemsSorterManagerStopMergeGet(sortingConveyorsAmount, groupSize, reportSize, 5 * sortingConveyorsAmount);

        ItemsDividerManagerUponExecutor divider = new ItemsDividerManagerUponExecutor(splittingConveyorsAmount,
                itemLinesAmount,
                10,
                new ItemsDividerByProductId(sorter.conveyorsAmount()),
                sorter,
                () -> sorter.stop(() -> latch.countDown()));

        new ItemReaderManagerCsvDirectories(directories, divider, () -> divider.stop());

       try {
            latch.await();
            if (!report.exists()) {
                if(!report.createNewFile()) {
                    throw new CouldNotCreateAReportFile(report.getAbsolutePath());
                }
            }

           try (Writer writer = new BufferedWriter(new FileWriter(report))) {
               List<Item> items = sorter.getSorted();
               if(logger.isInfoEnabled()) {
                   for (Item i : items) {
                       logger.info("to be written {}", i);
                   }
               }
               CsvClient<Item> client = new CsvClientImpl<>(writer, Item.class);
               client.writeBeans(items);
           }
        } catch (Throwable t) {
            logger.error("we could not get a sorted list of items", t);
        } finally {
           System.exit(0);
        }
    }

    private static class CouldNotCreateAReportFile extends RuntimeException {
        public CouldNotCreateAReportFile(String message) {
            super(message);
        }
    }

}
