package com.ao1;

import com.ao1.data.Item;
import com.ao1.sorter.ItemsSorter;
import com.ao1.sorter.ItemsSorterProductGroupsRestrictedAmountMergeSorted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


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

    private AtomicBoolean stopped;
    private AtomicInteger taskCounter;

    private int maxCounter;
    private int conveyorAmount;

    public ItemsSorterManagerStopMergeGet(int conveyorsAmount, int groupSize, int sortedAmount, int maxPendingTasks) {
        this.conveyorAmount = conveyorsAmount;
        this.maxCounter = maxPendingTasks;

        taskCounter = new AtomicInteger(0);
        stopped = new AtomicBoolean(false);
        workers = new ArrayList<>(conveyorAmount);

        for (int i = 0; i < conveyorAmount; i++) {
            SorterAndExecutor worker = new SorterAndExecutor();
            workers.add(worker);
            worker.service = Executors.newSingleThreadScheduledExecutor();
            worker.sorter = new ItemsSorterProductGroupsRestrictedAmountMergeSorted(groupSize, sortedAmount);
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
    public void feed(List<Item>[] items) throws TooMuchFood {
        if (!stopped.get()) {
            if (items.length != conveyorAmount) {
                throw new ItemsAreDividedOnAmountOfListDiffersFromAmountOfExecutors();
            }

            if (taskCounter.get() + conveyorAmount <= maxCounter) {
                taskCounter.addAndGet(conveyorAmount);
                for (int i = 0; i < conveyorAmount; i++) {
                    ItemsSorter sorter = workers.get(i).sorter;
                    List<Item> portion = items[i];
                    workers.get(i).service.execute(() -> {
                        taskCounter.decrementAndGet();
                        try {
                            sorter.sort(portion);
                        } catch (Throwable e) {
                            logger.error("Could not sort some items", e);
                        }
                    });
                }
            } else {
                throw new TooMuchFood(50);
            }
        } else {
            throw new NoWayToFeed();
        }
    }

    private static class ItemsAreDividedOnAmountOfListDiffersFromAmountOfExecutors extends RuntimeException {
    }

    private static class NoWayToFeed extends RuntimeException {
    }

    /**
     * Get sorted result if all the sorted tasks has been done.
     * <p>
     * It is not recommended to call the task if this {@link ItemsSorterManager} is running
     * it may lead to checked exception or worse to invalid results.
     * <p>
     * The recommended way is to call it from workDone class.
     *
     * @return finally sorted data
     * @throws NoDataReady if some pre-sorting tasks are still in progress
     */
    @Override
    public List<Item> getSorted() throws NoDataReady {
        if (taskCounter.get() != 0) {
            throw new NoDataReady(taskCounter.get());
        }
        logger.info("begin sorting operation");

        ItemsSorter sorter = workers.stream().parallel()
                .map(p -> p.sorter)
                .reduce((s1, s2) -> {
                    List<Item> l = s2.sort(new ArrayList<>(0));
                    if(logger.isInfoEnabled()){
                        logger.info(" sorted items {}: {}" , l.size(), l.stream().map(j -> j.toString()).collect(Collectors.joining(",")));
                    }
                    l = s1.sort(l);
                    if(logger.isInfoEnabled()){
                        logger.info(" sorted items {}: {}" , l.size(), l.stream().map(j -> j.toString()).collect(Collectors.joining(",")));
                    }
                    return s1;
                }).get();

        return sorter.sort(new ArrayList<>(0));
    }

    public void stop(Runnable workDone) {
        stopped.set(true);
        ScheduledExecutorService service = workers.get(0).service;
        service.execute(new StoppingTask(workDone, service));
    }

    private class StoppingTask implements Runnable {
        Runnable workDone;
        ScheduledExecutorService service;

        StoppingTask(Runnable workDone, ScheduledExecutorService service) {
            this.workDone = workDone;
            this.service = service;
        }

        @Override
        public void run() {
            if (taskCounter.get() == 0) {
                workDone.run();
            } else {
                service.schedule(this, 10, TimeUnit.MILLISECONDS);
            }
        }
    }


    private class SorterAndExecutor {
        ScheduledExecutorService service;
        ItemsSorter sorter;
    }
}
