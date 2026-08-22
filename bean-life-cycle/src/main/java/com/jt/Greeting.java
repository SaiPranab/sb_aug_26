package com.jt;

import org.springframework.stereotype.Component;

@Component
public class Greeting {
  public Greeting() {
    System.out.println("Greeting Object is constructed");
  }

  public void sayHii() {
    System.out.println("Hiiiiiiiiiiiiiiii");
  }
}
