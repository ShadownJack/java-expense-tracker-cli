package dev.david.expensetracker.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import dev.david.expensetracker.domain.Transaction;
import dev.david.expensetracker.domain.TransactionType;
import dev.david.expensetracker.repo.TransactionRepository;

public class TransactionService {

	private final TransactionRepository repo;

	public TransactionService(TransactionRepository repo) {
		this.repo = repo;
	}

	public void add(Map<String, String> opts) throws Exception {

		var type = TransactionType.valueOf(req(opts, "type").toUpperCase());

		var valor = new BigDecimal(req(opts, "valor"));

		if (valor.signum() <= 0)
			throw new IllegalArgumentException("valor deve ser > 0");

		var cat = req(opts, "cat"); // ou "categoria" se você preferir o nome completo

		var data = LocalDate.parse(req(opts, "data"));

		var desc = opts.getOrDefault("desc", "");

		long id = repo.nextId();

		repo.save(new Transaction(id, type, valor, cat, data, desc));

	}

	private static String req(Map<String, String> m, String k) {

		var v = m.get(k);

		if (v == null || v.isBlank())
			throw new IllegalArgumentException("falta --" + k);

		return v;

	}

}
