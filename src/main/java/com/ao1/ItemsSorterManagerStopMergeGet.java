package com.ao1;

import com.ao1.data.ItemToBeRead;
import com.ao1.data.ItemToBeSorted;
import com.ao1.sorter.ItemsSorter;
import com.ao1.sorter.ItemsSorterWithProductGroupsAndHoleAmountRestrictedUsingPresortedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private ExecutorService[] services;
    private AtomicInteger counter;
    private ItemsSorter[] sorters;

    private int maxCounter;
    private int conveyorAmount;
    private int groupSize;
    private int sortedAmount;

    public ItemsSorterManagerStopMergeGet(int conveyorsAmount, int groupSize, int sortedAmount, int maxCounter) {
        this.conveyorAmount = conveyorsAmount;
        this.groupSize = groupSize;
        this.sortedAmount = sortedAmount;
        this.maxCounter = maxCounter;

    }

    @Override
    public int conveyorsAmount() {
        return conveyorAmount;
    }

    /**
     * This method may sometimes lead to more than maxCounter task will be put into executors, but
     * we can tolerate it well due to an excess of tasks is no more than conveyorsAmount.
     *
     * @param items items should be split between services and sorted later
     * @throws TooMuchFood it is thrown if an amount of tasks in  progress > maxCounter
     */
    public synchronized void feed(List<ItemToBeSorted>[] items) throws TooMuchFood {
        if (items.length != conveyorAmount) {
            throw new IllegalArgumentException();
        }

        if (counter.get() + conveyorAmount <= maxCounter) {
            counter.addAndGet(conveyorAmount);
            for (int i = 0; i < conveyorAmount; i++) {
                ItemsSorter sorter = sorters[i];
                List<ItemToBeSorted> portion = items[i];
                services[i].execute(() -> {
                    try {
                        sorter.sort(portion);
                    } catch (Exception e) {
                        logger.error("Could not sort some items", e);
                    } finally {
                        counter.decrementAndGet();
                    }
                });
            }
        } else {
            throw new TooMuchFood(50);
        }
    }


    /**
     * This method is dangerous and should be used from the protected piece of code knowing
     * exactly the situation with data. If we call sort and put some  new data in a millisecond to be sorted
     * we will get invalid results.

     * @return finally sorted data
     * @throws NoDataReady if some pre-sorting tasks are still in progress
     */
    @Override
    public List<ItemToBeSorted> getSorted() throws NoDataReady {
        if(haveSomeWork()) {
            throw new NoDataReady();
        }

        return null;
    }

    @Override
    public boolean haveSomeWork() {
        return counter.get() != 0;
    }

    @Override
    public void start() {
        counter = new AtomicInteger(0);
        services = new ExecutorService[conveyorAmount];
        for (int i = 0; i < conveyorAmount; i++) {
            services[i] = Executors.newSingleThreadExecutor();
            sorters[i] = new ItemsSorterWithProductGroupsAndHoleAmountRestrictedUsingPresortedData(groupSize, sortedAmount);
        }
    }

    @Override
    public void stop() {
        for (ExecutorService service : services) {
            service.shutdown();
        }
    }
}
