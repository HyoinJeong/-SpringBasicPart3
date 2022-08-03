package org.prgms.kdt.customer;

import java.util.List;

public interface CustomerService {
    void createCustomers(List<Customer>customers);
    Customer createCustomer(String email, String name);

    List<Customer> getAllCustomers();
}
