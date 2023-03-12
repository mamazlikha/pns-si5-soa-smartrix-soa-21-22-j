package com.smartrix2122soa2122j.smartrix.entity;

import javax.validation.constraints.NotBlank;

public class BankAccount {

    @NotBlank
    private String iban;
    @NotBlank
    private String swiftCode;
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

    
}
