package com.checkout.payment.gateway.controller;

import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.checkout.payment.gateway.model.dto.*;
import com.checkout.payment.gateway.model.enums.PaymentStatus;
import com.checkout.payment.gateway.service.PaymentGatewayService;

@RestController("api")
public class PaymentGatewayController
{
	private final PaymentGatewayService paymentGatewayService;

	public PaymentGatewayController(PaymentGatewayService paymentGatewayService)
	{
		this.paymentGatewayService = paymentGatewayService;
	}

	@GetMapping("/payment/{id}")
	public ResponseEntity<GetPaymentResponse> getPostPaymentEventById(@PathVariable UUID id)
	{
		return ResponseEntity.ok(paymentGatewayService.getPaymentById(id));
	}

	@GetMapping("/payment/all")
	public ResponseEntity<List<GetPaymentResponse>> getPostPaymentEvents(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "id") String sortBy,
		@RequestParam(defaultValue = "asc") String direction)
	{
		return ResponseEntity.ok(paymentGatewayService.getAllPaged(page, size, sortBy, direction));
	}

	@PostMapping("/payment")
	public ResponseEntity<PostPaymentResponse> postProcessPayment(@Valid @RequestBody PostPaymentRequest request)
	{
		PostPaymentResponse response = paymentGatewayService.processPayment(request);

		if (response.status() == PaymentStatus.DECLINED)
		{
			return ResponseEntity
				.status(HttpStatus.PAYMENT_REQUIRED)
				.body(response);
		}

		return ResponseEntity.ok(response);
	}
}