package org.example.entity;

import java.math.BigDecimal;

/**
 * represents a product
 */
public class Product {

    private String id;
    private String name;
    private BigDecimal price;

    public Product(Product other) {
        this.id = other.getId();
        this.name = other.getName();
        this.price = other.getPrice();
    }

    public Product() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
