package com.ao1.data;

public class Item {
    private int productId;
    private String name;
    private String condition;
    private String state;

    public Item() {
    }

    public Item(int productId, String name, String condition, String state) {
        this.productId = productId;
        this.name = name;
        this.condition = condition;
        this.state = state;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Item{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", condition='" + condition + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
