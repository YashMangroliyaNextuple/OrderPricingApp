package com.nextuple.yash.orderPricingApp.repository;

import com.nextuple.yash.orderPricingApp.dto.entity.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends CrudRepository<Item,String> {
    public Optional<Item> findByItemId(String itemId);
    public Optional<Item> findByItemKey(String itemKey);
    public Optional<Item> findByOrganizationCodeAndItemId(String organizationCode,String itemId);
}
