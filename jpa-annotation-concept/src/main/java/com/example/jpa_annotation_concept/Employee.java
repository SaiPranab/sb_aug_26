package com.example.jpa_annotation_concept;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor
@Entity 
@Table(name = "emp")
public class Employee {
  @Id 
  @GeneratedValue (strategy = GenerationType.UUID)
  @Column(name = "emp_id")
  private String id;

  @Column(name = "emp_name", columnDefinition = "VARCHAR(30)", nullable = false, unique = true)
  private String name;

  @Column(name = "emp_desc")
  @Lob // String -> TINYTEXT, byte[] -> BLOB, char[] -> CLOB
  private String description;

  @Column(name = "emp_salary", precision = 10, scale = 2)
  private BigDecimal salary;

  @Column(name = "emp_status")
  private EmployeeStatus status;

  @CreationTimestamp 
  private LocalDateTime createdTime;

  @UpdateTimestamp
  private LocalDateTime updatedTime;
}
