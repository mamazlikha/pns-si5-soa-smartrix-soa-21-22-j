package com.smartrix2122soa2122j.smartrix.configurations;


import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class RemoteServiceCallExecutor<T> {

    private final RestTemplate remoteService;
    private final String url;
    private final T t;

    public RemoteServiceCallExecutor(RestTemplate remoteService, String url, T t) throws InterruptedException {
        this.remoteService = remoteService;
        this.url = url;
        this.t = t;
    }

     ResponseEntity<T> execute() throws InterruptedException {
        return (ResponseEntity<T>) remoteService.postForEntity(url, t,t.getClass());
    }

    public T getOperation() {
        return t;
    }
}
