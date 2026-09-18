package com.example.methods_of_jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Entity 
@Builder 
public class Orders {
  @Id 
  @GeneratedValue(strategy = GenerationType.UUID)
  private String orderId;

  private int productId;

  private int quantity;

  private double totalPrice;
}
