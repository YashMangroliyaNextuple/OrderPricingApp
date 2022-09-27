package com.nextuple.yash.orderPricingApp.dto;

import lombok.*;

import java.time.ZonedDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemPriceResponseDTO {
    private String itemId;
    private ZonedDateTime pricingDate;
    private double unitPrice;
    private double listPrice;
    private String priceListName;
}
