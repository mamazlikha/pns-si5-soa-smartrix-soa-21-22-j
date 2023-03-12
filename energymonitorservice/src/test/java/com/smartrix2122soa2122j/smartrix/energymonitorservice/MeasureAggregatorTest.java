package com.smartrix2122soa2122j.smartrix.energymonitorservice;

import com.smartrix2122soa2122j.smartrix.energymonitorservice.component.ClockWrapper;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.component.MeasureAggregator;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.entity.Measure;
import com.smartrix2122soa2122j.smartrix.energymonitorservice.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.*;
import java.util.Random;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
/*@Testcontainers
@ContextConfiguration(initializers = MeasureAggregatorTest.Initializer.class)*/
public class MeasureAggregatorTest {

    @Autowired
    private MeasureRepository measureRepository;
    @Autowired
    private MeasureAggregator aggregator;

    @Autowired
    private DailyAggregateRepository dailyAggregateRepository;

    @Autowired
    private DailyGlobalMeasureRepository dailyGlobalMeasureRepository;

    @Autowired
    private MonthlyCustomerMeasureRepository monthlyCustomerMeasureRepository;

    @Autowired
    private MonthlyGlobalMeasureRepository monthlyGlobalMeasureRepository;

    @Autowired
    private ClockWrapper clockWrapper;

   /* @Container
    public static GenericContainer rabbit = new RabbitMQContainer()
            .withExposedPorts(5672, 15672);

    @Container
    public static GenericContainer mongo = new GenericContainer("mongo:5.0")
            .withExposedPorts(27017);*/

    private Random random=new Random();


    @BeforeEach
   public  void setUp(){

       this.measureRepository.deleteAll();
       this.dailyAggregateRepository.deleteAll();
       this.dailyGlobalMeasureRepository.deleteAll();
       this.monthlyCustomerMeasureRepository.deleteAll();
       this.monthlyGlobalMeasureRepository.deleteAll();

    }
    @Test
   public  void aggregateDailyTest(){

        int mrjones=2500;
        int dunham=6000;
        int bishop=7000;
        int knight=3600;

        this.clockWrapper.setClock(Clock.fixed(Instant.parse("2021-10-22T01:00:00.00Z"), ZoneOffset.ofHours(0)));
        LocalDateTime startDay=LocalDateTime.of(2021, Month.OCTOBER,21,0,1);
        while (mrjones > 0){


            int used= random.nextInt(20)+1;

            used= Math.min(used, mrjones);

            mrjones-=used;
            measureRepository.save(new Measure(startDay, "mrjones", used, "A"));

            startDay=startDay.plusSeconds(used);
        }


        startDay=LocalDateTime.of(2021, Month.OCTOBER,21,0,1);
        while (dunham > 0){


            int used= random.nextInt(20)+1;

            used= Math.min(used, dunham);

            dunham-=used;
            measureRepository.save(new Measure(startDay, "dunham", used, "B"));

            startDay=startDay.plusSeconds(used);
        }


        long n=dailyAggregateRepository.count();
        long globalDailyCount=dailyGlobalMeasureRepository.count();
        aggregator.dailyAggregate();

        assertEquals(n+2, dailyAggregateRepository.count()) ;
        assertEquals(globalDailyCount+1, dailyGlobalMeasureRepository.count());



    }


    @Test
    public void monthlyComputationsTest(){
        int mrjones=2500;
        int dunham=6000;
        int bishop=7000;
        int knight=3600;


        LocalDateTime startDay=LocalDateTime.of(2021, Month.OCTOBER,1,0,1);
        LocalDateTime current=startDay;
        LocalDateTime endDay=LocalDateTime.of(2021, Month.NOVEMBER,1,0,0);
        while (mrjones > 0 && current.isBefore(endDay)){


            int used= random.nextInt(20)+1;

            used= Math.min(used, mrjones);

            mrjones-=used;
            measureRepository.save(new Measure(current, "mrjones", used, "A"));

            current=current.plusMinutes(used);
        }


        current=startDay;
        while (dunham > 0 && current.isBefore(endDay)){


            int used= random.nextInt(20)+1;

            used= Math.min(used, dunham);

            dunham-=used;
            measureRepository.save(new Measure(current, "dunham", used, "B"));

            current=current.plusMinutes(used);
        }



        long n=monthlyCustomerMeasureRepository.count();

        long m = monthlyGlobalMeasureRepository.count();

        current= LocalDateTime.of(2021, Month.OCTOBER,2,1,0);
        LocalDateTime end=LocalDateTime.of(2021,Month.NOVEMBER,2,1,0);
        while(current.isBefore(end)){
            this.clockWrapper.setClock(Clock.fixed( current.toInstant(ZoneOffset.ofHours(0)),ZoneOffset.ofHours(0) ));
            aggregator.dailyAggregate();
            current=current.plusDays(1);
        }

        this.clockWrapper.setClock(Clock.fixed(Instant.parse("2021-11-02T01:00:00.00Z"), ZoneOffset.ofHours(0)));
        aggregator.monthlyAggregate();

        assertEquals(n+2, monthlyCustomerMeasureRepository.count());

        assertEquals( m +1, monthlyGlobalMeasureRepository.count());

    }

   /* public static class Initializer implements
            ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

            System.out.println("In initialize" );
            System.out.println(rabbit.getContainerIpAddress() +" "+rabbit.getExposedPorts().get(0));
            System.out.println(mongo.getContainerIpAddress() +" "+mongo.getExposedPorts().get(0));
            var values = TestPropertyValues.of(
                    "spring.rabbitmq.host=" + rabbit.getContainerIpAddress(),
                    "spring.rabbitmq.port=" + rabbit.getExposedPorts().get(0),
                    "spring.data.mongodb.host=" + mongo.getContainerIpAddress(),
                    "spring.data.mongodb.port=" + mongo.getExposedPorts().get(0)
            );
            values.applyTo(configurableApplicationContext.getEnvironment());


        }
    }*/
}
