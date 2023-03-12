package com.smartrix2122soa2122j.smartrix.configurations;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.netflix.hystrix.HystrixCommand;
import com.smartrix2122soa2122j.smartrix.entity.BankDetails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class RemoteServiceCallCommand<T> extends HystrixCommand<ResponseEntity<T>> {

    private final RemoteServiceCallExecutor<T> remoteService;
    private static final Set<RemoteServiceCallExecutor> remoteServiceCallExecutors = new HashSet<>();
    private final Logger logger = LoggerFactory.getLogger("RemoteServiceCallCommand");

    public RemoteServiceCallCommand(Setter config, RemoteServiceCallExecutor<T> remoteService) {
        super(config);
        this.remoteService = remoteService;
    }

    public static int getRemainRequestsSize(){
        return remoteServiceCallExecutors.size();
    }

    @Override
    protected ResponseEntity<T> getFallback() {
        remoteServiceCallExecutors.add(remoteService);
        BankDetails.BankDetailsDTO dflt = new BankDetails.BankDetailsDTO("Default","Default");
        logger.warn("Service invalable for the moment...");
        logger.warn("Following transactions are take in account. \nThey will be done before the new one exection:");
        logger.warn("Size: "+remoteServiceCallExecutors.size());
        logger.warn("Contents: ");
        remoteServiceCallExecutors.forEach(remoteServiceCallExecutor -> logger.warn(remoteServiceCallExecutor.getOperation().toString()));
        return new ResponseEntity<T>(remoteService.getOperation(), HttpStatus.PROCESSING);
    }

    @Override
    protected ResponseEntity<T> run() throws Exception {
        Iterator<RemoteServiceCallExecutor> remoteServiceCallExecutorIterator = remoteServiceCallExecutors.iterator();
        while(remoteServiceCallExecutorIterator.hasNext()) {
            RemoteServiceCallExecutor remoteServiceCallExecutor = remoteServiceCallExecutorIterator.next();
            if (remoteServiceCallExecutor.execute()!=null) {
                remoteServiceCallExecutorIterator.remove();
            }
        }
        return remoteService.execute();
    }
}
