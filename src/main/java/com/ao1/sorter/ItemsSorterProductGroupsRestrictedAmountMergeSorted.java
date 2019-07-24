package com.ao1.sorter;

import com.ao1.data.Item;

import java.util.*;


/**
 * This realisation of {@link ItemsSorter} uses sorted data from the previous call and merge them with
 * sorted data of the current call due to one comparator used to sort both of lists we may merge these lists
 * with O((maxSortedSize + items.size())*log(maxSortedSize)) which leads us eventually to O(n*log(maxSortedSize))
 * where n is an amount of Items handed on into this class.
 *
 * It also does not allow to have more than maxProductSize of any Item with the same productId
 * as well as restricts an amount of data returned by sort(List<Item>) operation with maxSortedSize.
 */
public class ItemsSorterProductGroupsRestrictedAmountMergeSorted implements ItemsSorter {

    private int maxProductSize;
    private int maxSortedSize;
    private ArrayList<Item> sorted;

    public ItemsSorterProductGroupsRestrictedAmountMergeSorted(int maxProductSize, int maxSortedSize) {
        this.maxProductSize = maxProductSize;
        this.maxSortedSize = maxSortedSize;
        this.sorted = new ArrayList<>();

    }

    @Override
    public List<Item> sort(List<Item> items) {
        Map<Integer, ProductSize> productGroups = new HashMap<>(maxSortedSize);
        items.sort(itemComparator);

        ArrayList<Item> result = new ArrayList<>(maxSortedSize);

        int sortedIndex = 0;
        Iterator<Item> itemsIterator = items.iterator();
        Item iValue = null;

        while (result.size() < maxSortedSize && itemsIterator.hasNext() && sortedIndex < sorted.size()) {
            iValue = iValue == null ? itemsIterator.next() : iValue;

            Item sValue = sorted.get(sortedIndex);

            int compare = itemComparator.compare(iValue, sValue);

            if (compare <= 0) {
                if (addIntoGroupIfPossible(iValue, productGroups)) {
                    result.add(iValue);
                }
                iValue = null;
            }
            if (compare >= 0) {
                if (addIntoGroupIfPossible(sValue, productGroups)) {
                    result.add(sValue);
                }
                sortedIndex++;
            }
        }

        if (result.size() != maxSortedSize) {
            if (sortedIndex == sorted.size()) {
                if (itemsIterator.hasNext()) {
                    Item item = iValue == null ? itemsIterator.next() : iValue;
                    do {
                        if (addIntoGroupIfPossible(item, productGroups)) {
                            result.add(item);
                        }
                        if (itemsIterator.hasNext()) {
                            item = itemsIterator.next();
                        } else {
                            break;
                        }
                    } while (result.size() < maxSortedSize);
                }
            } else {
                do {
                    Item item = sorted.get(sortedIndex);
                    if (addIntoGroupIfPossible(item, productGroups)) {
                        result.add(item);
                    }
                    sortedIndex++;
                } while (result.size() < maxProductSize && sortedIndex < sorted.size());
            }
        }
        sorted = result;
        return sorted;
    }

    private boolean addIntoGroupIfPossible(Item item, Map<Integer, ProductSize> productGroups) {
        ProductSize group = productGroups.computeIfAbsent(item.getProductId(), k -> new ProductSize(item.getProductId(), 0));
        if (group.size < maxProductSize) {
            group.size++;
            return true;
        } else {
            return false;
        }
    }

    class ProductSize {
        int productId;
        int size;

        ProductSize(int productId, int size) {
            this.productId = productId;
            this.size = size;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ProductSize that = (ProductSize) o;

            if (productId != that.productId) return false;
            return size == that.size;

        }

        @Override
        public int hashCode() {
            int result = productId;
            result = 31 * result + size;
            return result;
        }
    }

}
