package com.nextuple.yash.orderPricingApp.dto.wrapper;

import com.nextuple.yash.orderPricingApp.dto.entity.Item;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Component
public class ItemListWrapper {
    private int size=0;
    private List<Item> items;
}
