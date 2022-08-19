package com.avasthi.datascience.pipeline.server;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.avasthi.datascience"},
		exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@Log4j2
@EnableScheduling
public class DataPipelineServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataPipelineServiceApplication.class, args);
	}

}
