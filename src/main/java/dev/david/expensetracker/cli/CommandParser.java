package dev.david.expensetracker.cli;

import java.util.LinkedHashMap;
import java.util.Map;

public class CommandParser {
	public Command parse(String[] args) {
		if (args.length == 0)
			throw new IllegalArgumentException("Missing command");
		String verb = args[0];
		Map<String, String> opts = new LinkedHashMap<String, String>();

		for (int i = 1; i < args.length; i++) {

			String a = args[i];

			a = a.replace('–', '-').replace('—', '-');

			if (!a.startsWith("--"))
				continue;

			int eq = a.indexOf('=');

			if (a.startsWith("--") && eq > 2) {

				String k = a.substring(2, eq);

				String v = stripQuotes(a.substring(eq + 1));

				opts.put(k, v);
			} else {
				String k = a.substring(2);
				String v = (i + 1 < args.length && !args[i + 1].startsWith("--")) ? args[i++] : "";
				opts.put(k, stripQuotes(v));
			}
		}
		return new Command(verb, opts);
	}

	private static String stripQuotes(String v) {
		if (v == null)
			return "";
		if (v.length() >= 2 && ((v.startsWith("\"") && v.endsWith("\""))) || (v.startsWith("'") && v.endsWith("'"))) {
			return v.substring(1, v.length() - 1);

		}
		return v;
	}

}
