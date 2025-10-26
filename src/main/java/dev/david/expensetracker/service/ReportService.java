package dev.david.expensetracker.service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dev.david.expensetracker.domain.Transaction;
import dev.david.expensetracker.domain.TransactionType;
import dev.david.expensetracker.repo.TransactionRepository;

public class ReportService {
	private final TransactionRepository repo;

	public ReportService(TransactionRepository repo) {
		this.repo = repo;
	}

	public String monthlySummary(Map<String, String> o) throws Exception {
		var ym = YearMonth.parse(o.get("mes"));
		List<Transaction> list = repo.findByMonth(ym);

		BigDecimal total = BigDecimal.ZERO;
		Map<String, BigDecimal> byCat = new LinkedHashMap<>();

		for (var t : list) {
			BigDecimal signed = t.type() == TransactionType.DESPESA ? t.valor().negate() : t.valor();
			total = total.add(signed);
			byCat.merge(t.categoria(), signed, BigDecimal::add);
		}

		StringBuilder sb = new StringBuilder();
		sb.append("Resumo ").append(ym).append('\n');
		sb.append("Total: ").append(total.toPlainString()).append('\n');
		byCat.forEach((c, v) -> sb.append(c).append(": ").append(v.toPlainString()).append('\n'));
		return sb.toString();
	}
}
