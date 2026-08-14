package com;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class Teacher {
  public void sayHello() {
    System.out.println("Teacher says hello");
  }
}
