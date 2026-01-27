package com.fermi.signaling.domain.customer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;
    private String ssn;

    protected Customer() {}

    public Customer(String name, String phone, String ssn) {
        this.name = name;
        this.phone = phone;
        this.ssn = ssn;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getSsn() {
        return ssn;
    }
}