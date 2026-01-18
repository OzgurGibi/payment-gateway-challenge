package com.checkout.acquiring.bank;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.checkout.acquiring.bank.configuration.BankPaymentServiceConfiguration;

import java.time.Duration;

@Configuration
public class RestClientConfig
{
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder, BankPaymentServiceConfiguration config)
	{
		return builder
			.setConnectTimeout(Duration.ofMillis(config.getConnectTimeoutMilliseconds()))
			.setReadTimeout(Duration.ofMillis(config.getReadTimeoutMilliseconds()))
			.build();
	}
}