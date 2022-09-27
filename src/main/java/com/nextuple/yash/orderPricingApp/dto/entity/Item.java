package com.nextuple.yash.orderPricingApp.dto.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Component
@Scope("prototype")
@Entity
public class Item {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name="system-uuid",strategy = "uuid")
    public String itemKey;
    public String itemId;
    public String itemDescription;
    public String category;
    public String type;
    //  TODO @builder
    // TODO profiling in springboot
    public String status;
    public String organizationCode;
    public String unitOfMeasure;

    @Override
    public String toString() {
        return "Item{" +
                "itemKey='" + itemKey + '\'' +
                ", itemId='" + itemId + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", category='" + category + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", organizationCode='" + organizationCode + '\'' +
                ", unitOfMeasure='" + unitOfMeasure + '\'' +
                '}';
    }
}
