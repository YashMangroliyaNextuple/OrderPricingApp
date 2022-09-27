package com.nextuple.yash.orderPricingApp.controller;

import com.nextuple.yash.orderPricingApp.dto.ItemCurrentYearPricesDTO;
import com.nextuple.yash.orderPricingApp.dto.ItemPriceRequestDTO;
import com.nextuple.yash.orderPricingApp.dto.ItemPriceResponseDTO;
import com.nextuple.yash.orderPricingApp.exception.ItemException;
import com.nextuple.yash.orderPricingApp.service.BusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/business")
public class BusinessController {
    private Logger logger = LoggerFactory.getLogger(BusinessController.class);
    @Autowired
    private BusinessService businessService;
    @GetMapping("/itemPrice")
    public ResponseEntity getItemPrice(@RequestBody ItemPriceRequestDTO itemPriceRequestDTO)
    {
        logger.debug("Control reached getRequiredPrice of BusinessController");
        try{
            Optional<ItemPriceResponseDTO> optional = businessService.getItemPrice(itemPriceRequestDTO);
            if(optional.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            return ResponseEntity.status(HttpStatus.OK).body(optional.get());
        }
        catch(ItemException itemException)
        {
            logger.error(itemException.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(itemException.getMessage());
        }
    }

    @GetMapping("/currentYearPrices/{organizationCode}/{itemId}")
    public ResponseEntity getCurrentYearPrices(@PathVariable String organizationCode,@PathVariable String itemId)
    {
        logger.debug("Control reached getCurrentYearPrice of ItemController");
        try{
            ItemCurrentYearPricesDTO itemCurrentYearPricesDTO = businessService.getCurrentYearPrices(organizationCode,itemId);
            return ResponseEntity.status(HttpStatus.OK).body(itemCurrentYearPricesDTO);
        }catch (ItemException itemException)
        {
            logger.error(itemException.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(itemException.getMessage());
        }
    }
    @PostMapping("/{fileName}")
    public ResponseEntity addBulk(@PathVariable String fileName)
    {
        logger.debug("Control reached addBulk with filename: "+fileName);
        fileName= "Sheets/"+fileName;
        businessService.addBulk(fileName);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @GetMapping("/test")
    public void multithreadingTester()
    {
        System.out.println("THIS IS TEST METHOD");
        try{
            Thread.sleep(5000);
        }catch(Exception e)
        {
            System.out.println(e);
        }
        System.out.println("ENDDDDDDDDDD");
    }

}
