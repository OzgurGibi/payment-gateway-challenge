package com.checkout.payment.gateway.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExpiryDateValidator.class)
@Documented
public @interface ExpiryDate
{
	String message() default "Card expiry date must be in the future";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
