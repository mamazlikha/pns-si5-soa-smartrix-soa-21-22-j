package com.smartrix2122soa2122j.smartrix.energymonitorservice.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.Measure;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.repository.MeasureRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest

public class MeasureRepositoryTest {

    @Autowired 
    private MeasureRepository measureRepository;

    @BeforeEach
    public void setUp(){

        this.measureRepository.deleteAll();

        var m1=new Measure();
        m1.setCustomerId("azerty");
        m1.setEnergyUsed(12);
       
        m1.setTimestamp(LocalDateTime.of(2021, 9, 11, 8, 3, 6));


        var m2=new Measure();

        m2.setCustomerId("qwerty");
        m2.setEnergyUsed(25);
        
        m2.setTimestamp(LocalDateTime.of(2021, 9, 12, 4, 3, 6));

        var m3=new Measure();

        m3.setCustomerId("azerty");
        m3.setEnergyUsed(77);
        
        m3.setTimestamp(LocalDateTime.of(2021, 9, 17, 5, 33, 6));

        var m4=new Measure();

        m4.setCustomerId("qwerty");
        m4.setEnergyUsed(77);
        
        m4.setTimestamp(LocalDateTime.of(2021, 9, 24, 5, 33, 6));

        this.measureRepository.saveAll(List.of(m1, m2, m3, m4));
        
    }

    @Test
    public void findByTimestampBetweenAndByCustomerIdTest(){
        var count = this.measureRepository.findByTimestampBetween(LocalDateTime.of(2021, 9, 1, 1, 0, 0), LocalDateTime.of(2021, 9, 15, 1, 0, 0)).size();


        assertEquals(2, count);


        count =this.measureRepository.findByTimestampBetweenAndCustomerId(LocalDateTime.of(2021, 9, 1, 1, 0, 0), LocalDateTime.of(2021, 9, 15, 1, 0, 0), "azerty").size();

        assertEquals(1, count);
    }
    
}
