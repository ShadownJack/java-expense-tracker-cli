package dev.david.expensetracker;

import dev.david.expensetracker.cli.CommandParser;
import dev.david.expensetracker.repo.CsvTransactionRepository;
import dev.david.expensetracker.service.TransactionService;

public class App {
	public static void main(String[] args) throws Exception {
		var parser = new CommandParser();
		var cmd = parser.parse(args);
		var repo = new CsvTransactionRepository("data/lancamentos.csv");
		var service = new TransactionService(repo);

		if ("add".equals(cmd.verb())) { // ou cmd.verb()
			service.add(cmd.opts());
			System.out.println("OK");
		} else {
			System.out.println("Comando desconhecido: " + cmd.verb());
		}

	}
}
