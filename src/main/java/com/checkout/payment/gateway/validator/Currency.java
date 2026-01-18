package com.checkout.payment.gateway.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CurrencyValidator.class)
@Documented
public @interface Currency
{
	String message() default "Currency must be a valid 3-character ISO code (USD, EUR, or GBP)";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
