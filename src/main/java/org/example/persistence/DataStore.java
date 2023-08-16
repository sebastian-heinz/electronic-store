package org.example.persistence;

import org.example.discount.DiscountDealType;
import org.example.entity.BasketItem;
import org.example.entity.Product;

import java.util.List;
import java.util.Optional;

public interface DataStore {

    void clear();

    Optional<Product> getProduct(String productId);

    boolean storeProduct(Product product);

    boolean deleteProduct(String productId);

    Optional<List<BasketItem>> getBasketItems(String customerId);

    boolean updateBasketItems(String customerId, List<BasketItem> basketItems);

    boolean addDiscountDeal(String productId, DiscountDealType dealType);

    Optional<List<DiscountDealType>> getDiscountDeals(String productId);
}
