package com.ao1.sorter;

import com.ao1.data.ItemToBeSorted;

import java.util.List;

public interface ItemsSorter {

    List<ItemToBeSorted> sort(List<ItemToBeSorted> items);

}
