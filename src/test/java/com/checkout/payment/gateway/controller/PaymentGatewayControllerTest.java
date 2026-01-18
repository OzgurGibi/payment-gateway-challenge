package com.checkout.payment.gateway.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.checkout.payment.gateway.model.entity.*;
import com.checkout.payment.gateway.model.enums.PaymentStatus;
import com.checkout.payment.gateway.repository.PaymentsRepository;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentGatewayControllerTest
{
	@Autowired
	private MockMvc mvc;

	@Autowired
	private PaymentsRepository paymentsRepository;

	@Test
	void whenPaymentWithIdExistThenCorrectPaymentIsReturned() throws Exception
	{
		Payment payment = new Payment();
		payment.setId(UUID.randomUUID());
		payment.setAmount(10);
		payment.setCurrency("USD");
		payment.setStatus(PaymentStatus.AUTHORIZED);
		payment.setExpiryMonth(12);
		payment.setExpiryYear(2024);
		payment.setCardNumberLastFour("4321");

		paymentsRepository.add(payment);

		mvc.perform(MockMvcRequestBuilders
			.get("/payment/" + payment.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value(payment.getStatus().getName()))
				.andExpect(jsonPath("$.card_number_last_four").value(payment.getCardNumberLastFour()))
				.andExpect(jsonPath("$.expiry_month").value(payment.getExpiryMonth()))
				.andExpect(jsonPath("$.expiry_year").value(payment.getExpiryYear()))
				.andExpect(jsonPath("$.currency").value(payment.getCurrency()))
				.andExpect(jsonPath("$.amount").value(payment.getAmount()));
	}

	@Test
	void whenPaymentWithIdDoesNotExistThen404IsReturned() throws Exception
	{
		UUID id = UUID.randomUUID();
		mvc.perform(MockMvcRequestBuilders
			.get("/payment/" + id))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Payment with ID '" + id + "' not found."));
	}
}
