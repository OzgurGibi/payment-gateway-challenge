package com.checkout.payment.gateway.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

import com.checkout.payment.gateway.model.enums.PaymentStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse
{
	private final String message;
	public String getMessage() { return message; }

	private PaymentStatus status;
	public PaymentStatus getStatus() { return status; }
	public void setStatus(PaymentStatus status) { this.status = status; }

	private Map<String, String> validationErrors;
	public Map<String, String> getValidationErrors() { return validationErrors; }
	public void setValidationErrors(Map<String, String> validationErrors) { this.validationErrors = validationErrors; }

	public ErrorResponse(String message)
	{
		this.message = message;
	}

	@Override
	public String toString()
	{
		return "ErrorResponse{" + "message='" + message + '\'' + '}';
	}
}
