package com.checkout.payment.gateway.model.entity;

import java.util.UUID;

import com.checkout.payment.gateway.model.enums.PaymentStatus;

public class Payment
{
	private UUID id;
	public UUID getId() { return id; }
	public void setId(UUID id) { this.id = id; }

	private PaymentStatus status;
	public PaymentStatus getStatus() { return status; }
	public void setStatus(PaymentStatus status) { this.status = status; }

	private String cardNumberLastFour;
	public String getCardNumberLastFour() { return cardNumberLastFour; }
	public void setCardNumberLastFour(String cardNumberLastFour) { this.cardNumberLastFour = cardNumberLastFour; }

	private int expiryMonth;
	public int getExpiryMonth() { return expiryMonth; }
	public void setExpiryMonth(int expiryMonth) { this.expiryMonth = expiryMonth; }

	private int expiryYear;
	public int getExpiryYear() { return expiryYear; }
	public void setExpiryYear(int expiryYear) { this.expiryYear = expiryYear; }

	private String currency;
	public String getCurrency() { return currency; }
	public void setCurrency(String currency) { this.currency = currency; }

	private int amount;
	public int getAmount() { return amount; }
	public void setAmount(int amount) { this.amount = amount; }
}