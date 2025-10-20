package dev.david.expensetracker.cli;

import java.util.Map;

public record Command(String verb, Map<String, String> opts) {

}
