package com.nextuple.yash.orderPricingApp.repository;

import com.nextuple.yash.orderPricingApp.dto.entity.PriceListLineList;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PriceListLineListRepository extends CrudRepository<PriceListLineList,String> {
    public Optional<List<PriceListLineList>> findByPriceListKey(String priceListKey);
    public Optional<List<PriceListLineList>> findByItemKey(String itemKey);
    public Optional<PriceListLineList> findByOrganizationCodeAndPriceListKeyAndItemKey(String organizationCode,String priceListKey,String itemKey);
}
