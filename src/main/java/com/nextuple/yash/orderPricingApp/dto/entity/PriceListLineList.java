package com.nextuple.yash.orderPricingApp.dto.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Entity
public class  PriceListLineList {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name="system-uuid",strategy = "uuid")
    private String priceListLineKey;
    private String priceListKey;
    private String itemKey;
    private double unitPrice;
    private double listPrice;
    private String organizationCode;

}
