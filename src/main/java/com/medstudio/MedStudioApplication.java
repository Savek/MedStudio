package com.medstudio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = MedStudioApplication.class)
@EnableAutoConfiguration
public class MedStudioApplication {

	public static void main(String[] args) {

        SpringApplication.run(MedStudioApplication.class, args);
	}
}
