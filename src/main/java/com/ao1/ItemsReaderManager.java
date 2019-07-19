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

public class ItemsReaderManager {
    private static final Logger logger = LoggerFactory.getLogger(ItemsReaderManager.class);

    private ItemsDividerManager itemsDividerManager;
    private ScheduledExecutorService service;

    public ItemsReaderManager(File directory, ItemsDividerManager itemsDividerManager) {
        if (!directory.isDirectory()) {
            throw new FileHasBeenPassedInsteadOfDirectory();
        }
        this.itemsDividerManager = itemsDividerManager;
        this.service = Executors.newSingleThreadScheduledExecutor();
        File[] files = directory.listFiles((file, name) -> name.endsWith(".csv"));

        service.execute(new FeedDividerTask(files));
    }

    private class FeedDividerTask implements Runnable {
        private File[] files;
        private int counter;
        private HandsOnReservedFirstOfAll reader;


        public FeedDividerTask(File[] files) {
            this.files = files;
            this.counter = 0;
            this.reader = null;
        }

        @Override
        public void run() {
            logger.debug("have a new scheduled run");
            while (counter < files.length) {
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
                        } catch (ItemsDividerManager.TooMuchFood e) {
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
                counter++;
            }
        }
    }

    public static class FileHasBeenPassedInsteadOfDirectory extends RuntimeException {
    }

}
