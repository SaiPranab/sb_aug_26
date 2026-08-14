package com.jt.hello;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig {

  @Bean
  @Scope("prototype")
  public Student student() {
    // Student ss = new Student();
    // return ss;

    return new Student();
  }

  @Bean
  public CommandLineRunner commandLineRunner() {
    return args -> {
      System.out.println("This method is called after the spring container is initialized");
    };
  }
}
