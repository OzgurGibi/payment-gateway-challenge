package com.checkout.payment.gateway.exception;

import java.util.UUID;

public class PaymentNotFoundException extends RuntimeException
{
    private final UUID paymentId;

    public PaymentNotFoundException(UUID paymentId)
    {
        super(String.format("Payment with ID '%s' not found.", paymentId));
        this.paymentId = paymentId;
    }

    public UUID getPaymentId()
    {
        return paymentId;
    }
}
