package com.example.spring_boot_automated_testing;

import com.example.spring_boot_automated_testing.exception.OrderNotFoundException;
import com.example.spring_boot_automated_testing.model.Order;
import com.example.spring_boot_automated_testing.repository.OrderRepository;
import com.example.spring_boot_automated_testing.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class OrderServiceUnitTest {

    //create a mock object for a repository layer
    @Mock
    OrderRepository orderRepository;

    //inject this mock into service layer
    @InjectMocks
    OrderService orderService;

    @Test
    public void testGetOrderService(){
        Order order1 = new Order(1L,"test-buyer-1",30.0,4);
        Order order2 = new Order(2L,"test-buyer-2",10.0,60);
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1,order2));

        List<Order> orderList = orderService.getOrders();
        assertEquals(orderList.size(),2);
        assertEquals(orderList.get(0).getBuyer(),"test-buyer-1");
        assertEquals(orderList.get(1).getQty(),60);
    }

    @Test
    public void testGetOrderByIdService(){
        Order order = new Order(8L,"test-buyer",90.0,2);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        Order orderById = orderService.getOrderById(order.getId());
        assertNotEquals(orderById,null);
        assertEquals(orderById.getBuyer(),"test-buyer");

    }

    @Test
    public void testGetInvalidOrderById() {
        when(orderRepository.findById(17L)).thenThrow(new OrderNotFoundException("Order Not Found with ID"));
        Exception exception = assertThrows(OrderNotFoundException.class, () -> {
            orderService.getOrderById(17L);
        });
        assertTrue(exception.getMessage().contains("Order Not Found with ID"));
    }

    @Test
    public void testCreateOrder() {
        Order order = new Order(12L, "anna", 90.0, 6);
        orderService.createOrder(order);

        //This line may verify that the method is called only once!
        verify(orderRepository, times(1)).save(order);

        //to capture arguments for mocked method.
        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderArgumentCaptor.capture());

        //take the captor value out of it and compared with the actual value.
        Order orderCreated = orderArgumentCaptor.getValue();
        assertNotNull(orderCreated.getId());
        assertEquals("anna", orderCreated.getBuyer());
    }

    @Test
    public void testDeleteOrder() {
        Order order = new Order(13L, "simen", 120.0, 10);
        when(orderRepository.findById(13L)).thenReturn(Optional.of(order));

        orderService.deleteOrderById(order.getId());

        verify(orderRepository, times(1)).deleteById(order.getId());
        ArgumentCaptor<Long> orderArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        verify(orderRepository).deleteById(orderArgumentCaptor.capture());

        Long orderIdDeleted = orderArgumentCaptor.getValue();
        assertNotNull(orderIdDeleted);
        assertEquals(13L, orderIdDeleted);
    }

}

