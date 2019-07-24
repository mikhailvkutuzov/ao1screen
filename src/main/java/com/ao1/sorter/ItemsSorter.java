package com.ao1.sorter;

import com.ao1.data.Item;

import java.util.Comparator;
import java.util.List;

/**
 * To sort {@link Item} the same way and with the same criterion (itemComparator).
 */
public interface ItemsSorter {

    List<Item> sort(List<Item> items);

    Comparator<Item> itemComparator = (i1, i2) -> {
        int result = i1.getDecimalPrice().compareTo(i2.getDecimalPrice());

        if (result != 0) {
            return result;
        } else {
            if (i1.getProductId() > i2.getProductId()) {
                return 1;
            } else {
                if (i1.getProductId() < i2.getProductId()) {
                    return -1;
                } else {
                    result = i1.getName().compareTo(i2.getName());
                    if (result != 0) {
                        return result;
                    } else {
                        result = i1.getCondition().compareTo(i2.getCondition());
                        if (result != 0) {
                            return result;
                        } else {
                            return i1.getState().compareTo(i2.getState());
                        }
                    }
                }
            }
        }
    };


}

