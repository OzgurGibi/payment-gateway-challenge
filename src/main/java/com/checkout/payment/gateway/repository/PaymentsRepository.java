package com.checkout.payment.gateway.repository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

import com.checkout.payment.gateway.model.entity.Payment;

@Repository
public class PaymentsRepository
{
	private final HashMap<UUID, Payment> payments = new HashMap<>();

	public void add(Payment payment)
	{
		payments.put(payment.getId(), payment);
	}

	public Optional<Payment> get(UUID id)
	{
		return Optional.ofNullable(payments.get(id));
	}

	public List<Payment> getAllPaged(int page, int size, String sortBy, String direction)
	{
		Comparator<Payment> comparator = switch (sortBy)
		{
			case "amount" -> Comparator.comparing(Payment::getAmount);
			case "status" -> Comparator.comparing(p -> p.getStatus().getName());
			case "currency" -> Comparator.comparing(Payment::getCurrency);
			case "expiryMonth" -> Comparator.comparing(Payment::getExpiryMonth);
			case "expiryYear" -> Comparator.comparing(Payment::getExpiryYear);
			default -> Comparator.comparing(p -> p.getId().toString());
		};

		if ("desc".equalsIgnoreCase(direction))
		{
			comparator = comparator.reversed();
		}

		return payments
			.values()
			.stream()
			.sorted(comparator)
			.skip((long) page * size)
			.limit(size)
			.toList();
	}
}