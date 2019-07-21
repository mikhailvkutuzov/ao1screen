package com.ao1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ItemReaderManagerCsvDirectories {
    private List<ItemsReaderManagerCsvDirectory1Thread> managers;
    private AtomicInteger workingReaders;

    public ItemReaderManagerCsvDirectories(String[] directories, ItemsDividerManager divider, Runnable workDone) {
        workingReaders = new AtomicInteger(directories.length);
        managers = new ArrayList<>(directories.length);
        for (String directory : directories) {
            managers.add(new ItemsReaderManagerCsvDirectory1Thread(new File(directory), divider, () -> {
                if(workingReaders.decrementAndGet() == 0) {
                    workDone.run();
                }
            }));
        }
    }


}
