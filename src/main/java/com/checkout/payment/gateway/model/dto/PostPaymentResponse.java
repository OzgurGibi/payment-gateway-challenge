package com.checkout.payment.gateway.model.dto;

import com.checkout.payment.gateway.model.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record PostPaymentResponse(
	UUID id,
	PaymentStatus status,
	@JsonProperty("card_number_last_four") String cardNumberLastFour,
	String currency,
	int amount)
{
	@JsonIgnore
	public boolean isValid()
	{
		return id != null;
	}
}