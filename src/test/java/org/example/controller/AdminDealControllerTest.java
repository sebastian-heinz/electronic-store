package org.example.controller;


import org.example.discount.DiscountDealType;
import org.example.entity.Product;
import org.example.persistence.DataStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminDealController.class)
public class AdminDealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataStore dataStore;

    @BeforeEach
    void clearDataStore() {
        dataStore.clear();
    }

    @Test
    void whenCreateValidDeal_thenReturns200() throws Exception {

        Product productGreenShoes1 = new Product();
        productGreenShoes1.setId("green-shoes-1");
        productGreenShoes1.setName("Green Shoes");
        dataStore.storeProduct(productGreenShoes1);

        mockMvc.perform(post("/v1/admin/deal/create")
                        .contentType("application/json")
                        .param("productId", "green-shoes-1")
                        .param("dealType", DiscountDealType.BuyOne_Get50Off.getStringValue())
                )
                .andExpect(status().isOk());
    }

    @Test
    void whenCreateInvalidDeal_thenReturns500() throws Exception {

        mockMvc.perform(post("/v1/admin/deal/create")
                        .contentType("application/json")
                        .param("productId", "green-shoes-1")
                        .param("dealType", "INVALID_DEAL_TYPE")
                )
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCreateDealForNonExistentProduct_thenReturns500() throws Exception {

        mockMvc.perform(post("/v1/admin/deal/create")
                        .contentType("application/json")
                        .param("productId", "green-shoes-1")
                        .param("dealType", DiscountDealType.BuyOne_Get50Off.getStringValue())
                )
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCreateDuplicateDeal_thenReturns500() throws Exception {
        Product productGreenShoes1 = new Product();
        productGreenShoes1.setId("green-shoes-1");
        productGreenShoes1.setName("Green Shoes");
        dataStore.storeProduct(productGreenShoes1);

        mockMvc.perform(post("/v1/admin/deal/create")
                        .contentType("application/json")
                        .param("productId", "green-shoes-1")
                        .param("dealType", DiscountDealType.BuyOne_Get50Off.getStringValue())
                )
                .andExpect(status().isOk());

        mockMvc.perform(post("/v1/admin/deal/create")
                        .contentType("application/json")
                        .param("productId", "green-shoes-1")
                        .param("dealType", DiscountDealType.BuyOne_Get50Off.getStringValue())
                )
                .andExpect(status().isInternalServerError());
    }

}
