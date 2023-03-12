package com.smartrix2122soa2122j.smartrix.components;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartrix2122soa2122j.smartrix.entity.BankDetails;
import com.smartrix2122soa2122j.smartrix.entity.BankDetails.BankDetailsDTO;
import com.smartrix2122soa2122j.smartrix.repositories.BankDetailsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

@Component
public class RegisterBankDetails {

    private ObjectMapper objectMapper;
    private BankDetailsRepository bankDetailsRepository;

    private Logger logger=Logger.getLogger(RegisterBankDetails.class.getName());



    @Autowired
    public RegisterBankDetails(Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder, BankDetailsRepository bankDetailsRepository) {
        this.objectMapper = jackson2ObjectMapperBuilder.build();
        this.bankDetailsRepository=bankDetailsRepository;
    }

    public void dupCustomer(String customerStr) {
        logger.info("Receive information of customer " +customerStr);
        try {
            var customerDTO = this.objectMapper.readValue(customerStr, CustomerDTO.class);

            var bankDetails=new BankDetails( );
            bankDetails.setCustomerId(customerDTO.id);
            bankDetails.setIban(customerDTO.bankAccount.iban());
            bankDetails.setSwiftCode(customerDTO.bankAccount.swiftCode());
            
            bankDetails=this.bankDetailsRepository.save(bankDetails);
            logger.info("Save bankDetails of user" +bankDetails);
        } catch (JsonProcessingException e) {
            e.printStackTrace();

        }
    }

    /**
     * CustomerDTO
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CustomerDTO(String id,String name, String region, String type, String email, BankDetailsDTO bankAccount) {
    }

}
