package com.checkout.payment.gateway.model.mapper;

import java.util.UUID;
import org.springframework.stereotype.Component;

import com.checkout.acquiring.bank.dto.*;
import com.checkout.payment.gateway.model.dto.*;
import com.checkout.payment.gateway.model.enums.*;

@Component
public class BankPaymentMapper
{
	public BankPaymentRequest toBankPaymentRequest(PostPaymentRequest paymentRequest)
	{
		if (paymentRequest == null)
			return null;

		return new BankPaymentRequest(
			paymentRequest.cardNumber(),
			paymentRequest.getExpiryDate(),
			paymentRequest.currency(),
			paymentRequest.amount(),
			paymentRequest.cvv());
	}

	public PostPaymentResponse toPaymentResponse(PostPaymentRequest paymentRequest, BankPaymentResponse bankPaymentResponse)
	{
		if (bankPaymentResponse == null)
			return null;

		String cardNumberLastFour;
		try
		{
			String cardNumber = paymentRequest.cardNumber();
			cardNumberLastFour =
				cardNumber != null && cardNumber.length() >= 4
					? cardNumber.substring(cardNumber.length() - 4)
					: "0000";
		}
		catch (Exception e)
		{
			cardNumberLastFour ="0000";
		}

		boolean isAuthorized = bankPaymentResponse.authorized();

		UUID id =
			isAuthorized
				? UUID.fromString(bankPaymentResponse.authorizationCode())
				: UUID.randomUUID();

		PaymentStatus status =
			isAuthorized
				? PaymentStatus.AUTHORIZED 
				: PaymentStatus.DECLINED;

		return new PostPaymentResponse(
			id,
			status,
			cardNumberLastFour,
			paymentRequest.currency(),
			paymentRequest.amount());
	}
}