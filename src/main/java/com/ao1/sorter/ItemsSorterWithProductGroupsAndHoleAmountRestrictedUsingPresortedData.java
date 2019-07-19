package com.ao1.sorter;

import com.ao1.data.ItemToBeRead;
import com.ao1.data.ItemToBeSorted;

import java.util.*;

/**
 * Take previously sorted items and sort the new ones together with them.
 * Satisfy the constraints :
 * 0. no more than maxGroupSize items of the same  product type should be presented
 * 1. no more than maxSortedSize items should be presented at all
 */
public class ItemsSorterWithProductGroupsAndHoleAmountRestrictedUsingPresortedData implements ItemsSorter {

    int maxGroupSize;
    int maxSortedSize;

    private ArrayList<ItemToBeSorted> sorted;
    private Map<Integer, List<ItemToBeSorted>> productGroups;

    public ItemsSorterWithProductGroupsAndHoleAmountRestrictedUsingPresortedData(int maxGroupSize, int maxSortedSize) {
        this.maxGroupSize = maxGroupSize;
        this.maxSortedSize = maxSortedSize;
        this.sorted = new ArrayList<>(maxSortedSize + 1);
        this.productGroups = new HashMap<>(maxSortedSize + 1);
    }

    @Override
    public List<ItemToBeSorted> sort(List<ItemToBeRead> items) {
        items.stream()
                .map(i -> new ItemToBeSorted(i))
                .forEach(i -> {
                    int indexInsertion = Collections.binarySearch(sorted, i);
                    if (indexInsertion < 0) {
                        indexInsertion = -(indexInsertion + 1);
                        if (indexInsertion == maxSortedSize) {
                            return;
                        }
                    }
                    int lastIndex = lastIndexToBeErasedWithShift(i);

                    if(lastIndex >= 0) {
                        if (lastIndex == sorted.size()) {
                            sorted.add(null);
                        }
                        for (int j = lastIndex - 1; j >= indexInsertion; j--) {
                            sorted.set(j + 1, sorted.get(j));
                        }

                        if (sorted.size() == maxSortedSize + 1) {
                            ItemToBeSorted removed = sorted.remove(sorted.size() - 1);

                            List<ItemToBeSorted> group = productGroups.get(removed.getProductId());
                            if(group.size() > 1) {
                                group.remove(group.size() - 1);
                            } else {
                                productGroups.remove(removed.getProductId());
                            }

                        }
                        sorted.set(indexInsertion, i);
                    }
                });
        return sorted;
    }

    /**
     * Return an index of the right limit of shifted range (exclusive).
     * @param item an element to be inserted into the sorted
     * @return index >= 0 if there is a place for the item in the array
     *               -1 if there is no place for the item (it is bigger than other items of the group
     *         and the group size's reached the maximum)
     */
    private int lastIndexToBeErasedWithShift(ItemToBeSorted item) {
        List<ItemToBeSorted> group = productGroups.computeIfAbsent(item.getProductId(), k -> new ArrayList<>(maxGroupSize + 1));
        int index = Collections.binarySearch(group, item);
        if (index < 0) {
            index = -(index + 1);
        }

        if (index == group.size()) {
            if(group.size() != maxGroupSize) {
                group.add(item);
            } else {
                return -1;
            }
        } else {
            group.add(index, item);
        }

        if (group.size() == maxGroupSize + 1) {
            ItemToBeSorted removed = group.remove(maxGroupSize);
            return Collections.binarySearch(sorted, removed);
        } else {
            return sorted.size();
        }
    }
}
