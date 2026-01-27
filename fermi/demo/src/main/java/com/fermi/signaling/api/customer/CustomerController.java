package com.fermi.signaling.api.customer;

import com.fermi.signaling.application.customer.CustomerService;
import com.fermi.signaling.api.customer.dto.CreateCustomerRequest;
import com.fermi.signaling.domain.customer.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<Void> createCustomer(@RequestBody CreateCustomerRequest request) {
        Customer customer = customerService.createCustomer(request.getName(), request.getPhone(), request.getSsn());
        return ResponseEntity.created(URI.create("/api/v1/customers/" + customer.getId())).build();
    }
}