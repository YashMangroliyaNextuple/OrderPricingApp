package com.nextuple.yash.orderPricingApp.dto;

import lombok.*;

import java.time.ZonedDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemPriceRequestDTO {
    private String itemId;
    private String organizationCode;
    private ZonedDateTime pricingDate;
}
