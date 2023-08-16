package org.example.controller;

import org.example.discount.DiscountDealType;
import org.example.entity.Product;
import org.example.persistence.DataStore;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("v1/admin/deal")
public class AdminDealController {

    private final DataStore dataStore;

    public AdminDealController(DataStore dataStore) {
        this.dataStore = dataStore;
    }


    @PostMapping("/create")
    public void createDeal(@RequestParam(value = "productId") String productId, @RequestParam(value = "dealType") String dealStringType) {

        DiscountDealType dealType = DiscountDealType.getType(dealStringType);
        if (dealType == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "deal non existent");
        }

        Optional<Product> product = dataStore.getProduct(productId);
        if (product.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "product non existent");
        }

        if (!dataStore.addDiscountDeal(productId, dealType)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "failed to add deal");
        }
    }
}

