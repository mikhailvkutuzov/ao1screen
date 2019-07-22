package com.ao1;

import com.ao1.data.Item;
import com.ao1.divider.ItemsDivider;
import org.csveed.api.CsvClient;
import org.csveed.api.CsvClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ItemsDividerManagerUponExecutor implements ItemsDividerManager {
    private static final Logger logger = LoggerFactory.getLogger(ItemsDividerManagerUponExecutor.class);

    private ScheduledExecutorService service;
    private int linesAmount;
    private ItemsDivider divider;
    private ItemsSorterManager sorterManager;

    private AtomicInteger tasksInProgress;
    private int maxTasksInProgress;
    private Runnable workDone;

    public ItemsDividerManagerUponExecutor(int amountOfThreads, int linesAmount, int maxTasksInProgress, ItemsDivider divider, ItemsSorterManager sorterManager, Runnable workDone) {
        this.service = Executors.newScheduledThreadPool(amountOfThreads);
        this.linesAmount = linesAmount;
        this.divider = divider;
        this.sorterManager = sorterManager;
        this.maxTasksInProgress = maxTasksInProgress;
        this.tasksInProgress = new AtomicInteger(0);
        this.workDone = workDone;
    }

    /**
     * @return an amount of lines of strings it is ready to digest at once.
     */
    public int desiredDataChunk() {
        return linesAmount;
    }

    @Override
    public void feed(String data) throws TooMuchFood {
        if(!service.isShutdown()) {
            if (tasksInProgress.incrementAndGet() < maxTasksInProgress) {
                service.execute(new FeedSorterManager(data));
            } else {
                tasksInProgress.decrementAndGet();
                throw new TooMuchFood(50);
            }
        }
    }

    class FeedSorterManager implements Runnable {
        private String data;

        public FeedSorterManager(String data) {
            this.data = data;
        }

        @Override
        public void run() {
            try {
                tasksInProgress.decrementAndGet();

                CsvClient<Item> reader = new CsvClientImpl<>(new StringReader(data), Item.class);
                reader.setUseHeader(true);
                List<Item> items = reader.readBeans();
                List<Item>[] divided = divider.divide(items);
                try {
                    sorterManager.feed(divided);
                } catch (TooMuchFood tooMuchFood) {
                    tasksInProgress.incrementAndGet();
                    service.schedule(this, tooMuchFood.millisecondsToWait, TimeUnit.MILLISECONDS);
                }
            } catch (Throwable t) {
                logger.error("could not fed", t);
            }
        }
    }

    public void stop() {
        service.execute(workDone);
        service.shutdown();
    }
}
