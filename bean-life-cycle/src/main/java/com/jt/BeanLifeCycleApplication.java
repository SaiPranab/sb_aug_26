package com.jt;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BeanLifeCycleApplication implements CommandLineRunner{

	public static void main(String[] args) {
		System.out.println("Hiii");
		var context = SpringApplication.run(BeanLifeCycleApplication.class, args);
		System.out.println("Hello");
		var greet = context.getBean(Greet.class);
		greet.greet();
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Command Line Runner");
	}

	

	/*
		Bean Life Cycle

		1. Bean Instantitated 								- constructor
		2. Dependency Injected (if avilable) 	- by using any way of DI
		3. Bean initialized 									- @PostConstruct
		4. Bean used
		5. Bean Destroyed 										- @PreDestroy

		- managed By Spring Container
	*/
}
