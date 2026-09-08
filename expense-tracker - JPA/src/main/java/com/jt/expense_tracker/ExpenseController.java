package com.jt.expense_tracker;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ExpenseController {
  private final JdbcTemplate jdbcTemplate;

  private static final String EXPENSES_TABLE = "expenses";

  @GetMapping("/expenses")
  public List<Expense> getExpenses() {
    String sql = "SELECT * FROM %s".formatted(EXPENSES_TABLE);
    return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Expense>(Expense.class));
  }

  @GetMapping("/expenses/{id}")
  public Expense getExpenseById(@PathVariable int id) {
    var sql = "SELECT * FROM %s WHERE id=?".formatted(EXPENSES_TABLE);
    Expense expense = jdbcTemplate.queryForObject
                          (sql, new BeanPropertyRowMapper<>(Expense.class), id);
    return expense;
  }

  @PostMapping("/expenses")
  public Expense createExpense(@RequestBody Expense expense) {
    var sql = "INSERT INTO %s (title, category, price, date) VALUES (?, ?, ?, ?)"
                                                        .formatted(EXPENSES_TABLE);
    jdbcTemplate.update(sql, expense.getTitle(), expense.getCategory(), 
                                    expense.getPrice(), expense.getDate());
    return expense;
  }

  @DeleteMapping("/expenses/{id}")
  public void deleteExpense(@PathVariable int id) {
    String sql = "DELETE FROM %s WHERE id =?".formatted(EXPENSES_TABLE);
    jdbcTemplate.update(sql, id);
  }

  @PutMapping("/expenses")
  public Expense updateExpense(@RequestBody Expense expense) {
    var sql = "UPDATE %s SET title=?, category=?, price=?, date=? WHERE id =?".formatted(EXPENSES_TABLE);
    jdbcTemplate.update(sql, expense.getTitle(), expense.getCategory(), expense.getPrice(), expense.getDate(), expense.getId());
    Expense updatedExpense = getExpenseById(expense.getId());
    return updatedExpense;
  }
}
