package com.checkout.acquiring.bank;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.checkout.acquiring.bank.configuration.BankPaymentServiceConfiguration;
import com.checkout.acquiring.bank.dto.BankPaymentRequest;
import com.checkout.acquiring.bank.dto.BankPaymentResponse;
import com.checkout.payment.gateway.exception.BankCommunicationException;

@Service
public class BankPaymentServiceClient
{
	private final RestTemplate restTemplate;
	private final BankPaymentServiceConfiguration config;

	@Autowired
	public BankPaymentServiceClient(RestTemplate restTemplate, BankPaymentServiceConfiguration config)
	{
		this.restTemplate = restTemplate;
		this.config = config;
	}

	public BankPaymentResponse pay(BankPaymentRequest request)
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<BankPaymentRequest> requestEntity = new HttpEntity<>(request, headers);

		try
		{
			String url = Objects.requireNonNull(config.getUrl(), "Bank URL must not be null");
			ResponseEntity<BankPaymentResponse> responseEntity = restTemplate.postForEntity(url, requestEntity,
				BankPaymentResponse.class);

			if (!responseEntity.getStatusCode().is2xxSuccessful())
			{
				throw new BankCommunicationException("Bank returned error status: " + responseEntity.getStatusCode());
			}

			return responseEntity.getBody();
		}
		catch (RestClientException e)
		{
			throw new BankCommunicationException("Failed to communicate with the bank: " + e.getMessage(), e);
		}
	}
}
