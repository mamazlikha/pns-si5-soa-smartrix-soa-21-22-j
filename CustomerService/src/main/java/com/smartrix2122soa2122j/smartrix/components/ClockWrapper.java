package com.smartrix2122soa2122j.smartrix.components;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import javax.annotation.PostConstruct;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ClockWrapper {
    
    @Value("${app.mode}")
    private String mode;

    @Value("${app.initialtime}")
    private String initialTime;



    private final Logger logger=Logger.getLogger(ClockWrapper.class.getName());
    private Clock clock;

    
    public void setClock(Clock clock){
        this.clock=clock;
    }

    public Clock getClock(){
        
        return this.clock;
    }

    @PostConstruct
    private void initializeClock(){

        
        if(mode.equals("DEMO")){
            this.clock=Clock.fixed(Instant.parse(this.initialTime), ZoneOffset.ofHours(0));
            this.logger.info("---------\n"+this.clock.instant()+"\n---------\n");
        }
        else{
            this.clock=Clock.systemUTC();
        }
        
    }
}
