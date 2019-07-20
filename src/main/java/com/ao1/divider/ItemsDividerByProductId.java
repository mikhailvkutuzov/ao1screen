package com.ao1.divider;

import com.ao1.data.ItemToBeRead;
import com.ao1.data.ItemToBeSorted;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * We should split items the way that every item with a the same productId was always consumed by the same items consumer.
 */
public class ItemsDividerByProductId implements ItemsDivider {
    private int amountOfConsumers;

    /**
     * @param amountOfConsumers we use this parameter to spread productId's between consumers the same way any time
     */
    public ItemsDividerByProductId(int amountOfConsumers) {
        this.amountOfConsumers = amountOfConsumers;
    }

    @Override
    public List<ItemToBeSorted>[] divide(List<ItemToBeRead> items) {

        List<ItemToBeSorted>[] divided = new List[amountOfConsumers];

        for (int i = 0; i < amountOfConsumers; i++) {
            divided[i] = new ArrayList<>(2 * items.size() / amountOfConsumers);
        }

        for (ItemToBeRead item : items) {
            divided[item.getProductId() % amountOfConsumers].add(new ItemToBeSorted(item, new BigDecimal(item.getPrice())));
        }

        return divided;
    }
}
