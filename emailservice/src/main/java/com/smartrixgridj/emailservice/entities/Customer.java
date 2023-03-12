package com.smartrixgridj.emailservice.entities;

import java.time.LocalDateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.data.annotation.Id;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Customer {

    @Email
    private String email;
    @NotBlank
    private String region;

    
    @NotBlank
    private String name;
    private LocalDateTime subscriptionDateTime;
    @Id
    private String id;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
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

    @Override
    public String toString() {
        return "Customer [email=" + email + ", name=" + name + ", region=" + region + "]";
    }

}
