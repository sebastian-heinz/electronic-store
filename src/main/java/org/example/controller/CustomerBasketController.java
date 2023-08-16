package org.example.controller;

import org.example.discount.DiscountDeal;
import org.example.discount.DiscountDealFactory;
import org.example.discount.DiscountDealType;
import org.example.entity.*;
import org.example.persistence.DataStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("v1/basket")
public class CustomerBasketController {


    private final DataStore dataStore;

    public CustomerBasketController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @PostMapping("/add")
    public void addProduct(@RequestParam(value = "customerId") String customerId, @RequestParam(value = "productId") String productId) {

        Optional<Product> selectedProduct = dataStore.getProduct(productId);
        if (selectedProduct.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "product non existent");
        }

        Basket basket = getCustomerBasket(customerId);
        if (!basket.addProduct(productId)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "failed to add product to basket");
        }

        saveCustomerBasket(basket);
    }

    @DeleteMapping("/remove")
    public void removeProduct(@RequestParam(value = "customerId") String customerId, @RequestParam(value = "productId") String productId) {
        Basket basket = getCustomerBasket(customerId);

        if (!basket.removeProductOnce(productId)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "failed to remove product once from basket");
        }

        saveCustomerBasket(basket);
    }

    @GetMapping(value = "/checkout", produces = MediaType.TEXT_PLAIN_VALUE)
    public String checkoutBasketText(@RequestParam(value = "customerId") String customerId) {
        Bill bill = checkout(customerId);
        return bill.print();
    }

    private Bill checkout(String customerId) {
        Basket basket = getCustomerBasket(customerId);

        List<BillEntry> billEntries = new ArrayList<>();
        for (BasketItem basketItem : basket.getItems()) {
            Optional<Product> selectedProduct = dataStore.getProduct(basketItem.getProductId());
            if (selectedProduct.isEmpty()) {
                continue;
            }
            Product product = selectedProduct.get();
            BillEntry billEntry = new BillEntry(
                    product.getId(),
                    product.getName(),
                    basketItem.getQuantity(),
                    product.getPrice()
            );
            billEntries.add(billEntry);
        }

        List<BillEntry> finalBillEntries = new ArrayList<>();
        for (BillEntry billEntry : new ArrayList<>(billEntries)) {
            finalBillEntries.add(billEntry);
            List<DiscountDeal> productDeals = getProductDeals(billEntry.getProductId());
            for (DiscountDeal productDeal : productDeals) {
                Optional<BillEntry> potentialDiscountEntry = productDeal.apply(billEntry, billEntries);
                if (potentialDiscountEntry.isEmpty()) {
                    continue;
                }
                BillEntry discountEntry = potentialDiscountEntry.get();
                finalBillEntries.add(discountEntry);
            }
        }

        return new Bill(finalBillEntries);
    }

    private List<DiscountDeal> getProductDeals(String productId) {
        List<DiscountDeal> discountDeals = new ArrayList<>();
        Optional<List<DiscountDealType>> potentialDealTypes = dataStore.getDiscountDeals(productId);
        if (potentialDealTypes.isEmpty()) {
            return discountDeals;
        }
        List<DiscountDealType> dealTypes = potentialDealTypes.get();
        if (dealTypes.isEmpty()) {
            return discountDeals;
        }
        for (DiscountDealType dealType : dealTypes) {
            DiscountDeal discountDeal = DiscountDealFactory.create(dealType);
            discountDeals.add(discountDeal);
        }
        return discountDeals;
    }

    private Basket getCustomerBasket(String customerId) {
        Optional<List<BasketItem>> customerBasketItems = dataStore.getBasketItems(customerId);
        if (customerBasketItems.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "customer non existent");
        }
        List<BasketItem> basketItems = customerBasketItems.get();

        return new Basket(customerId, basketItems);
    }

    private void saveCustomerBasket(Basket basket) {
        if (!dataStore.updateBasketItems(basket.getCustomerId(), basket.getItems())) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "failed save basket");
        }
    }


}

