package org.prgms.kdt.customer.controller;

import org.prgms.kdt.customer.model.Customer;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerDto(
        UUID customerId,
        String name,
        String email,
        LocalDateTime lastLoginAt,
        LocalDateTime createdAt
) {
    static CustomerDto of(Customer customer){
        return new CustomerDto(customer.getCustomerId(),
                customer.getName(),
                customer.getEmail(),
                customer.getLastLoginAt(),
                customer.getCreatedAt());
    }
}
