package com.nextuple.yash.orderPricingApp.dto.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Component
@Entity
@Table(name="price_list",uniqueConstraints = {@UniqueConstraint(columnNames = {"priceListName","organizationCode"})})
public class PriceList {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name="system-uuid",strategy = "uuid")
    private String priceListKey;
    private String priceListName;
    private String active;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private String organizationCode;

}
