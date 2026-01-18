package com.checkout.payment.gateway.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import com.checkout.payment.gateway.validator.CardNumber;
import com.checkout.payment.gateway.validator.Currency;
import com.checkout.payment.gateway.validator.ExpiryDate;

@ExpiryDate
public record PostPaymentRequest(
	@NotBlank
	@Size(min = 14, max = 19)
	@Pattern(regexp = "^\\d+$", message = "Card Number must be 14-19 digits")
	@CardNumber
	@JsonProperty("card_number")
	String cardNumber,

	@NotNull
    @Min(value = 1)
    @Max(value = 12)
	@JsonProperty("expiry_month")
	int expiryMonth,

	@NotNull
	@JsonProperty("expiry_year")
	int expiryYear,

	@NotBlank
	@Currency
	String currency,
	
	@NotNull
	@Positive
	int amount,

	@NotBlank
	@Size(min = 3, max = 4)
    @Pattern(regexp = "^\\d{3,4}$", message = "CVV must be 3-4 digits")
	String cvv)
{
	@JsonProperty("expiry_date")
	public String getExpiryDate()
	{
		return String.format("%02d/%04d", expiryMonth, expiryYear);
	}
}