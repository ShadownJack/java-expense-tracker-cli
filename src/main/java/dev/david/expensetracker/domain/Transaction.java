package dev.david.expensetracker.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Transaction(long id, TransactionType type, BigDecimal valor, String categoria, LocalDate data,
		String descricao) {
}
