package com.smartrix2122soa2122j.smartrix.energymonitorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EnergyMonitorServiceApplication /*implements ApplicationRunner */{

	/*@Autowired

	private MeasureRepository measureRepository;*/

	public static void main(String[] args) {
		SpringApplication.run(EnergyMonitorServiceApplication.class, args);

	}

	/*@Override
	public void run(ApplicationArguments args) throws Exception {

		var response=this.measureRepository.sumCustomerConsumptionBetween(LocalDateTime.of(2020, 11, 10, 1, 35, 0),
				LocalDateTime.of(2020, 12, 24, 20, 59, 50));
				
				
				for(var r:response){
					System.out.println(r);
				}

	}*/

}
