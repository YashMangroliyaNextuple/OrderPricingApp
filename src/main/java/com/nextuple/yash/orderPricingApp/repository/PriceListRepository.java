package com.nextuple.yash.orderPricingApp.repository;

import com.nextuple.yash.orderPricingApp.dto.entity.PriceList;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PriceListRepository extends CrudRepository<PriceList,String> {
    public Optional<PriceList> findByOrganizationCodeAndPriceListName(String organizationCode,String priceListName);
    public Optional<PriceList> findByPriceListKey(String priceListKey);
    public Optional<PriceList> findByStartDateAndEndDateAndOrganizationCode(ZonedDateTime startDate,ZonedDateTime endDate,String organizationCode);
    public Optional<List<PriceList>> findByOrganizationCodeAndActive(String organizationCode,String active);
}
