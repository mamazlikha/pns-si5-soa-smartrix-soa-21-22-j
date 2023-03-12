package fr.polytechnice.soa.energysupplier.controllers;

import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.polytechnice.soa.energysupplier.models.Operation;



@CrossOrigin
@RestController
public class BankController {

   

    private final Logger logger= Logger.getLogger(BankController.class.getName());

    

    @PostMapping(path = "/transfer",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Operation> postDebit(@RequestBody Operation newOperation){
        logger.info("Debiting account "+newOperation.from()+" of "+ newOperation.amount()+ "...");
       
        logger.info("Crediting account "+newOperation.to()+" of amount "+ newOperation.amount()+"...");
        return new ResponseEntity<>(newOperation, HttpStatus.CREATED);
    }
    
    



}
