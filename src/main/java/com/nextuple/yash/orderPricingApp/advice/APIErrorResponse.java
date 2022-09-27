package com.nextuple.yash.orderPricingApp.advice;

import lombok.*;

import java.time.Instant;

@Setter
@Getter
@ToString
public class APIErrorResponse {
    private String timeStamp;
    private int status;
    private String error;
    private String message;
    public APIErrorResponse()
    {
        this.timeStamp = Instant.now().toString();
    }
    public APIErrorResponse(int status,String error,String message)
    {
        this.timeStamp = Instant.now().toString();
        this.status = status;
        this.error = error;
        this.message = message;
    }
}

