package com.checkout.payment.gateway.repository;

import com.checkout.payment.gateway.model.entity.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentsRepositoryTest
{
	private PaymentsRepository paymentsRepository;

	@BeforeEach
	void setUp()
	{
		paymentsRepository = new PaymentsRepository();
	}

	@Test
	void addAndGetPayment_WorksCorrectly()
	{
		// Arrange
		UUID id = UUID.randomUUID();
		Payment payment = new Payment();
		payment.setId(id);

		// Act
		paymentsRepository.add(payment);
		Optional<Payment> retrieved = paymentsRepository.get(id);

		// Assert
		assertTrue(retrieved.isPresent());
		assertEquals(id, retrieved.get().getId());
	}

	@Test
	void get_WhenDoesNotExist_ReturnsEmpty()
	{
		// Act
		Optional<Payment> retrieved = paymentsRepository.get(UUID.randomUUID());

		// Assert
		assertTrue(retrieved.isEmpty());
	}

	@Test
	void getAll_ReturnsAllPayments()
	{
		// Arrange
		Payment p1 = new Payment();
		p1.setId(UUID.randomUUID());
		Payment p2 = new Payment();
		p2.setId(UUID.randomUUID());

		paymentsRepository.add(p1);
		paymentsRepository.add(p2);

		// Act
		Collection<Payment> all = paymentsRepository.getAllPaged(1, 10, "id", "asc");

		// Assert
		assertEquals(2, all.size());
		assertTrue(all.contains(p1));
		assertTrue(all.contains(p2));
	}
}
