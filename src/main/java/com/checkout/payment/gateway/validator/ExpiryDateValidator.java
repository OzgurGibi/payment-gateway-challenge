package com.checkout.payment.gateway.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.YearMonth;

import com.checkout.payment.gateway.model.dto.PostPaymentRequest;

public class ExpiryDateValidator implements ConstraintValidator<ExpiryDate, PostPaymentRequest>
{
	@Override
	public boolean isValid(PostPaymentRequest request, ConstraintValidatorContext context)
	{
		// this case is handled in another validator
		if (request == null)
		{
			return true;
		}

		try
		{
			YearMonth expiryYearMonth = YearMonth.of(request.expiryYear(), request.expiryMonth());
			YearMonth currentYearMonth = YearMonth.now();

			return expiryYearMonth.isAfter(currentYearMonth);
		}
		catch (Exception e)
		{
			return false;
		}
	}
}
