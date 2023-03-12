package com.smartrix2122soa2122j.smartrix.electricmeterregister.entity;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.data.annotation.Id;

@JsonIgnoreProperties(ignoreUnknown=true)
public class CustomerLocalProduction {


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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerLocalProduction that = (CustomerLocalProduction) o;

        if (!id.equals(that.id)) return false;
        if (!quantity.equals(that.quantity)) return false;
        if (!moment.equals(that.moment)) return false;
        if (customerId != null ? !customerId.equals(that.customerId) : that.customerId != null) return false;
        return region != null ? region.equals(that.region) : that.region == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (moment != null ? moment.hashCode() : 0);
        result = 31 * result + (customerId != null ? customerId.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
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
