package com.jt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Car {
  // private Engine engine = new Engine();

  // DI (Dependency Injection)
  // Di is a mechanism in which Spring Container automatically inject the bean
  // inside a class reference variable
  // In simple word Spring Container automatically assigns the address of the bean
  // inside another class reference variable

  // 1. Field Based Injection - NR
  // @Autowired
  // private Engine engine;

  // 2. Setter Method Based Injection - R
  // private Engine engine;
  // @Autowired
  // public void setEngine(Engine engine) {
  // // System.out.println("parameter engine"+ engine);
  // // System.out.println("variable engine"+ this.engine);
  // this.engine = engine;
  // }

  // 3. Constructor based injection - HR
  private Engine engine;

  @Autowired
  public Car(@Qualifier("dieselEngine") Engine engine) {
    this.engine = engine;
    System.out.println("paramerterized constructor");
  }

  public Car() {
    System.out.println("Non paramerterized constructor");
  }

  public void startCar() {
    // Engine engine = new Engine();
    engine.startEngine();

    System.out.println("Car is started");
  }

  public void stopCar() {
    // Engine engine = new Engine();
    engine.stopEngine();

    System.out.println("Car is stopped");
  }
}
