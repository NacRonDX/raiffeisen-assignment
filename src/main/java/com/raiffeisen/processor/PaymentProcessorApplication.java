package com.raiffeisen.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class PaymentProcessorApplication {
	public static void main(String[] args) {
		SpringApplication.run(PaymentProcessorApplication.class, args);
	}
}
