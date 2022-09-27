package com.nextuple.yash.orderPricingApp;

import com.nextuple.yash.orderPricingApp.dto.entity.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OrderPricingAppApplication {

	@Bean
	public Item getItemBean()
	{
		return new Item();
	}


	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(OrderPricingAppApplication.class, args);


	}

}
