package org.example.entity;

import java.util.List;

/**
 * temporary class to aid in maintaining customers basket items
 */
public class Basket {

    private final String customerId;
    private final List<BasketItem> items;

    public Basket(String customerId, List<BasketItem> items) {
        this.customerId = customerId;
        this.items = items;
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<BasketItem> getItems() {
        return items;
    }

    public boolean addProduct(String productId) {
        BasketItem basketItem = null;
        for (BasketItem item : items) {
            if (item.getProductId() == null) {
                continue;
            }
            if (item.getProductId().equals(productId)) {
                basketItem = item;
                break;
            }
        }

        if (basketItem == null) {
            basketItem = new BasketItem(productId, 1);
            items.add(basketItem);
        } else {
            basketItem.incrementQuantity();
        }

        return true;
    }

    public boolean removeProductOnce(String productId) {
        BasketItem basketItem = null;
        for (BasketItem item : items) {
            if (item.getProductId() == null) {
                continue;
            }
            if (item.getProductId().equals(productId)) {
                basketItem = item;
                break;
            }
        }

        if (basketItem == null) {
            return false;
        }

        int quantity = basketItem.decrementQuantity();

        if (quantity <= 0) {
            items.remove(basketItem);
        }
        return true;
    }


}
