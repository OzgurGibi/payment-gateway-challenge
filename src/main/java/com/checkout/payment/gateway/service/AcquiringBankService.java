package com.checkout.payment.gateway.service;

import org.springframework.stereotype.Service;

import com.checkout.acquiring.bank.dto.*;
import com.checkout.acquiring.bank.BankPaymentServiceClient;
import com.checkout.payment.gateway.model.dto.*;
import com.checkout.payment.gateway.model.mapper.BankPaymentMapper;

@Service
public class AcquiringBankService
{
	private final BankPaymentServiceClient bankPaymentServiceClient;
	private final BankPaymentMapper bankPaymentMapper;

	public AcquiringBankService(BankPaymentServiceClient bankPaymentServiceClient, BankPaymentMapper bankPaymentMapper)
	{
		this.bankPaymentServiceClient = bankPaymentServiceClient;
		this.bankPaymentMapper = bankPaymentMapper;
	}

	public PostPaymentResponse pay(PostPaymentRequest paymentRequest)
	{
		BankPaymentRequest request = bankPaymentMapper.toBankPaymentRequest(paymentRequest);

		BankPaymentResponse bankPaymentResponse = bankPaymentServiceClient.pay(request);

		return bankPaymentMapper.toPaymentResponse(paymentRequest, bankPaymentResponse);
	}
}
