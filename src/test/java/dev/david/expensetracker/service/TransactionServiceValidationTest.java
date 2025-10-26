package dev.david.expensetracker.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import dev.david.expensetracker.domain.Transaction;
import dev.david.expensetracker.repo.TransactionRepository;

public class TransactionServiceValidationTest {
	// Fake repo só para testes (não precisa ser public)
	static class FakeRepo implements TransactionRepository {
		long next = 1;

		@Override
		public Transaction save(Transaction t) {
			return t;
		}

		@Override
		public List<Transaction> findByMonth(YearMonth ym) {
			return List.of();
		}

		@Override
		public long nextId() {
			return next++;
		}
	}

	@Test
	void valorDeveSerPositivo() {
		var svc = new TransactionService(new FakeRepo());
		var opts = Map.of("type", "despesa", "valor", "0", "cat", "alimentacao", "data", "2025-10-19");
		assertThrows(IllegalArgumentException.class, () -> svc.add(opts));
	}

	@Test
	void camposObrigatorios() {
		var svc = new TransactionService(new FakeRepo());
		var opts = Map.of("type", "despesa", "valor", "10.00", "cat", "alimentacao"); // falta data
		assertThrows(IllegalArgumentException.class, () -> svc.add(opts));
	}
}
