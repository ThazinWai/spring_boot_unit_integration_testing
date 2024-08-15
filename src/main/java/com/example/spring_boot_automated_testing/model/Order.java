package com.example.spring_boot_automated_testing.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.Objects;

@Getter
@Setter
@Builder
@ToString
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String buyer;
    Double price;
    int qty;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return qty == order.qty && id.equals(order.id) && buyer.equals(order.buyer) && price.equals(order.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, buyer, price, qty);
    }
}
