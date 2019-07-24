package com.ao1.sorter;

import com.ao1.data.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Take previously sorted items and sort the new ones together with them.
 * Satisfy the constraints :
 * 0. no more than maxGroupSize items of the same  product type should be presented
 * 1. no more than maxSortedSize items should be presented at all
 */
public class ItemsSorterProductGroupsRestrictedAmountInsertsIntoSorted implements ItemsSorter {
    private static final Logger logger = LoggerFactory.getLogger(ItemsSorterProductGroupsRestrictedAmountInsertsIntoSorted.class);

    private int maxGroupSize;
    private int maxSortedSize;

    private ArrayList<Item> sorted;
    private Map<Integer, List<Item>> productGroups;

    public ItemsSorterProductGroupsRestrictedAmountInsertsIntoSorted(int maxGroupSize, int maxSortedSize) {
        this.maxGroupSize = maxGroupSize;
        this.maxSortedSize = maxSortedSize;
        this.sorted = new ArrayList<>(maxSortedSize + 1);
        this.productGroups = new HashMap<>(maxSortedSize + 1);
    }

    @Override
    public List<Item> sort(final List<Item> incomingItems) {

        List<Item> items = new ArrayList<>(incomingItems);

        if (logger.isInfoEnabled()) {
            logger.info("input for {} : {}", this, items.stream().map(i -> i.toString()).collect(Collectors.joining(",")));
        }


        items.forEach(i -> {
            int indexInsertion = Collections.binarySearch(sorted, i, itemComparator);
            if (indexInsertion < 0) {
                indexInsertion = -(indexInsertion + 1);
                if (indexInsertion == maxSortedSize) {
                    return;
                }
            }
            int lastIndex = lastIndexToBeErasedWithShift(i);

            if (lastIndex >= 0) {
                if (lastIndex == sorted.size()) {
                    sorted.add(null);
                }
                for (int j = lastIndex - 1; j >= indexInsertion; j--) {
                    sorted.set(j + 1, sorted.get(j));
                }

                if (sorted.size() == maxSortedSize + 1) {
                    Item removed = sorted.remove(sorted.size() - 1);

                    List<Item> group = productGroups.get(removed.getProductId());
                    if (group.size() > 1) {
                        group.remove(group.size() - 1);
                    } else {
                        productGroups.remove(removed.getProductId());
                    }

                }
                sorted.set(indexInsertion, i);
            }
        });
        if (logger.isInfoEnabled()) {
            logger.info("input for {} : {}", this, sorted.stream().map(i -> i.toString()).collect(Collectors.joining(",")));
        }
        return sorted;
    }

    /**
     * Return an index of the right limit of shifted range (exclusive).
     *
     * @param item an element to be inserted into the sorted
     * @return index >= 0 if there is a place for the item in the array
     * -1 if there is no place for the item (it is bigger than other items of the group
     * and the group size's reached the maximum)
     */
    private int lastIndexToBeErasedWithShift(Item item) {
        List<Item> group = productGroups.computeIfAbsent(item.getProductId(), k -> new ArrayList<>(maxGroupSize + 1));
        int index = Collections.binarySearch(group, item, itemComparator);
        if (index < 0) {
            index = -(index + 1);
        }

        if (index == group.size()) {
            if (group.size() != maxGroupSize) {
                group.add(item);
            } else {
                return -1;
            }
        } else {
            group.add(index, item);
        }

        if (group.size() == maxGroupSize + 1) {
            Item removed = group.remove(maxGroupSize);
            return Collections.binarySearch(sorted, removed, itemComparator);
        } else {
            return sorted.size();
        }
    }
}
