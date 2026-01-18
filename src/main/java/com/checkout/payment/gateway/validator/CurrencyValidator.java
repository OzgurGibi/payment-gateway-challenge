package com.checkout.payment.gateway.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class CurrencyValidator implements ConstraintValidator<Currency, String>
{
	private static final Set<String> VALID_CURRENCIES = Set.of("USD", "EUR", "GBP");

	@Override
	public boolean isValid(String currency, ConstraintValidatorContext context)
	{
		if (currency == null || currency.isEmpty())
		{
			return false;
		}

		return VALID_CURRENCIES.contains(currency.toUpperCase());
	}
}
