package com.ao1.data;

import java.math.BigDecimal;

public class ItemToBeSorted extends Item implements Comparable<ItemToBeSorted> {
    private BigDecimal price;

    public ItemToBeSorted() {
    }

    public ItemToBeSorted(ItemToBeRead read) {
        setCondition(read.getCondition());
        setName(read.getName());
        setProductId(read.getProductId());
        setState(read.getState());
        setPrice(new BigDecimal(read.getPrice()));
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
