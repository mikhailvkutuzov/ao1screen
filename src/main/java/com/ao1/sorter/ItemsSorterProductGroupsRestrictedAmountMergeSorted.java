package com.ao1.sorter;

import com.ao1.data.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;


/**
 * This realisation of {@link ItemsSorter} uses sorted data from the previous call and merge them with
 * sorted data of the current call due to one comparator used to sort both of lists we may merge these lists
 * with O((maxReportSize + items.size())*log(maxReportSize)) which leads us eventually to O(n*log(maxReportSize))
 * where n is an amount of Items handed on into this class.
 * <p>
 * It also does not allow to have more than maxProductSize of any Item with the same productId
 * as well as restricts an amount of data returned by sort(List<Item>) operation with maxReportSize.
 */
public class ItemsSorterProductGroupsRestrictedAmountMergeSorted implements ItemsSorter {
    private static final Logger logger = LoggerFactory.getLogger(ItemsSorterProductGroupsRestrictedAmountMergeSorted.class);

    private int maxProductSize;
    private int maxReportSize;
    private ArrayList<Item> sorted;

    public ItemsSorterProductGroupsRestrictedAmountMergeSorted(int maxProductSize, int maxReportSize) {
        this.maxProductSize = maxProductSize;
        this.maxReportSize = maxReportSize;
        this.sorted = new ArrayList<>();

    }

    @Override
    public List<Item> sort(List<Item> incomingItems) {

        List<Item> items = new ArrayList<>(incomingItems);

        if (logger.isInfoEnabled()) {
            logger.info("input for {} : {}", this, items.stream().map(i -> i.toString()).collect(Collectors.joining(",")));
        }

        Map<Integer, ProductSize> productGroups = new HashMap<>(maxReportSize);
        items.sort(itemComparator);

        ArrayList<Item> result = new ArrayList<>(maxReportSize);

        int sortedIndex = 0;
        Iterator<Item> itemsIterator = items.iterator();
        Item iValue = null;

        while (result.size() < maxReportSize && itemsIterator.hasNext() && sortedIndex < sorted.size()) {
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

        if (result.size() != maxReportSize) {
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
                    } while (result.size() < maxReportSize);
                }
            } else {
                do {
                    Item item = sorted.get(sortedIndex);
                    if (addIntoGroupIfPossible(item, productGroups)) {
                        result.add(item);
                    }
                    sortedIndex++;
                } while (result.size() < maxReportSize && sortedIndex < sorted.size());
            }
        }
        sorted = result;

        if (logger.isInfoEnabled()) {
            logger.info("output from {} : {}", this, items.stream().map(i -> i.toString()).collect(Collectors.joining(",")));
        }
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
