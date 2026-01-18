package com.checkout.payment.gateway.service;

import com.checkout.payment.gateway.exception.PaymentNotFoundException;
import com.checkout.payment.gateway.model.dto.*;
import com.checkout.payment.gateway.model.entity.*;
import com.checkout.payment.gateway.model.enums.PaymentStatus;
import com.checkout.payment.gateway.model.mapper.PaymentMapper;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentGatewayServiceTest
{
	@Mock
	private PaymentsRepository paymentsRepository;

	@Mock
	private AcquiringBankService acquiringBankService;

	@Mock
	private PaymentMapper paymentMapper;

	@InjectMocks
	private PaymentGatewayService paymentGatewayService;

	@BeforeEach
	void setUp()
	{
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void getPaymentById_WhenPaymentExists_ReturnsResponse()
	{
		// Arrange
		UUID id = UUID.randomUUID();
		Payment payment = new Payment();
		payment.setId(id);
		PostPaymentResponse expectedResponse = new PostPaymentResponse(id, PaymentStatus.AUTHORIZED, "", "EUR", 0);

		when(paymentsRepository.get(id)).thenReturn(Optional.of(payment));
		when(paymentMapper.toPostResponse(payment)).thenReturn(expectedResponse);

		// Act
		GetPaymentResponse response = paymentGatewayService.getPaymentById(id);

		// Assert
		assertNotNull(response);
		assertEquals(id, response.id());
		verify(paymentsRepository).get(id);
		verify(paymentMapper).toGetResponse(payment);
	}

	@Test
	void getPaymentById_WhenPaymentDoesNotExist_ThrowsException()
	{
		// Arrange
		UUID id = UUID.randomUUID();
		when(paymentsRepository.get(id)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(PaymentNotFoundException.class, () -> paymentGatewayService.getPaymentById(id));
		verify(paymentsRepository).get(id);
		verifyNoInteractions(paymentMapper);
	}

	@Test
	void processPayment_WhenSuccessful_SavesAndReturnsResponse()
	{
		// Arrange
		PostPaymentRequest request = new PostPaymentRequest("1234567890123456", 12, 2028, "USD", 1000, "123");
		PostPaymentResponse response = new PostPaymentResponse(UUID.randomUUID(), PaymentStatus.AUTHORIZED, "", "EUR",
				0);

		Payment payment = new Payment();

		when(acquiringBankService.pay(request)).thenReturn(response);
		when(paymentMapper.toEntity(request, response)).thenReturn(payment);

		// Act
		PostPaymentResponse result = paymentGatewayService.processPayment(request);

		// Assert
		assertNotNull(result);
		assertEquals(response.id(), result.id());
		verify(acquiringBankService).pay(request);
		verify(paymentMapper).toEntity(request, response);
		verify(paymentsRepository).add(payment);
	}

	@Test
	void processPayment_WhenResponseIdIsNull_DoesNotSaveToRepository()
	{
		// Arrange
		PostPaymentRequest request = new PostPaymentRequest("1234567890123456", 12, 2028, "USD", 1000, "123");
		PostPaymentResponse response = new PostPaymentResponse(null, PaymentStatus.DECLINED, "", "EUR", 0);

		when(acquiringBankService.pay(request)).thenReturn(response);

		// Act
		PostPaymentResponse result = paymentGatewayService.processPayment(request);

		// Assert
		assertNotNull(result);
		assertNull(result.id());
		verify(acquiringBankService).pay(request);
		verifyNoInteractions(paymentMapper);
		verifyNoInteractions(paymentsRepository);
	}

	@Test
	void getAllPaged_ReturnsSortedAndPagedResults()
	{
		// Arrange
		Payment p1 = new Payment();
		p1.setId(UUID.randomUUID());
		p1.setAmount(100);
		Payment p2 = new Payment();
		p2.setId(UUID.randomUUID());
		p2.setAmount(200);
		Payment p3 = new Payment();
		p3.setId(UUID.randomUUID());
		p3.setAmount(300);

		when(paymentsRepository.getAllPaged(1, 10, "id", "asc")).thenReturn(Arrays.asList(p1, p2, p3));
		when(paymentMapper.toGetResponse(any(Payment.class))).thenAnswer(invocation ->
		{
			Payment p = invocation.getArgument(0);
			return new GetPaymentResponse(p.getId(), PaymentStatus.AUTHORIZED, "", 0, 0, "EUR", p.getAmount());
		});

		// Act - Get page 0, size 2, sort by amount desc
		List<GetPaymentResponse> result = paymentGatewayService.getAllPaged(0, 2, "amount", "desc");

		// Assert
		assertEquals(2, result.size());
		assertEquals(300, result.get(0).amount());
		assertEquals(200, result.get(1).amount());
	}
}
