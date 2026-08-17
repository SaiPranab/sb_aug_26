package com.jt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DependencyInjectionApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(DependencyInjectionApplication.class, args);
		// Engine engine = context.getBean(Engine.class);
		// engine.startEngine();

		Car car = context.getBean(Car.class);
		car.startCar();
		car.stopCar();


		// Demo d1 = new Demo();
		// Demo d2 = new Demo();
		// Demo d3 = new Demo();
		// Demo d4 = new Demo();
		
		// d2.data++;
		// d3.incrementBy5();
		// d4.incrementBy10();

		// System.out.println(d1.data); // 100
		// System.out.println(d2.data); // 101
		// System.out.println(d3.data); // 105
		// System.out.println(d4.data); // 100
	}

}
