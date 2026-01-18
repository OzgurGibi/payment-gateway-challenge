package com.checkout.payment.gateway.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CardNumberValidator implements ConstraintValidator<CardNumber, String>
{
	@Override
	public boolean isValid(String cardNumber, ConstraintValidatorContext context)
	{
		// this case is handled in another validator
		if (cardNumber == null || cardNumber.isEmpty())
		{
			return true;
		}

		// this case is handled in another validator
		int length = cardNumber.length();
		if (length < 14 || length > 19)
		{
			return true;
		}

		return isLuhnValid(cardNumber);
	}

	private static boolean isLuhnValid(String number)
	{
		int sum = 0;
		boolean alternate = false;

		for (int i = number.length() - 1; i >= 0; i--)
		{
			int digit = Character.getNumericValue(number.charAt(i));

			if (alternate)
			{
				digit *= 2;
				if (digit > 9)
				{
					digit -= 9;
				}
			}
			sum += digit;
			alternate = !alternate;
		}

		return sum % 10 == 0;
	}
}
