package com.nextuple.yash.orderPricingApp.dto;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class UpdatePriceListDTO {
    private String priceListKey;
    private String active;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
}
