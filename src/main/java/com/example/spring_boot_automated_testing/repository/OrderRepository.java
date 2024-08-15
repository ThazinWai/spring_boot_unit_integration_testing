package com.example.spring_boot_automated_testing.repository;

import com.example.spring_boot_automated_testing.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}