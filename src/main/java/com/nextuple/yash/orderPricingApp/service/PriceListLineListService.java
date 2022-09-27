package com.nextuple.yash.orderPricingApp.service;

import com.nextuple.yash.orderPricingApp.controller.PriceListLineListController;
import com.nextuple.yash.orderPricingApp.dto.entity.Item;
import com.nextuple.yash.orderPricingApp.dto.wrapper.ListOfPriceListLineListWrapper;
import com.nextuple.yash.orderPricingApp.dto.entity.PriceList;
import com.nextuple.yash.orderPricingApp.dto.entity.PriceListLineList;
import com.nextuple.yash.orderPricingApp.exception.PriceListLineListException;
import com.nextuple.yash.orderPricingApp.repository.ItemRepository;
import com.nextuple.yash.orderPricingApp.repository.PriceListLineListRepository;
import com.nextuple.yash.orderPricingApp.repository.PriceListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PriceListLineListService {
    @Autowired
    private PriceListLineListRepository priceListLineListRepository;
    @Autowired
    private PriceListRepository priceListRepository;
    @Autowired
    private ItemRepository itemRepository;
    private final Logger logger = LoggerFactory.getLogger(PriceListLineListService.class);

    public ListOfPriceListLineListWrapper getAllPriceListLineList()
    {
        logger.debug("Control reached getAllPriceListLineList of PriceListLineListService");
        ListOfPriceListLineListWrapper listOfPriceLiseLineListWrapper=new ListOfPriceListLineListWrapper();
        listOfPriceLiseLineListWrapper.setListOfPriceListLineList((List< PriceListLineList >)priceListLineListRepository.findAll());
        listOfPriceLiseLineListWrapper.setSize(listOfPriceLiseLineListWrapper.getListOfPriceListLineList().size());
        return listOfPriceLiseLineListWrapper;
    }
    public PriceListLineList getPriceListLineListByKey(String priceListLineKey) throws PriceListLineListException
    {
        logger.debug("Control reached getPriceListLineListByKey of PriceListLineListService");
        Optional<PriceListLineList> optional=priceListLineListRepository.findById(priceListLineKey);
        if(optional.isEmpty())
        {
            throw new PriceListLineListException("Pricelist line list with key: "+priceListLineKey+" does not exist.");
        }
        else return optional.get();
    }
    public void addPriceListLineList(PriceListLineList priceListLineList) throws PriceListLineListException
    {
        logger.debug("Control reached addPriceListLineList of PriceListLineListService");
        Optional<PriceListLineList> optional=priceListLineListRepository.findByOrganizationCodeAndPriceListKeyAndItemKey(priceListLineList.getOrganizationCode(), priceListLineList.getPriceListKey(), priceListLineList.getItemKey());
        if(optional.isPresent())
        {
            throw new PriceListLineListException("Price list line list is already exists.");
        }

        // Checking existence of Pricelist
        Optional<PriceList> optional1=priceListRepository.findById(priceListLineList.getPriceListKey());
        if(optional1.isEmpty())
        {
            throw new PriceListLineListException("Price list with key: "+priceListLineList.getPriceListKey()+" does not exist.");
        }
        if(!optional1.get().getOrganizationCode().equalsIgnoreCase(priceListLineList.getOrganizationCode()))
        {
            throw new PriceListLineListException("Orgnanization code of pricelist doesn't match organization code of priceListLineList");
        }

        // Checking the existence of Item
        Optional<Item> optionalItem=itemRepository.findByItemKey(priceListLineList.getItemKey());
        if(optionalItem.isEmpty())
        {
            throw new PriceListLineListException("Item with key :"+optionalItem.get().getItemKey()+" does not exist.");
        }
        if(!optionalItem.get().getOrganizationCode().equalsIgnoreCase(priceListLineList.getOrganizationCode()))
        {
            throw new PriceListLineListException("Orgnanization code of item doesn't match organization code of PriceListLineList");
        }
        priceListLineListRepository.save(priceListLineList);
    }
    public void deletePriceListLineList(String priceListLineKey) throws PriceListLineListException
    {
        logger.debug("Control reached deletePriceListLineList of PriceListLineListService");
        Optional<PriceListLineList> optional=priceListLineListRepository.findById(priceListLineKey);
        if(optional.isEmpty())
        {
            throw new PriceListLineListException("Price list line list with key : "+priceListLineKey+" does not exist.");
        }
        priceListLineListRepository.delete(optional.get());
    }
    public void putPriceListLineList(String priceListLineKey, PriceListLineList priceListLineList) throws PriceListLineListException
    {
        logger.debug("Control reached putPriceListLineList of PriceListLineListService");
        Optional<PriceListLineList> optional=priceListLineListRepository.findById(priceListLineKey);
        if(optional.isEmpty())
        {
            throw new PriceListLineListException("Price list line list with key : "+priceListLineKey+" does not exist.");
        }
        priceListLineList.setPriceListLineKey(priceListLineKey);

        // Checking existence of Pricelist
        Optional<PriceList> optional1=priceListRepository.findById(priceListLineList.getPriceListKey());
        if(optional1.isEmpty())
        {
            throw new PriceListLineListException("Price list with key: "+priceListLineList.getPriceListKey()+" does not exist.");
        }
        if(!optional1.get().getOrganizationCode().equalsIgnoreCase(priceListLineList.getOrganizationCode()))
        {
            throw new PriceListLineListException("Orgnanization code of pricelist doesn't match organization code of PriceListLineList");
        }

        // Checking the existence of Item
        Optional<Item> optionalItem=itemRepository.findByItemKey(priceListLineList.getItemKey());
        if(optionalItem.isEmpty())
        {
            throw new PriceListLineListException("Item with key :"+optionalItem.get().getItemKey()+" does not exist.");
        }
        if(!optionalItem.get().getOrganizationCode().equalsIgnoreCase(priceListLineList.getOrganizationCode()))
        {
            throw new PriceListLineListException("Orgnanization code of item doesn't match organization code of PriceListLineList");
        }

        priceListLineListRepository.save(priceListLineList);
    }
}

