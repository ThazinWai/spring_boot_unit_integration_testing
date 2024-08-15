package com.example.spring_boot_automated_testing;

import com.example.spring_boot_automated_testing.model.Order;

import com.example.spring_boot_automated_testing.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class OrderControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    private Order order;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        order = new Order(6L, "testing-buyer", 50.0, 6);
    }

    //test for getAllPosts
    @Test
    public void testForGetAllOrders() throws Exception {
        when(orderService.getOrders()).thenReturn(Collections.singletonList(order));
        mockMvc.perform(get("http://localhost8080/api/orders"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$").isArray());
    }

    //test for getOrderById
    @Test
    public void testGetOrderById() throws Exception {
        when(orderService.getOrderById(6L)).thenReturn(order);
        mockMvc.perform(get("http://localhost:8080/api/orders/{id}",6L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.buyer", is("testing-buyer")))
                .andExpect(jsonPath("$.id", is(6)))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    //test for create order
    @Test
    public void testCreateOrder() throws Exception{
        when(orderService.createOrder(order)).thenReturn(order);
        mockMvc.perform(post("http://localhost:8080/api/orders")
                .content(objectMapper.writeValueAsString(order))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.buyer", is("testing-buyer")))
                .andExpect(jsonPath("$.id", is(6)))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    //test for delete order
    @Test
    public void testDeleteOrder() throws Exception{
        when(orderService.deleteOrderById(order.getId())).thenReturn(true);
        mockMvc.perform(delete("http://localhost:8080/api//orders/"+ order.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
