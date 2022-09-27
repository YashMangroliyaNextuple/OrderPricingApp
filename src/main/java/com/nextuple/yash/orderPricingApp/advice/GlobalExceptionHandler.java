package com.nextuple.yash.orderPricingApp.advice;

import com.nextuple.yash.orderPricingApp.exception.ItemException;
import com.nextuple.yash.orderPricingApp.exception.PriceListException;
import com.nextuple.yash.orderPricingApp.exception.PriceListLineListException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ItemException.class,PriceListException.class,PriceListLineListException.class})
    public ResponseEntity<APIErrorResponse> APIExceptionHandlerMethod(Exception e)
    {
        logger.debug("Control reached APIExceptionHandlerMethod with : "+e);
        APIErrorResponse apiErrorResponse=new APIErrorResponse(400,HttpStatus.BAD_REQUEST.toString(),e.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorResponse);
    }
}
