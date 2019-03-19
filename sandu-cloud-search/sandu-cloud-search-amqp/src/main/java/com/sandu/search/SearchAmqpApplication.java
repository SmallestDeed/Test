package com.sandu.search;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@EnableScheduling
@EnableTransactionManagement
@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class SearchAmqpApplication {

	public static void main(String[] args) {
		log.info("############### Search Amqp Server Starting... ###############");
		SpringApplication.run(SearchAmqpApplication.class, args);
		log.info("############### Search Amqp Start Finish! ###############");
	}
}
