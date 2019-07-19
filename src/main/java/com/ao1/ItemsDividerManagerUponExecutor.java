package com.ao1;

import com.ao1.data.ItemToBeRead;
import com.ao1.divider.ItemsDivider;
import org.csveed.api.CsvClient;
import org.csveed.api.CsvClientImpl;

import java.io.StringReader;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class ItemsDividerManagerUponExecutor implements ItemsDividerManager {

    private ScheduledExecutorService service;
    private int linesAmount;
    private ItemsDivider divider;

    private AtomicInteger tasksInProgress;
    private int maxTasksInProgress;

    public ItemsDividerManagerUponExecutor(int amountOfThreads, int linesAmount, int maxTasksInProgress) {
        this.service = Executors.newScheduledThreadPool(amountOfThreads);
        this.linesAmount = linesAmount;

        this.maxTasksInProgress = maxTasksInProgress;
        this.tasksInProgress = new AtomicInteger(0);
    }

    /**
     * This method returns an amount of lines of strings it is ready to digest at once.
     *
     * @return
     */
    public int desiredDataChunk() {
        return linesAmount;
    }

    @Override
    public void feed(String data) throws TooMuchFood {
        if (tasksInProgress.incrementAndGet() < maxTasksInProgress) {
            service.execute(() -> {
                tasksInProgress.decrementAndGet();
                CsvClient<ItemToBeRead> reader = new CsvClientImpl<>(new StringReader(data), ItemToBeRead.class);
                List<ItemToBeRead> items = reader.readBeans();
                List<ItemToBeRead>[] divided = divider.divide(items);


            });
        } else {
            tasksInProgress.decrementAndGet();
            throw new TooMuchFood(50);
        }
    }
}
