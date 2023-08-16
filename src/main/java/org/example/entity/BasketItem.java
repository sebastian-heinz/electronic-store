package org.example.entity;

/**
 * represents customers selected product and quantity
 */
public class BasketItem {

    private String productId;
    private int quantity;

    public BasketItem(BasketItem other) {
        this.productId = other.getProductId();
        this.quantity = other.getQuantity();
    }

    public BasketItem() {

    }

    public BasketItem(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int incrementQuantity() {
        return quantity++;
    }

    public int decrementQuantity() {
        quantity--;
        if (quantity < 0) {
            quantity = 0;
        }
        return quantity;
    }

}
