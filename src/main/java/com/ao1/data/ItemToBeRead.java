package com.ao1.data;

public class ItemToBeRead extends Item {
    private String price;

    public ItemToBeRead() {
    }

    public ItemToBeRead(int productId, String name, String condition, String state, String price) {
        super(productId, name, condition, state);
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
