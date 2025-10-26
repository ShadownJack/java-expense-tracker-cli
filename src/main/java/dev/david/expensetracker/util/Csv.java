package dev.david.expensetracker.util;

public final class Csv {
	private Csv() {
	}

	public static String escape(String s) {
		if (s == null)
			return "";
		boolean needs = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
		String v = s.replace("\"", "\"\"");
		return needs ? "\"" + v + "\"" : v;
	}
}