package com.ao1.divider;

import com.ao1.data.Item;

import java.util.List;


/**
 * Every chunk of data should be split on groups to be consumed.
 */
public interface ItemsDivider {

    /**
     * Split list of items into array of list of items.
     * Every array element with an index contains data for a consumer with that index.
     * @param items
     * @return items are not only split but converted to a proper sorting form
     */
    List<Item>[] divide(List<Item> items);

}
