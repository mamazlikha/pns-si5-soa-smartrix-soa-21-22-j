package com.smartrix2122soa2122j.smartrix.entity;

import java.time.LocalDateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import org.springframework.data.annotation.Id;



@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME, 
  include = JsonTypeInfo.As.PROPERTY, 
  property = "type")
@JsonSubTypes({ 
  @Type(value = PrivatePerson.class, name = "PRIVATE_PERSON"), 
  @Type(value = CustomerCompany.class, name = "COMPANY") 
})
public abstract class Customer {
    @NotBlank
    private String name;
    private LocalDateTime subscriptionDateTime;
    @Id
    private String id;
    @Email
    private String email;
    
    private String type;
    @NotNull
    private BankAccount bankAccount;


    
    
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public LocalDateTime getSubscriptionDateTime() {
        return subscriptionDateTime;
    }
    public void setSubscriptionDateTime(LocalDateTime subscriptionDateTime) {
        this.subscriptionDateTime = subscriptionDateTime;
    }
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }




    public void setEmail(String email) {
        this.email = email;
    }




    public BankAccount getBankAccount() {
        return bankAccount;
    }




    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Customer other = (Customer) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    
}
