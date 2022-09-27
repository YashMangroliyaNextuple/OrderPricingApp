package com.nextuple.yash.orderPricingApp.dto;

import lombok.*;

import java.time.ZonedDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PriceDTO {
    private ZonedDateTime fromDate,toDate;
    private double unitPrice,listPrice;
    private String priceListName;
}
