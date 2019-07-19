package com.ao1.divider;

import com.ao1.data.ItemToBeRead;

import java.util.List;


/**
 * Every chunk of data should be split on groups to be consumed.
 */
public interface ItemsDivider {

    /**
     * Split list of items into array of list of items.
     * Every array element with an index contains data for a consumer with that index.
     * @param items
     * @return
     */
    List<ItemToBeRead>[] divide(List<ItemToBeRead> items);

}
