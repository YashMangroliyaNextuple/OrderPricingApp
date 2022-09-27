package com.nextuple.yash.orderPricingApp.service;

import com.nextuple.yash.orderPricingApp.controller.ItemController;
import com.nextuple.yash.orderPricingApp.dto.*;
import com.nextuple.yash.orderPricingApp.dto.entity.Item;
import com.nextuple.yash.orderPricingApp.dto.entity.PriceList;
import com.nextuple.yash.orderPricingApp.dto.entity.PriceListLineList;
import com.nextuple.yash.orderPricingApp.dto.wrapper.ItemListWrapper;
import com.nextuple.yash.orderPricingApp.exception.ItemException;
import com.nextuple.yash.orderPricingApp.exception.PriceListException;
import com.nextuple.yash.orderPricingApp.repository.ItemRepository;
import com.nextuple.yash.orderPricingApp.repository.PriceListLineListRepository;
import com.nextuple.yash.orderPricingApp.repository.PriceListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service

public class ItemService {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemListWrapper itemListWrapper;

    @Autowired
    private PriceListRepository priceListRepository;

    private final Logger logger= LoggerFactory.getLogger(ItemService.class);
    @Autowired
    private PriceListLineListRepository priceListLineListRepository;

    public ItemListWrapper getAllItems()
    {
        logger.debug("Control reached getAllItems");
        itemListWrapper.setItems((List<Item>) itemRepository.findAll());
        itemListWrapper.setSize(itemListWrapper.getItems().size());
        return itemListWrapper;
    }

    public Item getByItemKey(String itemKey) throws ItemException
    {
        logger.debug("Control reached getByItemKey with itemKey: "+itemKey);
        Optional<Item> optional=itemRepository.findById(itemKey);
        if(optional.isPresent())
        {
            return optional.get();
        }
        else {
            throw new ItemException("Item with key:"+itemKey+" does not exists");
        }
    }
    public Item getByOrganizationCodeAndItemId(String organizationCode,String itemId) throws ItemException
    {
        logger.debug("Control reached getByOrganizationCodeAndItemId with organizationCode: "+organizationCode+" and itemId: "+itemId);
        Optional<Item> optional=itemRepository.findByOrganizationCodeAndItemId(organizationCode,itemId);
        if(optional.isPresent())
        {
            return optional.get();
        }
        else {
            throw new ItemException("Item with id :"+itemId+" and organization code "+organizationCode+" does not exist");
        }
    }

    public Item addItem(Item item) throws ItemException
    {
        logger.debug("Control reached addItem with item: "+item);
        Optional<Item> optional = itemRepository.findByOrganizationCodeAndItemId(item.getOrganizationCode(),item.getItemId());
        if(optional.isPresent()) throw new ItemException("Item with item id : "+item.getItemId()+" and organization code: "+item.getOrganizationCode()+" already exists.");

        itemRepository.save(item);
        return item;
    }
    public void deleteItem(String itemKey) throws ItemException
    {
        logger.debug("Control reached deleteItem with itemKey: "+itemKey);
        Optional<Item> optional = itemRepository.findById(itemKey);
        if(optional.isEmpty()) return;

        Optional<List<PriceListLineList>> optional1=priceListLineListRepository.findByItemKey(itemKey);
        if(optional1.isPresent() && optional1.get().size()!=0) throw new ItemException("Item with key "+itemKey+" is being referred by a pricelist line list, if want to forcefully delete then use '/item/{itemKey}/Y'.");

        itemRepository.delete(optional.get());
    }
    public void deleteItemForcefully(String itemKey,String choice) throws ItemException
    {
        logger.debug("Control reached deleteItemForcefully with itemKey: "+itemKey+" and choice: "+choice);
        Optional<Item> optional = itemRepository.findById(itemKey);
        if(optional.isEmpty()) return;

        Optional<List<PriceListLineList>> optional1=priceListLineListRepository.findByItemKey(itemKey);
        if(optional1.isEmpty() || (optional1.isPresent() && optional1.get().size()==0))
        {
            itemRepository.delete(optional.get());
            return ;
        }
        if(! "Y".equalsIgnoreCase(choice)) throw new ItemException("item with item key "+itemKey+" is being referred by a pricelist line list, if want to forcefully delete then use '/items/{itemKey}/Y'.");
        for(PriceListLineList pp : optional1.get()) priceListLineListRepository.delete(pp);
        itemRepository.delete(optional.get());
    }
    public void deleteItemByOrganizationCodeAndItemId(String organizationCode,String itemId) throws ItemException
    {
        logger.debug("Control reached deleteItemByOrganizationCodeAndItemId with organization code : "+organizationCode+" and item id : "+itemId);
        Optional<Item> optional = itemRepository.findByOrganizationCodeAndItemId(organizationCode,itemId);
        if(optional.isEmpty()) return;

        Optional<List<PriceListLineList>> optional1=priceListLineListRepository.findByItemKey(optional.get().getItemKey());
        if(optional1.isPresent() && optional1.get().size()!=0) throw new ItemException("Item with key "+optional.get().getItemKey()+" is being referred by a pricelist line list, if want to forcefully delete then use '/item/{itemKey}/Y'.");

        itemRepository.delete(optional.get());
    }

    public void patchItem(String itemKey, UpdateItemDTO updateItemDTO) throws ItemException
    {
        logger.debug("Control reached putItem with itemKey: "+itemKey+" and : "+updateItemDTO);
        Optional<Item> optional = itemRepository.findById(itemKey);
        if(optional.isEmpty()) throw new ItemException("Item with key: "+itemKey+" does not exist.");
        Item item = new Item(itemKey, optional.get().getItemId(), updateItemDTO.getItemDescription(), updateItemDTO.getCategory(),  updateItemDTO.getType(), updateItemDTO.getStatus(), optional.get().getOrganizationCode(), updateItemDTO.getUnitOfMeasure());
        itemRepository.save(item);
    }

}
