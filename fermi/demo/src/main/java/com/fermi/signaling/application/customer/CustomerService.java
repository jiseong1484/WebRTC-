package com.fermi.signaling.application.customer;

import com.fermi.signaling.domain.customer.Customer;
import com.fermi.signaling.domain.customer.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(String name, String phone, String ssn) {
        Customer customer = new Customer(name, phone, ssn);
        return customerRepository.save(customer);
    }
}