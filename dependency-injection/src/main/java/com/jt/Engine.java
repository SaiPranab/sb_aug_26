package com.jt;

import org.springframework.stereotype.Component;

// @Component
// public class Engine {
//   public void startEngine() {
//     System.out.println("Engine is starting");
//   }
  
//   public void stopEngine() {
//     System.out.println("Engine is stopping");
//   }
// }


public interface Engine {
  void startEngine();
  void stopEngine();
}