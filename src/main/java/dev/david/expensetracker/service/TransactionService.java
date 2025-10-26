package dev.david.expensetracker.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import dev.david.expensetracker.domain.Transaction;
import dev.david.expensetracker.domain.TransactionType;
import dev.david.expensetracker.repo.TransactionRepository;

public class TransactionService {
	private final TransactionRepository repo;

	public TransactionService(TransactionRepository repo) {
		this.repo = repo;
	}

	public void add(Map<String, String> o) throws Exception {
		var type = TransactionType.valueOf(req(o, "type").toUpperCase());
		var valor = new BigDecimal(req(o, "valor"));
		if (valor.signum() <= 0)
			throw new IllegalArgumentException("valor deve ser > 0");
		var cat = req(o, "cat"); // ou "categoria"
		var data = LocalDate.parse(req(o, "data"));
		var desc = o.getOrDefault("desc", "");
		long id = repo.nextId();
		repo.save(new Transaction(id, type, valor, cat, data, desc));
	}

	public List<Transaction> list(Map<String, String> o) throws Exception {
		return repo.findByMonth(YearMonth.parse(req(o, "mes")));
	}

	public void export(Map<String, String> o) throws Exception {
		// por enquanto, só listar e imprimir no stdout
		var ym = YearMonth.parse(req(o, "mes"));
		for (var t : repo.findByMonth(ym)) {
			System.out.println(t.id() + "," + t.type() + "," + t.valor() + "," + t.categoria() + "," + t.data() + ","
					+ t.descricao());
		}
	}

	private static String req(Map<String, String> m, String k) {
		var v = m.get(k);
		if (v == null || v.isBlank())
			throw new IllegalArgumentException("falta --" + k);
		return v;
	}
}
