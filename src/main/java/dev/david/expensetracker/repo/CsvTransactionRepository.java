package dev.david.expensetracker.repo;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import dev.david.expensetracker.domain.Transaction;
import dev.david.expensetracker.domain.TransactionType;

public class CsvTransactionRepository implements TransactionRepository {

	private final Path file;

	public CsvTransactionRepository(String path) {
		this.file = Path.of(path);
	}

	@Override
	public synchronized Transaction save(Transaction t) throws Exception {
		Files.createDirectories(file.getParent()); // garante /data
		boolean newFile = Files.notExists(file);
		try (var w = Files.newBufferedWriter(file, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
			if (newFile)
				w.write("id,tipo,valor,categoria,data,descricao\n");
			w.write(t.id() + "," + t.type() + "," + t.valor().toPlainString() + "," + t.categoria() + "," + t.data()
					+ "," + (t.descricao() == null ? "" : t.descricao()) + "\n");
		}
		return t;
	}

	@Override
	public synchronized List<Transaction> findByMonth(YearMonth ym) throws Exception {
		if (Files.notExists(file))
			return List.of();
		List<Transaction> out = new ArrayList<>();
		try (var r = Files.newBufferedReader(file)) {
			String header = r.readLine(); // pula header
			for (String line; (line = r.readLine()) != null;) {
				String[] c = line.split(",", -1);
				LocalDate d = LocalDate.parse(c[4]);
				if (YearMonth.from(d).equals(ym)) {
					out.add(new Transaction(Long.parseLong(c[0]), TransactionType.valueOf(c[1]), new BigDecimal(c[2]),
							c[3], d, c.length > 5 ? c[5] : ""));
				}
			}
		}
		return out;
	}

	@Override
	public synchronized long nextId() throws Exception {
		long max = 0;
		if (Files.notExists(file))
			return 1;
		try (var r = Files.newBufferedReader(file)) {
			r.readLine(); // header
			for (String line; (line = r.readLine()) != null;) {
				String[] c = line.split(",", -1);
				max = Math.max(max, Long.parseLong(c[0]));
			}
		}
		return max + 1;
	}

}