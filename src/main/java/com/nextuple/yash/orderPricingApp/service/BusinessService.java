package com.nextuple.yash.orderPricingApp.service;

import com.nextuple.yash.orderPricingApp.controller.BusinessController;
import com.nextuple.yash.orderPricingApp.dto.ItemCurrentYearPricesDTO;
import com.nextuple.yash.orderPricingApp.dto.ItemPriceRequestDTO;
import com.nextuple.yash.orderPricingApp.dto.ItemPriceResponseDTO;
import com.nextuple.yash.orderPricingApp.dto.PriceDTO;
import com.nextuple.yash.orderPricingApp.dto.entity.Item;
import com.nextuple.yash.orderPricingApp.dto.entity.PriceList;
import com.nextuple.yash.orderPricingApp.dto.entity.PriceListLineList;
import com.nextuple.yash.orderPricingApp.exception.ItemException;
import com.nextuple.yash.orderPricingApp.repository.ItemRepository;
import com.nextuple.yash.orderPricingApp.repository.PriceListLineListRepository;
import com.nextuple.yash.orderPricingApp.repository.PriceListRepository;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.DateFormatter;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BusinessService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private PriceListRepository priceListRepository;
    @Autowired
    private PriceListLineListRepository priceListLineListRepository;
    private final Logger logger = LoggerFactory.getLogger(BusinessService.class);
    public Optional<ItemPriceResponseDTO> getItemPrice(ItemPriceRequestDTO itemPriceRequestDTO) throws ItemException
    {
        logger.debug("Control reached getItemPrice of ItemService");
        Optional<Item> optionalItem=itemRepository.findByOrganizationCodeAndItemId(itemPriceRequestDTO.getOrganizationCode(), itemPriceRequestDTO.getItemId());
        if(optionalItem.isEmpty()) throw new ItemException("Item with item id: "+itemPriceRequestDTO.getItemId()+" and organizationCode: "+itemPriceRequestDTO.getOrganizationCode()+" does not exist.");
        Optional<List<PriceListLineList>> optional = priceListLineListRepository.findByItemKey(optionalItem.get().getItemKey());
        if(optional.get().size()==0) throw new ItemException("PriceList line list for item with id: "+itemPriceRequestDTO.getItemId()+" andd orgnanization code: "+itemPriceRequestDTO.getOrganizationCode());
        PriceList priceList;
        ZonedDateTime startDate,endDate,pricingDate;
        ItemPriceResponseDTO itemPriceResponseDTO=null;
        for(PriceListLineList priceListLineList : optional.get())
        {
            Optional<PriceList> optional1 = priceListRepository.findById(priceListLineList.getPriceListKey());
            priceList=optional1.get();
            startDate = priceList.getStartDate().withZoneSameInstant(ZoneId.of("UTC"));
            endDate = priceList.getEndDate().withZoneSameInstant(ZoneId.of("UTC"));
            pricingDate = itemPriceRequestDTO.getPricingDate().withZoneSameInstant(ZoneId.of("UTC"));
            if(startDate.isBefore(pricingDate) && endDate.isAfter(pricingDate) && "Y".equalsIgnoreCase(priceList.getActive()))
            {
                itemPriceResponseDTO=new ItemPriceResponseDTO();
                itemPriceResponseDTO.setItemId(itemPriceRequestDTO.getItemId());
                itemPriceResponseDTO.setPricingDate(itemPriceRequestDTO.getPricingDate());
                itemPriceResponseDTO.setUnitPrice(priceListLineList.getUnitPrice());
                itemPriceResponseDTO.setListPrice(priceListLineList.getListPrice());
                itemPriceResponseDTO.setPriceListName(priceList.getPriceListName());
                break;
            }
        }
        if(itemPriceResponseDTO==null) return Optional.empty();
        return Optional.of(itemPriceResponseDTO);
    }

    public ItemCurrentYearPricesDTO getCurrentYearPrices(String organizationCode,String itemId) throws ItemException
    {
        logger.debug("Control reached getCurrentYearPrices of BusinessService");
        Optional<Item> op = itemRepository.findByOrganizationCodeAndItemId(organizationCode,itemId);
        if(op.isEmpty()) throw new ItemException("Item with item id : "+itemId+" and organization code: "+organizationCode+"does not exist.");
        Item item=op.get();
        Optional<List<PriceListLineList>> optional = priceListLineListRepository.findByItemKey(item.getItemKey());
        if(optional.isEmpty()) throw new ItemException("There is no price list associated with item with id : "+itemId+" and organization code: "+organizationCode);
        ItemCurrentYearPricesDTO itemCurrentYearPricesDTO=new ItemCurrentYearPricesDTO();
        itemCurrentYearPricesDTO.setItemId(itemId);
        itemCurrentYearPricesDTO.setItemDescription(item.getItemDescription());
        PriceList priceList;
        List<PriceDTO> priceDTOList=new ArrayList<>();

        ZonedDateTime zdt=ZonedDateTime.now();
        for(PriceListLineList priceListLineList : optional.get())
        {
            Optional<PriceList> optional1 = priceListRepository.findByPriceListKey(priceListLineList.getPriceListKey());
            if(optional1.isEmpty()) continue;
            priceList=optional1.get();
            if(!"Y".equalsIgnoreCase(priceList.getActive())) continue;
            if(!(priceList.getStartDate().getYear()== zdt.getYear() || priceList.getEndDate().getYear()==zdt.getYear())) continue;
            PriceDTO priceDTO=new PriceDTO();
            priceDTO.setFromDate(priceList.getStartDate());
            priceDTO.setToDate(priceList.getEndDate());
            priceDTO.setUnitPrice(priceListLineList.getUnitPrice());
            priceDTO.setListPrice(priceListLineList.getListPrice());
            priceDTO.setPriceListName(priceList.getPriceListName());
            priceDTOList.add(priceDTO);
        }
        itemCurrentYearPricesDTO.setPrices(priceDTOList);
        return itemCurrentYearPricesDTO;
    }

    public void addBulk(String fileName)
    {
        int index,prevIndex,frontSlashIndex;
        logger.debug("Control reached addBulk with fileName: "+fileName);
        String organizationCode, startDateString, endDateString;
        index=fileName.lastIndexOf("/");
        frontSlashIndex=index;
        prevIndex=index;
        index=fileName.indexOf('_',index+1);
        organizationCode=fileName.substring(prevIndex+1,index);
        logger.debug("Organization code: "+organizationCode);
        prevIndex=index;
        index=fileName.indexOf('_',index+1);
        startDateString=fileName.substring(prevIndex+1,index);
        logger.debug("Start date: "+startDateString);

        prevIndex=index;
        index=fileName.indexOf('.',index+1);
        endDateString=fileName.substring(prevIndex+1,index);
        logger.debug("End date: "+endDateString);

        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate=LocalDate.parse(startDateString, dateTimeFormatter);
        LocalDateTime localDateTime= localDate.atTime(18,30);
        ZoneId zoneId=ZoneId.of("Asia/Kolkata");
        ZonedDateTime startDateTime=ZonedDateTime.of(localDateTime,zoneId);

        localDate=LocalDate.parse(endDateString,dateTimeFormatter);
        localDateTime=localDate.atTime(18,30);
        ZonedDateTime endDateTime=ZonedDateTime.of(localDateTime,zoneId);


        PriceList priceList;
        Optional<PriceList> optional1 = priceListRepository.findByStartDateAndEndDateAndOrganizationCode(startDateTime,endDateTime,organizationCode);
        if(optional1.isEmpty()) {
            priceList=new PriceList();
            priceList.setPriceListName(fileName.substring(frontSlashIndex+1));
            priceList.setActive("Y");
            priceList.setStartDate(startDateTime);
            priceList.setEndDate(endDateTime);
            priceList.setOrganizationCode(organizationCode);
            priceListRepository.save(priceList);
        }
        else priceList=optional1.get();

        try
        {
            File f=new File("Sheets/invalidInput.txt");
            if(f.exists()) f.delete();
            f.createNewFile();
            RandomAccessFile randomAccessFile=new RandomAccessFile(f,"rw");
            randomAccessFile.writeBytes("{\"fileName\" : \""+fileName+"\", \"itemId\" : [");
            FileInputStream fileInputStream=new FileInputStream(new File(fileName));
            XSSFWorkbook xssfWorkbook= new XSSFWorkbook(fileInputStream);
            XSSFSheet workSheet = xssfWorkbook.getSheetAt(0);
            String itemId;
            double unitPrice,listPrice;
            Item item;
            PriceListLineList priceListLineList;
            for(int i=1;i<workSheet.getPhysicalNumberOfRows();i++)
            {
                XSSFRow row = workSheet.getRow(i);

                itemId=row.getCell(0).getStringCellValue();
                unitPrice=row.getCell(1).getNumericCellValue();
                listPrice=row.getCell(2).getNumericCellValue();


                Optional<Item> optionalItem=itemRepository.findByOrganizationCodeAndItemId(organizationCode,itemId);
                if (optionalItem.isEmpty()) {
                    if(i!=1) randomAccessFile.writeBytes(",");
                    randomAccessFile.writeBytes("\""+itemId+"\"");
                    continue;
                }
                item=optionalItem.get();
                Optional<PriceListLineList> priceListLineListOptional=priceListLineListRepository.findByOrganizationCodeAndPriceListKeyAndItemKey(organizationCode, priceList.getPriceListKey(), item.getItemKey());
                if(priceListLineListOptional.isPresent()) continue;
                priceListLineList=new PriceListLineList();
                priceListLineList.setPriceListKey(priceList.getPriceListKey());
                priceListLineList.setItemKey(item.getItemKey());
                priceListLineList.setOrganizationCode(organizationCode);
                priceListLineList.setUnitPrice(unitPrice);
                priceListLineList.setListPrice(listPrice);
                priceListLineListRepository.save(priceListLineList);
            }
            randomAccessFile.writeBytes("]}");
            randomAccessFile.close();
        }catch (FileNotFoundException fileNotFoundException)
        {
            System.out.println(fileNotFoundException);
        }catch (IOException ioException){
            System.out.println(ioException);
        }

    }

}
