package com.checkout.payment.gateway.model.mapper;

import org.springframework.stereotype.Component;

import com.checkout.payment.gateway.model.dto.*;
import com.checkout.payment.gateway.model.entity.*;

@Component
public class PaymentMapper
{
	public PostPaymentResponse toPostResponse(Payment payment)
	{
		if (payment == null)
			return null;

		return new PostPaymentResponse(
			payment.getId(),
			payment.getStatus(),
			payment.getCardNumberLastFour(),
			payment.getCurrency(),
			payment.getAmount());
	}

	public GetPaymentResponse toGetResponse(Payment payment)
	{
		if (payment == null)
			return null;

		return new GetPaymentResponse(
			payment.getId(),
			payment.getStatus(),
			payment.getCardNumberLastFour(),
			payment.getExpiryMonth(),
			payment.getExpiryYear(),
			payment.getCurrency(),
			payment.getAmount());
	}

	public Payment toEntity(PostPaymentRequest request, PostPaymentResponse response)
	{
		if (request == null || response == null)
			return null;

		Payment payment = new Payment();
		payment.setId(response.id());
		payment.setStatus(response.status());
		payment.setCardNumberLastFour(response.cardNumberLastFour());
		payment.setCurrency(response.currency());
		payment.setAmount(response.amount());
		payment.setExpiryMonth(request.expiryMonth());
		payment.setExpiryYear(request.expiryYear());

		return payment;
	}
}