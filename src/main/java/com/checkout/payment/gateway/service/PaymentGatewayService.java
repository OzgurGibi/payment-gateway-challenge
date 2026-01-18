package com.checkout.payment.gateway.service;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.checkout.payment.gateway.exception.PaymentNotFoundException;
import com.checkout.payment.gateway.model.dto.*;
import com.checkout.payment.gateway.model.entity.*;
import com.checkout.payment.gateway.model.mapper.*;
import com.checkout.payment.gateway.repository.PaymentsRepository;

@Service
public class PaymentGatewayService
{
	private static final Logger LOG = LoggerFactory.getLogger(PaymentGatewayService.class);

	private final PaymentsRepository paymentsRepository;
	private final AcquiringBankService acquiringBankService;
	private final PaymentMapper paymentMapper;

	public PaymentGatewayService(
		PaymentsRepository paymentsRepository,
		AcquiringBankService acquiringBankService,
		PaymentMapper paymentMapper)
	{
		this.paymentsRepository = paymentsRepository;
		this.acquiringBankService = acquiringBankService;
		this.paymentMapper = paymentMapper;
	}

	public GetPaymentResponse getPaymentById(UUID id)
	{
		LOG.debug("Requesting access to to payment with ID {}", id);

		Payment payment = paymentsRepository
			.get(id)
			.orElseThrow(() -> new PaymentNotFoundException(id));

		return paymentMapper.toGetResponse(payment);
	}

	public PostPaymentResponse processPayment(PostPaymentRequest paymentRequest)
	{
		PostPaymentResponse paymentResponse = acquiringBankService.pay(paymentRequest);

		if (paymentResponse.isValid())
		{
			Payment payment = paymentMapper.toEntity(paymentRequest, paymentResponse);
			paymentsRepository.add(payment);
		}

		return paymentResponse;
	}

	public List<GetPaymentResponse> getAllPaged(int page, int size, String sortBy, String direction)
	{
		return paymentsRepository
			.getAllPaged(page, size, sortBy, direction)
			.stream()
			.map(paymentMapper::toGetResponse)
			.toList();
	}
}
