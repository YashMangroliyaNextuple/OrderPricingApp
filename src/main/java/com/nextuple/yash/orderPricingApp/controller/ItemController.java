package com.nextuple.yash.orderPricingApp.controller;

import com.nextuple.yash.orderPricingApp.dto.*;
import com.nextuple.yash.orderPricingApp.dto.entity.Item;
import com.nextuple.yash.orderPricingApp.dto.wrapper.ItemListWrapper;
import com.nextuple.yash.orderPricingApp.exception.ItemException;
import com.nextuple.yash.orderPricingApp.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    private ItemService itemService;
    private final Logger logger= LoggerFactory.getLogger(ItemController.class);

    @GetMapping("")
    public ResponseEntity<ItemListWrapper> getAllItems()
    {
        logger.debug("Control reached getAllItems");
        ItemListWrapper itemListWrapper =itemService.getAllItems();
        if(itemListWrapper.getSize()==0) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.of(Optional.of(itemListWrapper));
    }
    @GetMapping("/{itemKey}")
    public ResponseEntity getByItemId(@PathVariable("itemKey") String itemKey) throws ItemException
    {
        logger.debug("Control reached getByItemKey with itemKey: "+itemKey);
        Item item=itemService.getByItemKey(itemKey);
        return ResponseEntity.status(HttpStatus.OK).body(item);
    }

    @GetMapping("/{organizationCode}/{itemId}")
    public ResponseEntity getByOrganizationCodeAndItemId(@PathVariable("organizationCode") String organizationCode,@PathVariable("itemId") String itemId) throws ItemException
    {
        logger.debug("Control reached getByOrganizationCodeAndItemId with organizationCode: "+organizationCode+" ans itemId: "+itemId);
        Item item=itemService.getByOrganizationCodeAndItemId(organizationCode,itemId);
        return ResponseEntity.status(HttpStatus.OK).body(item);
    }

    @PostMapping("")
    public ResponseEntity addItem(@RequestBody Item item) throws ItemException{
        logger.debug("Control reached addItem with item: "+item);
        Item item1 = itemService.addItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(item1);
    }

    @DeleteMapping("/{itemKey}")
    public ResponseEntity deleteItem(@PathVariable("itemKey") String itemKey) throws ItemException
    {
        logger.debug("Control reached deleteItem with itemKey: "+itemKey);
        itemService.deleteItem(itemKey);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @DeleteMapping("/{itemKey}/{choice}")
    public ResponseEntity deleteItemForcefully(@PathVariable("itemKey") String itemKey, @PathVariable("choice") String choice) throws ItemException
    {
        logger.debug("Control reached deleteItem with itemKey: "+itemKey+" and choice: "+choice);
        itemService.deleteItemForcefully(itemKey,choice);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @DeleteMapping("/orgCodeAndItemId/{organizationCode}/{itemId}")
    public ResponseEntity deleteItemByOrganizationCodeAndItemId(@PathVariable("organizationCode") String organizationCode, @PathVariable("itemId") String itemId) throws ItemException
    {
        logger.debug("Control reached deleteItem with organization code: "+organizationCode+" and item id: "+itemId);
         itemService.deleteItemByOrganizationCodeAndItemId(organizationCode,itemId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PatchMapping("/{itemKey}")
    public ResponseEntity patchItem(@RequestBody UpdateItemDTO updateItemDTO,@PathVariable("itemKey") String itemKey) throws ItemException
    {
        logger.debug("Control reached patchItem with itemKey: "+itemKey+" and : "+updateItemDTO);
        itemService.patchItem(itemKey,updateItemDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}