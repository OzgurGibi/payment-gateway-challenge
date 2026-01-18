package com.checkout.payment.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.checkout.payment.gateway",
    "com.checkout.acquiring.bank"
})
public class PaymentGatewayApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(PaymentGatewayApplication.class, args);
	}
}
