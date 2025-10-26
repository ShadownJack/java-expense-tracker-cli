package dev.david.expensetracker.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import org.junit.jupiter.api.Test;

public class CommandParserTest {
	void parseKeyEqualsValue() {
		var p = new CommandParser();
		var c = p.parse(new String[] { "add", "--type=despesa", "--valor=49.90", "--cat=alimentacao",
				"--data=2025-10-19", "--desc=\"lanche\"" });

		assertEquals("add", c.verb());
		assertEquals(Map.of("type", "despesa", "valor", "49.90", "cat", "alimentacao", "data", "2025-10-19", "desc",
				"lanche"), c.opts());
	}

	@Test
	void parseKeySpaceValueAndQuotes() {
		var p = new CommandParser();
		var c = p.parse(new String[] { "add", "--desc", "\"lanche da tarde\"" });
		assertEquals("add", c.verb());
		assertEquals("lanche da tarde", c.opts().get("desc"));
	}

	@Test
	void missingCommandThrows() {
		var p = new CommandParser();
		assertThrows(IllegalArgumentException.class, () -> p.parse(new String[] {}));
	}
}
