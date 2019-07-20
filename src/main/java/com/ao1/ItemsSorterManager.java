package com.ao1;

import com.ao1.data.ItemToBeRead;
import com.ao1.data.ItemToBeSorted;

import java.util.List;

public interface ItemsSorterManager extends Manager {

    /**
     * An amount of dedicated thread digesting the data
     * @return
     */
    int conveyorsAmount();

    /**
     * To feed dedicated threads we use an array of list. The array size equals to conveyorsAmount() to let each conveyor
     * get it's own chunk of {@link ItemToBeRead}+.
     * @param items
     * @throws TooMuchFood
     */
    void feed(List<ItemToBeRead>[] items) throws TooMuchFood;


    /**
     * Return
     * @return
     */
    List<ItemToBeSorted> getSorted() throws NoDataReady;

}
