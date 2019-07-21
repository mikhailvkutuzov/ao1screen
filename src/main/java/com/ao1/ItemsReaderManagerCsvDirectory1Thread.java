package com.ao1;

import com.ao1.reader.HandsOnReservedFirstOfAll;
import com.ao1.reader.StringDataReader;
import com.ao1.reader.StringDataReaderCsvFileWithHeaderByChunks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ItemsReaderManagerCsvDirectory1Thread {
    private static final Logger logger = LoggerFactory.getLogger(ItemsReaderManagerCsvDirectory1Thread.class);

    private ItemsDividerManager itemsDividerManager;
    private ScheduledExecutorService service;
    private File directory;
    private int counter;
    private Runnable workDone;

    public ItemsReaderManagerCsvDirectory1Thread(File directory, ItemsDividerManager itemsDividerManager, Runnable workDone) {
        if (!directory.isDirectory()) {
            throw new FileHasBeenPassedInsteadOfDirectory();
        }
        this.directory = directory;
        this.itemsDividerManager = itemsDividerManager;
        this.service = Executors.newSingleThreadScheduledExecutor();
        this.workDone = workDone;

        File[] files = directory.listFiles((file, name) -> name.endsWith(".csv"));
        counter = files.length - 1;
        service.execute(new FeedDividerTask(files));

    }

    private class FeedDividerTask implements Runnable {
        private File[] files;
        private HandsOnReservedFirstOfAll reader;


        public FeedDividerTask(File[] files) {
            this.files = files;
            this.reader = null;
        }

        @Override
        public void run() {
            logger.debug("have a new scheduled run");
            try {
                while (counter >= 0) {
                    File file = files[counter];
                    try {
                        if (reader == null) {
                            reader = new HandsOnReservedFirstOfAll(new StringDataReaderCsvFileWithHeaderByChunks(file, itemsDividerManager.desiredDataChunk()));
                            logger.debug("created a reader");
                        }
                        while (true) {
                            String data = reader.read();
                            try {
                                logger.debug("try to feed");
                                itemsDividerManager.feed(data);
                            } catch (TooMuchFood e) {
                                logger.error("feeding problems, sleep for {} ms", e.millisecondsToWait);
                                reader.reserveForNextRead(data);
                                service.schedule(this, e.millisecondsToWait, TimeUnit.MILLISECONDS);
                                return;
                            }
                        }
                    } catch (IOException e) {
                        logger.error("there were some problems with a file {} and date may be corrupted", file);
                    } catch (StringDataReader.NoMoreDataAvailable noMoreDataAvailable) {
                        logger.info("a file named {} has been consumed completely", file);
                    }
                    reader = null;
                    counter--;
                }
            } catch (Throwable t) {
                logger.error("an error has occurred reading csv directory {}" ,directory, t);
                logger.error("lost csv files amount {}", counter);
            } finally {
                workDone.run();
            }
        }
    }

    public static class FileHasBeenPassedInsteadOfDirectory extends RuntimeException {
    }

}
