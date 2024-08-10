package com.toilet_search.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ToiletSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToiletSearchApplication.class, args);
	}

}
