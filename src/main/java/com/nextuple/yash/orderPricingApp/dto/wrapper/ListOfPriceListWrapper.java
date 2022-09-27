package com.nextuple.yash.orderPricingApp.dto.wrapper;

import com.nextuple.yash.orderPricingApp.dto.entity.PriceList;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ListOfPriceListWrapper {
    int size=0;
    List<PriceList> listOfPriceList;
}
