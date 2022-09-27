package com.nextuple.yash.orderPricingApp.dto;

import com.nextuple.yash.orderPricingApp.dto.entity.Item;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateItemDTO {
    public String itemDescription;
    public String category;
    public String type;
    public String status;
    public String unitOfMeasure;

}
