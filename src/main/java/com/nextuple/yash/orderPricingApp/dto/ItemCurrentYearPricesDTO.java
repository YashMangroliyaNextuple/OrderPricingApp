package com.nextuple.yash.orderPricingApp.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemCurrentYearPricesDTO {
    private String itemId;
    private String itemDescription;
    private List<PriceDTO> prices;
}
