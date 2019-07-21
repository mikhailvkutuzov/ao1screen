package com.ao1;

import com.ao1.data.ItemToBeRead;
import com.ao1.data.ItemToBeSorted;

import java.util.List;

public interface ItemsSorterManager {

    /**
     * @return an amount of dedicated thread digesting the data
     */
    int conveyorsAmount();

    /**
     * To feed dedicated threads we use an array of list. The array size equals to conveyorsAmount() to let each conveyor
     * get it's own chunk of {@link ItemToBeRead}+.
     * @param items
     * @throws TooMuchFood if there are too much tasks in a sorter
     */
    void feed(List<ItemToBeSorted>[] items) throws TooMuchFood;


    /**
     * @return Items sorted in an ascending order
     */
    List<ItemToBeSorted> getSorted() throws NoDataReady;

}
