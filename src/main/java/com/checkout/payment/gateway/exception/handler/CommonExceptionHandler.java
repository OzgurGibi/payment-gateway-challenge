package com.checkout.payment.gateway.exception.handler;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.checkout.payment.gateway.exception.BankCommunicationException;
import com.checkout.payment.gateway.exception.EventProcessingException;
import com.checkout.payment.gateway.exception.PaymentNotFoundException;
import com.checkout.payment.gateway.model.dto.ErrorResponse;
import com.checkout.payment.gateway.model.enums.PaymentStatus;

@RestControllerAdvice
public class CommonExceptionHandler
{
	private static final Logger LOG = LoggerFactory.getLogger(CommonExceptionHandler.class);

	@ExceptionHandler(PaymentNotFoundException.class)
	public ResponseEntity<ErrorResponse> handlePaymentNotFound(PaymentNotFoundException ex)
	{
		LOG.error("Payment not found: {}", ex.getPaymentId());

		ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

	@ExceptionHandler(BankCommunicationException.class)
	public ResponseEntity<ErrorResponse> handleBankCommunication(BankCommunicationException ex)
	{
		LOG.error("Bank communication error: {}", ex.getMessage());

		ErrorResponse errorResponse = new ErrorResponse("Acquiring bank is currently unavailable or returned an error.");
		errorResponse.setStatus(PaymentStatus.REJECTED);
		return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(errorResponse);
	}

	@ExceptionHandler(EventProcessingException.class)
	public ResponseEntity<ErrorResponse> handleEventProcessing(EventProcessingException ex)
	{
		LOG.error("Event processing error: {}", ex.getMessage());

		ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex)
	{
		LOG.error("Validation error happened", ex);

		Map<String, String> errors = new HashMap<>();

		// field level errors
		ex.getBindingResult().getFieldErrors()
			.forEach(error ->
				errors.put(error.getField(), error.getDefaultMessage()));

		// class level errors
		ex.getBindingResult().getGlobalErrors()
        	.forEach(error -> 
            	errors.put(error.getCode(), error.getDefaultMessage()));

		ErrorResponse errorResponse = new ErrorResponse("Validation failed");
		errorResponse.setValidationErrors(errors);
		errorResponse.setStatus(PaymentStatus.REJECTED);

		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> catchAll(Exception ex)
	{
		LOG.error("Unexpected error occurred", ex);
		
		ErrorResponse errorResponse = new ErrorResponse("An internal server error occurred. Please try again later.");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
}