package com.jt;

public class Demo {
  int data = 100;

  public void incrementBy5() {
    data += 5;
  }

  public static void incrementBy10() {
    // data += 10;

    Demo demo1 = new Demo();
    demo1.data += 10;
  }
}
