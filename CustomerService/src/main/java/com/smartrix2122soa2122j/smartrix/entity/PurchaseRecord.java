package com.smartrix2122soa2122j.smartrix.entity;

public class PurchaseRecord {
    
    private final Customer customer;
    private final int quantity;

    
    public PurchaseRecord(Customer customer, int quantity) {
        this.customer = customer;
        this.quantity = quantity;
    }

    public Customer getCustomer() {
        return customer;
    }
  
    public int getQuantity() {
        return quantity;
    }
    

    
}
