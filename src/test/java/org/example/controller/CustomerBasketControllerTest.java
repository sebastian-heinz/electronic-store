package org.example.controller;


import org.example.discount.DiscountDealType;
import org.example.entity.BasketItem;
import org.example.entity.Product;
import org.example.persistence.DataStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CustomerBasketController.class)
public class CustomerBasketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataStore dataStore;

    @BeforeEach
    void clearDataStore() {
        dataStore.clear();
    }

    private static final String CustomerId = UUID.randomUUID().toString();

    @Test
    void whenAddValidItemToBasket_thenReturns200() throws Exception {
        Product productGreenShoes1 = new Product();
        productGreenShoes1.setId("green-shoes-1");
        productGreenShoes1.setName("Green Shoes");
        dataStore.storeProduct(productGreenShoes1);

        mockMvc.perform(post("/v1/basket/add")
                        .contentType("application/json")
                        .param("customerId", CustomerId)
                        .param("productId", "green-shoes-1")
                )
                .andExpect(status().isOk());
    }

    @Test
    void whenAddInvalidItemToBasket_thenReturns500() throws Exception {
        mockMvc.perform(post("/v1/basket/add")
                        .contentType("application/json")
                        .param("customerId", CustomerId)
                        .param("productId", "green-shoes-2")
                )
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenRemoveValidItemFromBasket_thenReturns200() throws Exception {
        Product productGreenShoes1 = new Product();
        productGreenShoes1.setId("green-shoes-1");
        productGreenShoes1.setName("Green Shoes");
        dataStore.storeProduct(productGreenShoes1);

        mockMvc.perform(post("/v1/basket/add")
                        .contentType("application/json")
                        .param("customerId", CustomerId)
                        .param("productId", "green-shoes-1")
                )
                .andExpect(status().isOk());

        mockMvc.perform(delete("/v1/basket/remove")
                        .contentType("application/json")
                        .param("customerId", CustomerId)
                        .param("productId", "green-shoes-1")
                )
                .andExpect(status().isOk());
    }

    @Test
    void whenRemoveInvalidItemFromBasket_thenReturns500() throws Exception {

        mockMvc.perform(delete("/v1/basket/remove")
                        .contentType("application/json")
                        .param("customerId", CustomerId)
                        .param("productId", "green-shoes-1")
                )
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCheckoutMultipleItemsWithMultipleDiscount_thenReturns200() throws Exception {
        Product productGreenShoes1 = new Product();
        productGreenShoes1.setId("green-shoes-1");
        productGreenShoes1.setName("Green Shoes");
        productGreenShoes1.setPrice(new BigDecimal(66));
        dataStore.storeProduct(productGreenShoes1);

        Product productRedShoes1 = new Product();
        productRedShoes1.setId("red-shoes-1");
        productRedShoes1.setName("Red Shoes");
        productRedShoes1.setPrice(new BigDecimal(100));
        dataStore.storeProduct(productRedShoes1);

        List<BasketItem> basketItems = new ArrayList<>();
        basketItems.add(new BasketItem("green-shoes-1", 3));
        basketItems.add(new BasketItem("red-shoes-1", 10));
        dataStore.updateBasketItems(CustomerId, basketItems);

        dataStore.addDiscountDeal("green-shoes-1", DiscountDealType.BuyOne_Get50Off);
        dataStore.addDiscountDeal("red-shoes-1", DiscountDealType.BuyTen_GetOne);


        mockMvc.perform(get("/v1/basket/checkout")
                        .param("customerId", CustomerId)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Product, Amount, Item Price, Total Price\r\n" +
                        "# --- --- --- --- ----\r\n" +
                        "Green Shoes, 3, 66, 198, \r\n" +
                        "Green Shoes Deal (buy 1, get 50% off next), 1, -33, -33, \r\n" +
                        "Red Shoes, 10, 100, 1000, \r\n" +
                        "Red Shoes Deal (buy 10, get one), 1, 0, 0, \r\n" +
                        "# --- --- --- --- ----\r\n" +
                        "# Total: 1165\r\n"));
    }

    @Test
    void whenCheckoutTextMultipleItemsWithDiscount_thenReturns200() throws Exception {
        Product productGreenShoes1 = new Product();
        productGreenShoes1.setId("green-shoes-1");
        productGreenShoes1.setName("Green Shoes");
        productGreenShoes1.setPrice(new BigDecimal(66));
        dataStore.storeProduct(productGreenShoes1);

        Product productRedShoes1 = new Product();
        productRedShoes1.setId("red-shoes-1");
        productRedShoes1.setName("Red Shoes");
        productRedShoes1.setPrice(new BigDecimal(100));
        dataStore.storeProduct(productRedShoes1);

        List<BasketItem> basketItems = new ArrayList<>();
        basketItems.add(new BasketItem("green-shoes-1", 2));
        basketItems.add(new BasketItem("red-shoes-1", 1));
        dataStore.updateBasketItems(CustomerId, basketItems);

        dataStore.addDiscountDeal("green-shoes-1", DiscountDealType.BuyOne_Get50Off);


        mockMvc.perform(get("/v1/basket/checkout")
                        .param("customerId", CustomerId)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Product, Amount, Item Price, Total Price\r\n" +
                        "# --- --- --- --- ----\r\n" +
                        "Green Shoes, 2, 66, 132, \r\n" +
                        "Green Shoes Deal (buy 1, get 50% off next), 1, -33, -33, \r\n" +
                        "Red Shoes, 1, 100, 100, \r\n" +
                        "# --- --- --- --- ----\r\n" +
                        "# Total: 199\r\n"));
    }
}
