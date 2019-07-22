package com.ao1;

import com.ao1.data.Item;

import java.util.List;

public interface ItemsSorterManager {

    /**
     * @return an amount of dedicated thread digesting the data
     */
    int conveyorsAmount();

    /**
     * To feed dedicated threads we use an array of list. The array size equals to conveyorsAmount() to let each conveyor
     * get it's own chunk of {@link Item}+.
     * @param items
     * @throws TooMuchFood if there are too much tasks in a sorter
     */
    void feed(List<Item>[] items) throws TooMuchFood;


    /**
     * @return Items sorted in an ascending order
     */
    List<Item> getSorted() throws NoDataReady;


    /**
     * Stop all the sorting  activities.
     * @param workDone
     */
    void stop(Runnable workDone);

}
