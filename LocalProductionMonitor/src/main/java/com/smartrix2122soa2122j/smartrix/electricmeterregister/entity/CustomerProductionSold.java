package com.smartrix2122soa2122j.smartrix.electricmeterregister.entity;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;

public class CustomerProductionSold {
    @Id
    private String id;
    @NotBlank
    private Integer quantity;
    @NotBlank
    private LocalDateTime moment;
    @NotBlank
    private String customerId;
    @NotBlank
    private String region;

    

    public CustomerProductionSold() {
    }

    public CustomerProductionSold(@NotBlank Integer quantity, @NotBlank LocalDateTime moment,
            @NotBlank String customerId, @NotBlank String region) {
        this.quantity = quantity;
        this.moment = moment;
        this.customerId = customerId;
        this.region = region;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getMoment() {
        return moment;
    }

    public void setMoment(LocalDateTime moment) {
        this.moment = moment;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CustomerProductionSold other = (CustomerProductionSold) obj;
        if (customerId == null) {
            if (other.customerId != null)
                return false;
        } else if (!customerId.equals(other.customerId))
            return false;
        if (moment == null) {
            if (other.moment != null)
                return false;
        } else if (!moment.equals(other.moment))
            return false;
        if (quantity == null) {
            if (other.quantity != null)
                return false;
        } else if (!quantity.equals(other.quantity))
            return false;
        if (region == null) {
            if (other.region != null)
                return false;
        } else if (!region.equals(other.region))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
        result = prime * result + ((moment == null) ? 0 : moment.hashCode());
        result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
        result = prime * result + ((region == null) ? 0 : region.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "CustomerLocalProduction{" +
                "id='" + id + '\'' +
                ", quantity=" + quantity +
                ", moment=" + moment +
                ", customerId='" + customerId + '\'' +
                ", region='" + region + '\'' +
                '}';
    }
}
