package com.smartrix2122soa2122j.smartrix.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartrix2122soa2122j.smartrix.entity.Customer;
import com.smartrix2122soa2122j.smartrix.entity.ElectricComponent;
import com.smartrix2122soa2122j.smartrix.entity.PrivatePerson;
import com.smartrix2122soa2122j.smartrix.entity.UsageSwitch;
import com.smartrix2122soa2122j.smartrix.entity.dto.DTO;
import com.smartrix2122soa2122j.smartrix.repository.CustomerRepository;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomerCareWS  {

    @Value("${topic.exchange.name}")
    private String topicExchangeName;

    @Value("${energy.monitor.service.address}")
    private String energyMonitorService;

    @Value("${accounting.service.url}")
    private String accountingServiceUrl;

    @Value("${local.production.service.url}")
    private String localProductionServiceUrl;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final RestTemplate restTemplate;

    private final ObjectMapper mapper;
    @Autowired
    private CustomerRepository customerRepository;

    private final Logger logger = Logger.getLogger(CustomerCareWS.class.getName());

    public CustomerCareWS(RestTemplateBuilder restTemplateBuilder, Jackson2ObjectMapperBuilder objectMapperBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        this.mapper = objectMapperBuilder.build();

    }


    public ResponseEntity<DTO.OrderedConsumptionGraph> getConsumptionGraph(Customer customer, String graphType) {

        return this.restTemplate.getForEntity(energyMonitorService + "/consumptions/{customerId}/{graphType}",
                DTO.OrderedConsumptionGraph.class, customer.getId(), graphType);

    }

    public void addPrivateComponents(PrivatePerson person) {

        logger.info("Adding components for private customer");

        ElectricComponent essentialLights = new ElectricComponent();
        essentialLights.setName("Essential Lights");
        essentialLights.setCustomerId(person.getId());
        essentialLights.setCustomerName(person.getName());
        essentialLights.setRegion(person.getRegion());
        essentialLights.setEnergyUsed(215);
        essentialLights.setUsageType(UsageSwitch.ESSENTIAL.toString());

        ElectricComponent nonEssentialLights = new ElectricComponent();
        nonEssentialLights.setName("Non Essential Lights");
        nonEssentialLights.setCustomerId(person.getId());
        nonEssentialLights.setCustomerName(person.getName());
        nonEssentialLights.setRegion(person.getRegion());
        nonEssentialLights.setEnergyUsed(106);
        nonEssentialLights.setUsageType(UsageSwitch.NON_ESSENTIAL.toString());

        ElectricComponent electricCarCharger = new ElectricComponent();
        electricCarCharger.setName("Car Charger");
        electricCarCharger.setCustomerId(person.getId());
        electricCarCharger.setCustomerName(person.getName());
        electricCarCharger.setRegion(person.getRegion());
        electricCarCharger.setEnergyUsed(305);
        electricCarCharger.setUsageType(UsageSwitch.NON_ESSENTIAL.toString());

        ElectricComponent homeAppliance = new ElectricComponent();
        homeAppliance.setName("Home appliance");
        homeAppliance.setCustomerId(person.getId());
        homeAppliance.setCustomerName(person.getName());
        homeAppliance.setRegion(person.getRegion());
        homeAppliance.setEnergyUsed(97);
        homeAppliance.setUsageType(UsageSwitch.NON_ESSENTIAL.toString());

        List<ElectricComponent> electricComponents = new ArrayList<>();
        electricComponents.add(essentialLights);
        electricComponents.add(nonEssentialLights);
        electricComponents.add(electricCarCharger);
        electricComponents.add(homeAppliance);

        restTemplate.postForObject(energyMonitorService + "/addCustomerComponent",
                new DTO.AddComponentRequest(person.getId(), electricComponents), DTO.AddComponentRequest.class);

        /*
         * try { rabbitTemplate.convertAndSend(topicExchangeName,
         * "smartrix.measure.add-components", mapper.writeValueAsString(aRequest)); }
         * catch (JsonProcessingException e) { e.printStackTrace(); }
         */
    }

    public ResponseEntity<DTO.MonthlyBill> getBillForMonth(Customer customer, String year, String month) {
        return this.restTemplate.getForEntity(
                this.accountingServiceUrl + "/bills/customer/{customerId}/year/{year}/month/{month}", DTO.MonthlyBill.class,
                customer.getId(), year, month);
    }


    public boolean switchCustomerComponent(DTO.ComponentSwitchRequest componentSwitchRequest) {
        Optional<Customer> optCustomer = customerRepository.findByName(componentSwitchRequest.customerName());
        if (optCustomer.isEmpty() || componentSwitchRequest.componentName() == null
                || componentSwitchRequest.switchOn() == null) {
            return false;
        }
        Customer customer = optCustomer.get();

        restTemplate.postForObject(energyMonitorService + "/switchCustomerComponent",
                new DTO.ComponentSwitchRequest(componentSwitchRequest.customerName(), customer.getId(),
                        componentSwitchRequest.componentName(), componentSwitchRequest.switchOn()),
                DTO.ComponentSwitchRequest.class);

        return true;
    }

    public ResponseEntity<DTO.MonthlyBill> computeAmountDueForThisMonth(Customer customer) {

        return this.restTemplate.getForEntity(this.accountingServiceUrl + "/bills/customer/{customerId}/this-month",
                DTO.MonthlyBill.class, customer.getId());

    }

    public void installBattery(DTO.Battery battery){
        this.restTemplate.postForEntity(this.localProductionServiceUrl+"/customer/battery",battery, DTO.Battery.class);
    }

}