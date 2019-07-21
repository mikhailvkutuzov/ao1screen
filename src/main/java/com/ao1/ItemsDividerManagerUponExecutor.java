package com.ao1;

import com.ao1.data.ItemToBeRead;
import com.ao1.data.ItemToBeSorted;
import com.ao1.divider.ItemsDivider;
import org.csveed.api.CsvClient;
import org.csveed.api.CsvClientImpl;

import java.io.StringReader;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ItemsDividerManagerUponExecutor implements ItemsDividerManager {

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
            tasksInProgress.decrementAndGet();

            CsvClient<ItemToBeRead> reader = new CsvClientImpl<>(new StringReader(data), ItemToBeRead.class);
            List<ItemToBeRead> items = reader.readBeans();
            List<ItemToBeSorted>[] divided = divider.divide(items);
            try {
                sorterManager.feed(divided);
            } catch (TooMuchFood tooMuchFood) {
                tasksInProgress.incrementAndGet();
                service.schedule(this, tooMuchFood.millisecondsToWait, TimeUnit.MILLISECONDS);
            }
        }
    }

    public void stop() {
        service.execute(workDone);
        service.shutdown();
    }
}
