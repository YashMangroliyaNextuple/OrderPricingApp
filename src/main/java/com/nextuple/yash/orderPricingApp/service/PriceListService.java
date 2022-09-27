package com.nextuple.yash.orderPricingApp.service;

import com.nextuple.yash.orderPricingApp.dto.UpdatePriceListDTO;
import com.nextuple.yash.orderPricingApp.dto.wrapper.ListOfPriceListWrapper;
import com.nextuple.yash.orderPricingApp.dto.entity.PriceList;
import com.nextuple.yash.orderPricingApp.dto.entity.PriceListLineList;
import com.nextuple.yash.orderPricingApp.exception.PriceListException;
import com.nextuple.yash.orderPricingApp.repository.PriceListLineListRepository;
import com.nextuple.yash.orderPricingApp.repository.PriceListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PriceListService {
    @Autowired
    private PriceListRepository priceListRepository;
    @Autowired
    private PriceListLineListRepository priceListLineListRepository;
    private final Logger logger = LoggerFactory.getLogger(PriceListService.class);

    public ListOfPriceListWrapper getAllPriceList()
    {
        logger.debug("Control reached getAllPriceList of PriceListService");
        ListOfPriceListWrapper listOfPriceListWrapper=new ListOfPriceListWrapper();
        listOfPriceListWrapper.setListOfPriceList((List<PriceList>) priceListRepository.findAll());
        listOfPriceListWrapper.setSize(listOfPriceListWrapper.getListOfPriceList().size());
        return listOfPriceListWrapper;
    }
    public PriceList getByPriceListKey(String priceListKey) throws PriceListException
    {
        logger.debug("Control reached getByPriceListKey of PriceListService");
        Optional<PriceList> optinal=priceListRepository.findById(priceListKey);
        if(optinal.isEmpty())
        {
            throw new PriceListException("Price list with key : "+priceListKey+" does not exist.");
        }
        else{
            return optinal.get();
        }
    }
    public PriceList getPriceListByOrganizationCodeAndPriceListName(String organizationCode,String priceListName) throws PriceListException
    {
        logger.debug("Control reached getPriceByOrganizationCodeAndPriceListName of PriceListService");
        Optional<PriceList> optional=priceListRepository.findByOrganizationCodeAndPriceListName(organizationCode,priceListName);
        if(optional.isEmpty())
        {
            throw new PriceListException("Pricelist with organization code :"+organizationCode+" and pricelist name: "+priceListName+" does not exist.");
        }
        return optional.get();
    }
    public ListOfPriceListWrapper getActivePriceLists(String organizationCode) throws PriceListException
    {
        logger.debug("Control reached getActivePriceLists with organizationCode"+organizationCode);
        Optional<List<PriceList>> optional=priceListRepository.findByOrganizationCodeAndActive(organizationCode,"Y");
        return new ListOfPriceListWrapper(optional.get().size(),optional.get());
    }
    public void deletePriceList(String priceListKey) throws PriceListException
    {
        logger.debug("Control reached deletePriceList of PriceListService");
        Optional<PriceList> optional = priceListRepository.findById(priceListKey);
        if(optional.isEmpty()) throw new PriceListException("PriceList with price list key "+priceListKey+" does not exist.");
        Optional<List<PriceListLineList>> optional1=priceListLineListRepository.findByPriceListKey(priceListKey);
        if(optional1.isPresent() && optional1.get().size()!=0) throw new PriceListException("PriceList with price list key "+priceListKey+" is being referred by a pricelist line list, if want to forcefully delete then use '/pricelist/{priceListKey}/Y'.");
        priceListRepository.delete(optional.get());
    }

    public void deletePriceListForcefully(String priceListKey,String choice) throws PriceListException
    {
        logger.debug("Control reached deletePriceListForcefully of PriceListService");
        Optional<PriceList> optional = priceListRepository.findById(priceListKey);
        if(optional.isEmpty()) return;
        Optional<List<PriceListLineList>> optional1=priceListLineListRepository.findByPriceListKey(priceListKey);
        if(optional1.isEmpty())
        {
            priceListRepository.delete(optional.get());
            return;
        }
        if(!"Y".equalsIgnoreCase(choice)) throw new PriceListException("PriceList with price list key "+priceListKey+" is being referred by a pricelist line list, if want to forcefully delete then use '/pricelist/{priceListKey}/Y'.");
        for(PriceListLineList pp : optional1.get()) priceListLineListRepository.delete(pp);
        priceListRepository.delete(optional.get());
    }
    public void patchPriceList(String priceListKey, UpdatePriceListDTO updatePriceListDTO) throws PriceListException
    {
        logger.debug("Control reached putPriceList of PriceListService");
        Optional<PriceList> optional = priceListRepository.findById(priceListKey);
        if(optional.isEmpty()) throw new PriceListException("Pricelist with key: "+priceListKey+" does not exist.");
        PriceList priceList=optional.get();
        priceList.setActive(updatePriceListDTO.getActive());
        priceList.setStartDate(updatePriceListDTO.getStartDate());
        priceList.setEndDate(updatePriceListDTO.getEndDate());
        priceListRepository.save(priceList);
    }
    public void addPriceList(PriceList priceList) throws PriceListException
    {
        logger.debug("Control reached addPriceList of PriceListService");
        Optional<PriceList> optional = priceListRepository.findByOrganizationCodeAndPriceListName(priceList.getOrganizationCode(),priceList.getPriceListName());
        if(optional.isPresent()) throw new PriceListException("Pricelist with organization code : "+priceList.getOrganizationCode()+" and price list name : "+priceList.getPriceListName()+" already exists.");
        priceListRepository.save(priceList);
    }
}
