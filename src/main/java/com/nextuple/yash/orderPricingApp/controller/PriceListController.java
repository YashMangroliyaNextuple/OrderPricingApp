package com.nextuple.yash.orderPricingApp.controller;

import com.nextuple.yash.orderPricingApp.dto.UpdatePriceListDTO;
import com.nextuple.yash.orderPricingApp.dto.wrapper.ListOfPriceListWrapper;
import com.nextuple.yash.orderPricingApp.dto.entity.PriceList;
import com.nextuple.yash.orderPricingApp.exception.PriceListException;
import com.nextuple.yash.orderPricingApp.service.PriceListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/priceList")
public class PriceListController {
    @Autowired
    private PriceListService priceListService;

    private final Logger logger = LoggerFactory.getLogger(PriceListController.class);

    @GetMapping("")
    public ResponseEntity getAllPriceList() {
        logger.debug("Control reached getAllPriceList of PriceListController");
        ListOfPriceListWrapper listOfPriceListWrapper = priceListService.getAllPriceList();
        if (listOfPriceListWrapper.getSize() == 0) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(listOfPriceListWrapper);
    }

    @GetMapping("/{priceListKey}")
    public ResponseEntity getByPriceListKey(@PathVariable("priceListKey") String priceListKey) throws PriceListException
    {
        logger.debug("Control reached getByPriceListKey with priceListKey: "+priceListKey);
        PriceList priceList = priceListService.getByPriceListKey(priceListKey);
        return ResponseEntity.status(HttpStatus.OK).body(priceList);
    }

    @GetMapping("/{organizationCode}/{priceListName}")
    public ResponseEntity getPriceListOfOrganization(@PathVariable("organizationCode") String organizationCode, @PathVariable("priceListName") String priceListName) throws PriceListException
    {
        logger.debug("Control reached getPriceListOfOrganization with organizationCode: "+organizationCode+" and priceListName: "+priceListName);
        PriceList priceList = priceListService.getPriceListByOrganizationCodeAndPriceListName(organizationCode, priceListName);
        return ResponseEntity.status(HttpStatus.OK).body(priceList);
    }

    @GetMapping("/getActivePriceLists/{organizationCode}")
    public ResponseEntity getActivePriceLists(@PathVariable("organizationCode") String organizationCode) throws PriceListException
    {
        logger.debug("Control reached getActivePriceLists with organizationCode: "+organizationCode);
        ListOfPriceListWrapper listOfPriceListWrapper=priceListService.getActivePriceLists(organizationCode);
        return ResponseEntity.status(HttpStatus.OK).body(listOfPriceListWrapper);
    }

    @PostMapping("")
    public ResponseEntity addPriceList(@RequestBody PriceList priceList)  throws PriceListException
    {
        logger.debug("Control reached addPriceList");
        priceListService.addPriceList(priceList);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{priceListKey}")
    public ResponseEntity deletePriceList(@PathVariable String priceListKey) throws PriceListException
    {
        logger.debug("Control reached deletePriceList with priceListKey: "+priceListKey);
        priceListService.deletePriceList(priceListKey);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{priceListKey}/{choice}")
    public ResponseEntity deletePriceListForcefully(@PathVariable String priceListKey,@PathVariable String choice) throws PriceListException
    {
        logger.debug("Control reached deletePriceListForcefully with priceListKey: "+priceListKey+" and choice: "+choice);
        priceListService.deletePriceListForcefully(priceListKey,choice);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{priceListKey}")
    public ResponseEntity putPriceList(@PathVariable String priceListKey, @RequestBody UpdatePriceListDTO updatePriceListDTO) throws PriceListException
    {
        logger.debug("Control reached putPriceList with priceListKey: "+priceListKey);
        priceListService.patchPriceList(priceListKey, updatePriceListDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
