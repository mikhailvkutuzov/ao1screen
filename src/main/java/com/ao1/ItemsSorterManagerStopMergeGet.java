package com.ao1;

import com.ao1.data.ItemToBeSorted;
import com.ao1.sorter.ItemsSorter;
import com.ao1.sorter.ItemsSorterWithProductGroupsAndHoleAmountRestrictedUsingPresortedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * The main idea is to have several(as many as possible) single threaded executors and feed them with the
 * items of the same product types(every product type is confined to some special executor). There are
 * dedicated {@link ItemsSorter}s sorting items on these executors in parallel.
 * When all data are consumed and sorted separately we have conveyorsAmount sorted collections of {@link com.ao1.data.Item}s
 * and are able to merge these results into one to be obtained via getSorted() operation.
 */
public class ItemsSorterManagerStopMergeGet implements ItemsSorterManager {
    private static final Logger logger = LoggerFactory.getLogger(ItemsSorterManagerStopMergeGet.class);

    private ArrayList<SorterAndExecutor> workers;

    private AtomicInteger taskCounter;

    private int maxCounter;
    private int conveyorAmount;
    private int groupSize;
    private int sortedAmount;

    public ItemsSorterManagerStopMergeGet(int conveyorsAmount, int groupSize, int sortedAmount, int maxPendingTasks) {
        this.conveyorAmount = conveyorsAmount;
        this.groupSize = groupSize;
        this.sortedAmount = sortedAmount;
        this.maxCounter = maxPendingTasks;


        taskCounter = new AtomicInteger(0);
        workers = new ArrayList<>(conveyorAmount);
        for (int i = 0; i < conveyorAmount; i++) {
            SorterAndExecutor worker = new SorterAndExecutor();
            workers.add(worker);
            worker.service = Executors.newSingleThreadExecutor();
            worker.sorter = new ItemsSorterWithProductGroupsAndHoleAmountRestrictedUsingPresortedData(groupSize, sortedAmount);
        }

    }

    @Override
    public int conveyorsAmount() {
        return conveyorAmount;
    }

    /**
     * This method may sometimes lead to more than maxCounter task will be put into executors, but
     * we can tolerate it well due to an excess of tasks could not be much more than conveyorAmount.
     *
     * @param items items should be split between services and sorted later
     * @throws TooMuchFood it is thrown if an amount of tasks in  progress > maxCounter
     */
    public void feed(List<ItemToBeSorted>[] items) throws TooMuchFood {
        if (items.length != conveyorAmount) {
            throw new IllegalArgumentException();
        }

        if (taskCounter.get() + conveyorAmount <= maxCounter) {
            taskCounter.addAndGet(conveyorAmount);
            for (int i = 0; i < conveyorAmount; i++) {
                ItemsSorter sorter = workers.get(i).sorter;
                List<ItemToBeSorted> portion = items[i];
                workers.get(i).service.execute(() -> {

                    taskCounter.decrementAndGet();
                    try {
                        sorter.sort(portion);
                    } catch (Exception e) {
                        logger.error("Could not sort some items", e);
                    }

                });
            }
        } else {
            throw new TooMuchFood(50);
        }
    }


    /**
     * Get sorted result if all the sorted tasks has been done.
     *
     * It is not recommended to call the task if this {@link ItemsSorterManager} is running
     * it may lead to checked exception or worse to invalid results.
     *
     * The recommended way is to call it from workDone class.
     *
     * @return finally sorted data
     * @throws NoDataReady if some pre-sorting tasks are still in progress
     */
    @Override
    public List<ItemToBeSorted> getSorted() throws NoDataReady {
        if (taskCounter.get() != 0) {
            throw new NoDataReady(taskCounter.get());
        }

        ItemsSorter sorter = (ItemsSorter) workers.stream().parallel()
                .map(p -> p.sorter)
                .reduce((s1, s2) -> {
                    s1.sort(s2.sort(Collections.EMPTY_LIST));
                    return s1;
                }).get();

        return sorter.sort(Collections.EMPTY_LIST);
    }

    /**
     * On stop signal we have to put workDone task into one of the task, due to some task might be
     * running right know or waiting their time to be run, this way we put the last task into executor
     * and it's execution say that data are
     */
    public void stop(Runnable workDone) {
        workers.get(0).service.execute(workDone);
        for (SorterAndExecutor worker : workers) {
            worker.service.shutdown();
        }
    }


    private class SorterAndExecutor {
        public ExecutorService service;
        public ItemsSorter sorter;
    }
}
