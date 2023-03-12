package com.smartrix2122soa2122j.smartrix.entity;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;

public class BankDetails {
    @Id
    private String id;


    @NotBlank
    private String iban;
    @NotBlank
    private String swiftCode;

    private String customerId;

    
    @Override
    public String toString() {
        return "BankDetails [customerId=" + customerId + ", iban=" + iban + ", id=" + id + ", swiftCode=" + swiftCode
                + "]";
    }
    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public String getIban() {
        return iban;
    }
    public void setIban(String iban) {
        this.iban = iban;
    }
    public String getSwiftCode() {
        return swiftCode;
    }
    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }


    /**
     * BankDetailsDTO
     */
    public record BankDetailsDTO(String iban, String swiftCode) {
    }
    
}
