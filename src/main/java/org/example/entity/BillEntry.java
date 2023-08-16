package org.example.entity;

import java.math.BigDecimal;

public class BillEntry {

    private final String productId;
    private final String name;
    private final int quantity;
    private final BigDecimal price;

    public BillEntry(String productId, String name, int quantity, BigDecimal price) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

}
