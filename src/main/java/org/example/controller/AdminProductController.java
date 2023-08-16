package org.example.controller;

import org.example.entity.Product;
import org.example.persistence.DataStore;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("v1/admin/product")
public class AdminProductController {

    private final DataStore dataStore;

    public AdminProductController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @PostMapping("/create")
    public Product createProduct(@RequestBody Product product) {
        if (!dataStore.storeProduct(product)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "failed to store product");
        }
        return product;
    }

    @DeleteMapping("/delete")
    public void deleteProduct(@RequestParam(value = "productId") String productId) {
        if (!dataStore.deleteProduct(productId)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "failed to delete product");
        }
    }

}

