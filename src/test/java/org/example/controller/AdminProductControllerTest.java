package org.example.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entity.Product;
import org.example.persistence.DataStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminProductController.class)
public class AdminProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataStore dataStore;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clearDataStore() {
        dataStore.clear();
    }

    @Test
    void whenCreateValidProduct_thenReturn200() throws Exception {
        Product productGreenShoes1 = new Product();
        productGreenShoes1.setId("green-shoes-1");
        productGreenShoes1.setName("Green Shoes");
        mockMvc.perform(post("/v1/admin/product/create")
                        .contentType("application/json")
                        .param("productId", "123")
                        .content(objectMapper.writeValueAsString(productGreenShoes1))
                )
                .andExpect(status().isOk());
    }

    @Test
    void whenCreateInvalidProduct_thenReturn400() throws Exception {
        mockMvc.perform(post("/v1/admin/product/create")
                        .contentType("application/json")
                        .param("productId", "123")
                        .content(objectMapper.writeValueAsString("INVALID_PAYLOAD"))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCreateDuplicateProduct_thenReturn500() throws Exception {
        Product productGreenShoes1 = new Product();
        productGreenShoes1.setId("green-shoes-1");
        productGreenShoes1.setName("Green Shoes");
        mockMvc.perform(post("/v1/admin/product/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(productGreenShoes1))
                )
                .andExpect(status().isOk());
        mockMvc.perform(post("/v1/admin/product/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(productGreenShoes1))
                )
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenDeleteValidProduct_thenReturn200() throws Exception {
        Product productGreenShoes1 = new Product();
        productGreenShoes1.setId("green-shoes-1");
        productGreenShoes1.setName("Green Shoes");

        mockMvc.perform(post("/v1/admin/product/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(productGreenShoes1))
                )
                .andExpect(status().isOk());

        mockMvc.perform(delete("/v1/admin/product/delete")
                        .contentType("application/json")
                        .param("productId", productGreenShoes1.getId())
                )
                .andExpect(status().isOk());
    }

    @Test
    void whenDeleteInvalidProduct_thenReturn500() throws Exception {
        mockMvc.perform(delete("/v1/admin/product/delete")
                        .contentType("application/json")
                        .param("productId", "1")
                )
                .andExpect(status().isInternalServerError());
    }
}
