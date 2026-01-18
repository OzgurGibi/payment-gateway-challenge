package com.checkout.payment.gateway.service;

import com.checkout.acquiring.bank.BankPaymentServiceClient;
import com.checkout.acquiring.bank.dto.BankPaymentRequest;
import com.checkout.acquiring.bank.dto.BankPaymentResponse;
import com.checkout.payment.gateway.model.dto.PostPaymentRequest;
import com.checkout.payment.gateway.model.dto.PostPaymentResponse;
import com.checkout.payment.gateway.model.enums.PaymentStatus;
import com.checkout.payment.gateway.model.mapper.BankPaymentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.UUID;

class AcquiringBankServiceTest
{
	@Mock
	private BankPaymentServiceClient bankPaymentServiceClient;

	@Mock
	private BankPaymentMapper bankPaymentMapper;

	@InjectMocks
	private AcquiringBankService acquiringBankService;

	@BeforeEach
	void setUp()
	{
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void pay_CallsClientAndMapsResponse()
	{
		// Arrange
		PostPaymentRequest request = new PostPaymentRequest("1234567890123456", 12, 2028, "USD", 1000, "123");
		BankPaymentRequest bankRequest = new BankPaymentRequest("", "/", "EUR", 0, "");
		BankPaymentResponse bankResponse = new BankPaymentResponse(true, "");
		PostPaymentResponse expectedResponse = new PostPaymentResponse(UUID.randomUUID(), PaymentStatus.AUTHORIZED, "",
				"EUR", 0);

		when(bankPaymentMapper.toBankPaymentRequest(request)).thenReturn(bankRequest);
		when(bankPaymentServiceClient.pay(bankRequest)).thenReturn(bankResponse);
		when(bankPaymentMapper.toPaymentResponse(request, bankResponse)).thenReturn(expectedResponse);

		// Act
		PostPaymentResponse result = acquiringBankService.pay(request);

		// Assert
		assertNotNull(result);
		assertEquals(expectedResponse, result);
		verify(bankPaymentMapper).toBankPaymentRequest(request);
		verify(bankPaymentServiceClient).pay(bankRequest);
		verify(bankPaymentMapper).toPaymentResponse(request, bankResponse);
	}
}
