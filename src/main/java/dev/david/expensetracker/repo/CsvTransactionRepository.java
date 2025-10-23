package dev.david.expensetracker.repo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import dev.david.expensetracker.domain.Transaction;

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
	public List<Transaction> findByMonth(java.time.YearMonth ym) {
		return List.of();
	} // implementar depois

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
