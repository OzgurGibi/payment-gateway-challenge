package com.checkout.payment.gateway.model.dto;

import com.checkout.payment.gateway.model.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record GetPaymentResponse(
	UUID id,
	PaymentStatus status,
	@JsonProperty("card_number_last_four") String cardNumberLastFour,
	@JsonProperty("expiry_month") int expiryMonth,
	@JsonProperty("expiry_year") int expiryYear,
	String currency,
	int amount)
{
}