package com.ao1.data;

import org.csveed.annotations.CsvCell;
import org.csveed.annotations.CsvFile;
import org.csveed.annotations.CsvIgnore;
import org.csveed.bean.ColumnNameMapper;

import java.math.BigDecimal;

@CsvFile(mappingStrategy = ColumnNameMapper.class)
public class Item {
    @CsvCell
    private int productId;
    @CsvCell
    private String name;
    @CsvCell
    private String condition;
    @CsvCell
    private String state;
    @CsvCell
    private String price;
    @CsvIgnore
    private BigDecimal decimalPrice;

    public Item() {
    }

    public Item(int productId, String name, String condition, String state, String price) {
        this.productId = productId;
        this.name = name;
        this.condition = condition;
        this.state = state;
        this.price = price;
    }


    public Item(int productId, String name, String condition, String state, BigDecimal decimalPrice) {
        this(productId, name, condition, state, "");
        this.decimalPrice = decimalPrice;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public BigDecimal getDecimalPrice() {
        return decimalPrice;
    }

    public void setDecimalPrice(BigDecimal decimalPrice) {
        this.decimalPrice = decimalPrice;
    }

    @Override
    public String toString() {
        return "Item{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", condition='" + condition + '\'' +
                ", state='" + state + '\'' +
                ", price='" + price + '\'' +
                ", decimalPrice=" + decimalPrice +
                '}';
    }
}
