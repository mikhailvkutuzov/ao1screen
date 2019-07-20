package com.ao1.data;

import java.math.BigDecimal;

/**
 * Such {@link Item}s are used for sorting operations.
 */
public class ItemToBeSorted extends Item implements Comparable<ItemToBeSorted> {
    private BigDecimal price;

    public ItemToBeSorted() {
    }

    public ItemToBeSorted(Item item, BigDecimal price) {
        super(item.getProductId(), item.getName(), item.getCondition(), item.getState());
        this.price = price;
    }

    public ItemToBeSorted(int productId, String name, String condition, String state, BigDecimal price) {
        super(productId, name, condition, state);
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public int compareTo(ItemToBeSorted o) {
        int result = price.compareTo(o.getPrice());

        if (result != 0) {
            return result;
        } else {
            if (getProductId() > o.getProductId()) {
                return 1;
            } else {
                if (getProductId() < o.getProductId()) {
                    return -1;
                } else {
                    result = getName().compareTo(o.getName());
                    if (result != 0) {
                        return result;
                    } else {
                        result = getCondition().compareTo(o.getCondition());
                        if (result != 0) {
                            return result;
                        } else {
                            return getState().compareTo(o.getState());
                        }
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return "ItemToBeSorted{" +
                "price=" + price +
                "} " + super.toString();
    }

}
