package dev.david.expensetracker.repo;

import java.time.YearMonth;
import java.util.List;

import dev.david.expensetracker.domain.Transaction;

public interface TransactionRepository {
	Transaction save(Transaction t) throws Exception;

	List<Transaction> findByMonth(YearMonth ym) throws Exception;

	long nextId() throws Exception;
}
