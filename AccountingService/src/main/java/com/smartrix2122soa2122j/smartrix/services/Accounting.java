package com.smartrix2122soa2122j.smartrix.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.YearMonth;
import java.util.logging.Logger;

public class Accounting implements IAccounting {

    @Value("${topic.exchange.name}")
    private String topicExchangeName ;


    @Autowired
    private RabbitTemplate rabbitTemplate;


    private ObjectMapper mapper = new ObjectMapper();

    private final Logger logger= Logger.getLogger(Accounting.class.getName());

    public void getConsumptionsOfMonth(int year,int month) {

        YearMonth yearMonth = YearMonth.of(year,month);

        try {
            logger.info("Asking Measures months consuptions ...");
            rabbitTemplate.convertAndSend(topicExchangeName, "smartrix.measure.asking-all-measure-in-month", mapper.writeValueAsString(yearMonth));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {

            logger.info("Measures months consuptions succefully, waiting for response");
        }
    }


}
