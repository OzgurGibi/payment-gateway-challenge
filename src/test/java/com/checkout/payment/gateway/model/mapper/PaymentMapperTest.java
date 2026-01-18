package com.checkout.payment.gateway.model.mapper;

import com.checkout.payment.gateway.model.dto.*;
import com.checkout.payment.gateway.model.entity.Payment;
import com.checkout.payment.gateway.model.enums.PaymentStatus;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentMapperTest
{
	private PaymentMapper paymentMapper;

	@BeforeEach
	void setUp()
	{
		paymentMapper = new PaymentMapper();
	}

	@Test
	void toPostResponse_MapsFieldsCorrectly()
	{
		// Arrange
		UUID id = UUID.randomUUID();
		Payment payment = new Payment();
		payment.setId(id);
		payment.setStatus(PaymentStatus.AUTHORIZED);
		payment.setCardNumberLastFour("1234");
		payment.setExpiryMonth(10);
		payment.setExpiryYear(2030);
		payment.setCurrency("GBP");
		payment.setAmount(500);

		// Act
		PostPaymentResponse response = paymentMapper.toPostResponse(payment);

		// Assert
		assertNotNull(response);
		assertEquals(id, response.id());
		assertEquals(PaymentStatus.AUTHORIZED, response.status());
		assertEquals("1234", response.cardNumberLastFour());
		assertEquals(10, payment.getExpiryMonth());
		assertEquals(2030, payment.getExpiryYear());
		assertEquals("GBP", response.currency());
		assertEquals(500, response.amount());
	}

	@Test
	void toEntity_MapsFieldsCorrectly()
	{
		// Arrange
		UUID id = UUID.randomUUID();
		PostPaymentRequest request = new PostPaymentRequest("1234567890123456", 5, 2029, "USD", 1000, "123");
		PostPaymentResponse response = new PostPaymentResponse(id, PaymentStatus.DECLINED, "4321", "EUR", 100);

		// Act
		Payment entity = paymentMapper.toEntity(request, response);

		// Assert
		assertNotNull(entity);
		assertEquals(id, entity.getId());
		assertEquals(PaymentStatus.DECLINED, entity.getStatus());
		assertEquals("4321", entity.getCardNumberLastFour());
		assertEquals(5, entity.getExpiryMonth());
		assertEquals(2029, entity.getExpiryYear());
		assertEquals("EUR", entity.getCurrency());
		assertEquals(100, entity.getAmount());
	}

	@Test
	void toPostResponse_WithNull_ReturnsNull()
	{
		assertNull(paymentMapper.toPostResponse(null));
	}

	@Test
	void toEntity_WithNull_ReturnsNull()
	{
		assertNull(paymentMapper.toEntity(null, null));
	}
}
