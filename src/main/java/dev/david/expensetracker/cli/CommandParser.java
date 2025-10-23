package dev.david.expensetracker.cli;

import java.util.LinkedHashMap;
import java.util.Map;

public class CommandParser {
	public Command parse(String[] args) {

		if (args.length == 0)

			throw new IllegalArgumentException("Missing command");

		String verb = args[0];

		Map<String, String> options = new LinkedHashMap<String, String>();

		for (int i = 1; i < args.length; i++) {

			String token = args[i];

			token = token.replace('–', '-').replace('—', '-');

			if (!token.startsWith("--"))
				continue;

			int equalIndex = token.indexOf('=');

			if (token.startsWith("--") && equalIndex > 2) {

				String k = token.substring(2, equalIndex);

				String v = stripQuotes(token.substring(equalIndex + 1));

				options.put(k, v);
			} else {
				String k = token.substring(2);
				String v = (i + 1 < args.length && !args[i + 1].startsWith("--")) ? args[i++] : "";
				options.put(k, stripQuotes(v));
			}
		}
		return new Command(verb, options);
	}

	private static String stripQuotes(String value) {

		if (value == null)
			return "";

		if (value.length() >= 2 && ((value.startsWith("\"") && value.endsWith("\"")))

				|| (value.startsWith("'") && value.endsWith("'"))) {
			return value.substring(1, value.length() - 1);

		}
		return value;
	}

}
