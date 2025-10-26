package dev.david.expensetracker;

import dev.david.expensetracker.cli.CommandParser;
import dev.david.expensetracker.repo.CsvTransactionRepository;
import dev.david.expensetracker.service.ReportService;
import dev.david.expensetracker.service.TransactionService;

public class App {
	public static void main(String[] args) throws Exception {
		var parser = new CommandParser();
		var command = parser.parse(args);

		var repo = new CsvTransactionRepository("data/lancamentos.csv");
		var service = new TransactionService(repo);
		var report = new ReportService(repo);

		switch (command.verb()) { // ou .verb()
		case "add" -> {
			service.add(command.opts());
			System.out.println("OK");
		}
		case "list" -> {
			var list = service.list(command.opts());
			if (list.isEmpty()) {
				System.out.println("Sem lançamentos.");
				break;
			}
			System.out.println("ID  | TIPO    | VALOR     | CATEGORIA     | DATA       | DESCRIÇÃO");
			System.out.println("----+---------+-----------+---------------+------------+---------------------");
			list.forEach(t -> System.out.printf("%-3d | %-7s | %10s | %-13s | %-10s | %s%n", t.id(), t.type(),
					t.valor().toPlainString(), t.categoria(), t.data(), t.descricao()));
		}
		case "sum" -> System.out.print(report.monthlySummary(command.opts()));
		case "export" -> service.export(command.opts());
		case "help" -> System.out.println("""
				Comandos:
				  add --type=DESPESA|RECEITA --valor=... --cat=... --data=YYYY-MM-DD [--desc="..."]
				  list --mes=YYYY-MM
				  sum  --mes=YYYY-MM
				  export --mes=YYYY-MM --fmt=csv
				""");
		default -> System.out.println("Comando desconhecido: " + command.verb());
		}
	}
}
