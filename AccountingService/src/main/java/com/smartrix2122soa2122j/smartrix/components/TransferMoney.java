package com.smartrix2122soa2122j.smartrix.components;

import java.util.Optional;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.smartrix2122soa2122j.smartrix.configurations.RemoteServiceCallCommand;
import com.smartrix2122soa2122j.smartrix.configurations.RemoteServiceCallExecutor;
import com.smartrix2122soa2122j.smartrix.entity.BankDetails;
import com.smartrix2122soa2122j.smartrix.entity.BankDetails.BankDetailsDTO;
import com.smartrix2122soa2122j.smartrix.repositories.BankDetailsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TransferMoney {

    @Value("${external.bank.api}")
    private String bankUrl;

    @Value("${smartrixgrid.iban}")
    private String smartrixgridIban;
    @Value("${smartrixgrid.swiftcode}")
    private String smartrixgridSwiftcode;

    private BankDetailsDTO smartrixGridBankDetails;

    private final Logger logger = Logger.getLogger(TransferMoney.class.getName());

    private final ObjectMapper mapper;

    private final RestTemplate restTemplate;

    @Autowired
    public TransferMoney(RestTemplateBuilder restTemplateBuilder, Jackson2ObjectMapperBuilder mapperBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        this.mapper = mapperBuilder.build();
    }

    @Autowired
    private BankDetailsRepository bankDetailsRepository;

    @Value("${topic.exchange.banq.result}")
    private String topicExchangeName;

    public void creditCustomer(String customerId, double amount) {
        Optional<BankDetails> bankDetailsOpt = this.bankDetailsRepository.findByCustomerId(customerId);

        if (bankDetailsOpt.isEmpty()) {
            logger.severe("customer with id " + customerId + " does not have bankdetails !!!");

        } else {
            var bankDetails = bankDetailsOpt.get();
            var bankDetailsDTO = new BankDetailsDTO(bankDetails.getIban(), bankDetails.getSwiftCode());
            logger.info("Transferring  " + bankDetailsDTO);
            this.transferMoney(this.smartrixGridBankDetails, bankDetailsDTO, amount);

        }
    }

    public void debitCustomer(String customerId, double amount) {
        Optional<BankDetails> bankDetailsOpt = this.bankDetailsRepository.findByCustomerId(customerId);

        if (bankDetailsOpt.isEmpty()) {
            logger.severe("customer with id " + customerId + " does not have bankdetails !!!");

        } else {
            var bankDetails = bankDetailsOpt.get();
            var bankDetailsDTO = new BankDetailsDTO(bankDetails.getIban(), bankDetails.getSwiftCode());

            this.transferMoney(bankDetailsDTO, this.smartrixGridBankDetails, amount);

        }
    }

    private void transferMoney(BankDetailsDTO from, BankDetailsDTO to, double amount) {
        try {

            HystrixCommand.Setter config = HystrixCommand.Setter
                    .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupCircuitBreaker"));
            HystrixCommandProperties.Setter properties = HystrixCommandProperties.Setter();
            properties.withExecutionIsolationThreadTimeoutInMilliseconds(1000);

            properties.withCircuitBreakerSleepWindowInMilliseconds(4000);
            properties.withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD);
            properties.withCircuitBreakerEnabled(true);
            properties.withCircuitBreakerRequestVolumeThreshold(1);
            properties.withFallbackEnabled(true);

            config.andCommandPropertiesDefaults(properties);

            config.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withMaxQueueSize(1)
                    .withCoreSize(1).withQueueSizeRejectionThreshold(1));

            ResponseEntity<Operation> operationResult = new RemoteServiceCallCommand<>(config,
                    new RemoteServiceCallExecutor<Operation>(restTemplate, bankUrl + "/transfer",
                            new Operation(from, to, amount))).execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @PostConstruct
    private void initPostConstruct() {
        this.smartrixGridBankDetails = new BankDetailsDTO(this.smartrixgridIban, this.smartrixgridSwiftcode);
    }

    /**
     * Operation BankDetailsDTO from, BankDetailsDTO to, double amount
     */
    public record Operation(BankDetailsDTO from, BankDetailsDTO to, double amount) {
    }
}
