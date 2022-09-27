package com.nextuple.yash.orderPricingApp.controller;

import com.nextuple.yash.orderPricingApp.dto.wrapper.ListOfPriceListLineListWrapper;
import com.nextuple.yash.orderPricingApp.dto.entity.PriceListLineList;
import com.nextuple.yash.orderPricingApp.exception.PriceListLineListException;
import com.nextuple.yash.orderPricingApp.service.PriceListLineListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/priceListLineList")
public class PriceListLineListController {
    @Autowired
    private PriceListLineListService priceListLineListService;
    private final Logger logger = LoggerFactory.getLogger(PriceListLineListController.class);

    @GetMapping("")
    public ResponseEntity getAllPriceListLineList()
    {
        logger.debug("Control reached getAllPriceListLineList of PriceListLineListController");
        ListOfPriceListLineListWrapper listOfPriceLiseLineListWrapper = priceListLineListService.getAllPriceListLineList();
        if(listOfPriceLiseLineListWrapper.getSize()==0)
        {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(listOfPriceLiseLineListWrapper);
        }
    }

    @GetMapping("/{priceListLineKey}")
    public ResponseEntity getPriceListLineListByKey(@PathVariable("priceListLineKey") String priceListLineKey) throws PriceListLineListException
    {
        logger.debug("Control reached getAllPriceListLineListByKey of PriceListLineListController");
        PriceListLineList priceListLineList = priceListLineListService.getPriceListLineListByKey(priceListLineKey);
        return ResponseEntity.status(HttpStatus.OK).body(priceListLineList);
    }

    @PostMapping("")
    public ResponseEntity addPriceListLineList(@RequestBody PriceListLineList priceListLineList)  throws PriceListLineListException
    {
        logger.debug("Control reached addPriceListLineList of PriceListLineListController");
        priceListLineListService.addPriceListLineList(priceListLineList);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @DeleteMapping("/{priceListLineKey}")
    public ResponseEntity deletePriceListLineList(@PathVariable("priceListLineKey") String priceListLineKey) throws PriceListLineListException
    {
        logger.debug("Control reached deletePriceListLineList of PriceListLineListController");
        priceListLineListService.deletePriceListLineList(priceListLineKey);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{priceListLineKey}")
    public ResponseEntity putPriceListLineList(@PathVariable("priceListLineKey") String priceListLineKey,@RequestBody PriceListLineList priceListLineList) throws PriceListLineListException
    {
        logger.debug("Control reached putPriceListLineList of PriceListLineListController");
        priceListLineListService.putPriceListLineList(priceListLineKey,priceListLineList);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
