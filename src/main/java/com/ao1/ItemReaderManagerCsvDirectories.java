package com.ao1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ItemReaderManagerCsvDirectories {
    private static final Logger logger = LoggerFactory.getLogger(ItemReaderManagerCsvDirectories.class);
    private List<ItemsReaderManagerCsvDirectory1Thread> managers;
    private AtomicInteger workingReaders;

    public ItemReaderManagerCsvDirectories(String[] directories, ItemsDividerManager divider, Runnable workDone) {
        workingReaders = new AtomicInteger(directories.length);
        managers = new ArrayList<>(directories.length);
        for (String directory : directories) {
            managers.add(new ItemsReaderManagerCsvDirectory1Thread(new File(directory), divider, () -> {
                if(workingReaders.decrementAndGet() == 0) {
                    workDone.run();
                    logger.info("a work is done");
                } else {
                    logger.info("not finished = {} ", workingReaders.get());
                }
            }));
        }
    }


}
