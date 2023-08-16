package org.example.persistence.javaobject;

import org.example.discount.DiscountDealType;
import org.example.entity.BasketItem;
import org.example.entity.Product;
import org.example.persistence.DataStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Provides storage capabilities for data entities.
 * Storage emulates instance isolation by returning new instance(s) per call.
 */
public class JavaObjectDataStore implements DataStore {

    private final HashMap<String, Product> products;
    private final HashMap<String, List<BasketItem>> basketItems;
    private final HashMap<String, List<DiscountDealType>> deals;

    private final Lock productLock;
    private final Lock basketItemsLock;
    private final Lock dealsLock;

    public JavaObjectDataStore() {
        products = new HashMap<>();
        basketItems = new HashMap<>();
        deals = new HashMap<>();
        productLock = new ReentrantLock();
        basketItemsLock = new ReentrantLock();
        dealsLock = new ReentrantLock();
    }

    @Override
    public void clear() {
        productLock.lock();
        try {
            products.clear();
        } finally {
            productLock.unlock();
        }

        basketItemsLock.lock();
        try {
            basketItems.clear();
        } finally {
            basketItemsLock.unlock();
        }

        dealsLock.lock();
        try {
            deals.clear();
        } finally {
            dealsLock.unlock();
        }
    }

    @Override
    public Optional<Product> getProduct(String productId) {
        productLock.lock();
        try {
            if (!products.containsKey(productId)) {
                return Optional.empty();
            }
            return Optional.of(new Product(products.get(productId)));
        } finally {
            productLock.unlock();
        }
    }

    @Override
    public boolean storeProduct(Product product) {
        productLock.lock();
        try {
            if (product == null) {
                return false;
            }
            if (products.containsKey(product.getId())) {
                return false;
            }

            products.put(product.getId(), new Product(product));
            return true;
        } finally {
            productLock.unlock();
        }
    }

    @Override
    public boolean deleteProduct(String productId) {
        productLock.lock();
        try {
            if (productId == null) {
                return false;
            }
            if (!products.containsKey(productId)) {
                return false;
            }
            products.remove(productId);
            return true;
        } finally {
            productLock.unlock();
        }
    }

    @Override
    public Optional<List<BasketItem>> getBasketItems(String customerId) {
        basketItemsLock.lock();
        try {
            List<BasketItem> basketItems;
            if (this.basketItems.containsKey(customerId)) {
                basketItems = this.basketItems.get(customerId);
            } else {
                basketItems = new ArrayList<>();
                this.basketItems.put(customerId, basketItems);
            }

            List<BasketItem> items = new ArrayList<>();
            for (BasketItem item : basketItems) {
                items.add(new BasketItem(item));
            }
            return Optional.of(items);
        } finally {
            basketItemsLock.unlock();
        }
    }

    @Override
    public boolean updateBasketItems(String customerId, List<BasketItem> basketItems) {
        basketItemsLock.lock();
        try {
            this.basketItems.put(customerId, basketItems);
            return true;
        } finally {
            basketItemsLock.unlock();
        }
    }

    @Override
    public Optional<List<DiscountDealType>> getDiscountDeals(String productId) {
        dealsLock.lock();
        try {
            if (!deals.containsKey(productId)) {
                return Optional.empty();
            }
            return Optional.of(new ArrayList<>(deals.get(productId)));
        } finally {
            dealsLock.unlock();
        }
    }

    @Override
    public boolean addDiscountDeal(String productId, DiscountDealType dealType) {
        dealsLock.lock();
        try {
            List<DiscountDealType> discountDeals;
            if (deals.containsKey(productId)) {
                discountDeals = deals.get(productId);
            } else {
                discountDeals = new ArrayList<>();
                deals.put(productId, discountDeals);
            }
            if (discountDeals.contains(dealType)) {
                return false;
            }
            discountDeals.add(dealType);
            return true;
        } finally {
            dealsLock.unlock();
        }
    }

}
