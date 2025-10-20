package dev.david.expensetracker;

import dev.david.expensetracker.cli.CommandParser;

public class App {
	public static void main(String[] args) {
		System.out.println("Expense Tracker CLI - starting...");
		// TODO: parse args e despachar comandos

		var parser = new CommandParser();
		var cmd = parser.parse(args);
		System.out.println("verb=" + cmd.verb());
		System.out.println("opts=" + cmd.opts());
	}
}
