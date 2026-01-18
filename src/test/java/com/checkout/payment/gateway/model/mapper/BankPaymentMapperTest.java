package com.checkout.payment.gateway.model.mapper;

import com.checkout.acquiring.bank.dto.*;
import com.checkout.payment.gateway.model.dto.*;
import com.checkout.payment.gateway.model.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BankPaymentMapperTest
{
	private BankPaymentMapper bankPaymentMapper;

	@BeforeEach
	void setUp()
	{
		bankPaymentMapper = new BankPaymentMapper();
	}

	@Test
	void toBankPaymentRequest_MapsFieldsCorrectly()
	{
		// Arrange
		PostPaymentRequest request = new PostPaymentRequest("1234567890123456", 12, 2028, "USD", 1000, "123");

		// Act
		BankPaymentRequest result = bankPaymentMapper.toBankPaymentRequest(request);

		// Assert
		assertNotNull(result);
		assertEquals("1234567890123456", result.cardNumber());
		assertEquals("12/2028", result.expiryDate());
		assertEquals("USD", result.currency());
		assertEquals(1000, result.amount());
		assertEquals("123", result.cvv());
	}

	@Test
	void toPaymentResponse_Authorized_MapsCorrectly()
	{
		// Arrange
		PostPaymentRequest request = new PostPaymentRequest("1234567890123456", 12, 2028, "USD", 1000, "123");

		UUID authCode = UUID.randomUUID();
		BankPaymentResponse bankResponse = new BankPaymentResponse(true, authCode.toString());

		// Act
		PostPaymentResponse result = bankPaymentMapper.toPaymentResponse(request, bankResponse);

		// Assert
		assertNotNull(result);
		assertEquals("3456", result.cardNumberLastFour());
		assertEquals(PaymentStatus.AUTHORIZED, result.status());
		assertEquals(authCode, result.id());
		assertEquals(12, request.expiryMonth());
		assertEquals(2028, request.expiryYear());
		assertEquals("USD", result.currency());
		assertEquals(1000, result.amount());
	}

	@Test
	void toPaymentResponse_Declined_MapsCorrectly()
	{
		// Arrange
		PostPaymentRequest request = new PostPaymentRequest("1234567890123456", 12, 2028, "USD", 1000, "123");

		BankPaymentResponse bankResponse = new BankPaymentResponse(false, null);

		// Act
		PostPaymentResponse result = bankPaymentMapper.toPaymentResponse(request, bankResponse);

		// Assert
		assertNotNull(result);
		assertEquals(PaymentStatus.DECLINED, result.status());
		assertNotNull(result.id());
		assertEquals("3456", result.cardNumberLastFour());
	}

	@Test
	void toPaymentResponse_CardNumberTooShort_HandlesSafely()
	{
		// Arrange
		PostPaymentRequest request = new PostPaymentRequest("123", 12, 2028, "USD", 1000, "");

		BankPaymentResponse bankResponse = new BankPaymentResponse(true, UUID.randomUUID().toString());

		// Act
		PostPaymentResponse result = bankPaymentMapper.toPaymentResponse(request, bankResponse);

		// Assert
		assertEquals("0000", result.cardNumberLastFour());
	}
}
