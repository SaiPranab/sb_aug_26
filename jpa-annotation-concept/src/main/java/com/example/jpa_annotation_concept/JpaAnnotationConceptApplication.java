package com.example.jpa_annotation_concept;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor 
public class JpaAnnotationConceptApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpaAnnotationConceptApplication.class, args);
	}

	private final EmployeeRepository repository;

	@Bean 
	public CommandLineRunner commandLineRunner() {
		return args -> {
			System.out.println("CommandLineRunner run method");

			Employee employee = Employee.builder()
													.name("Ranjit Nayak")
													.description("Ranjit is a loyal Employeee.")
													.salary(BigDecimal.valueOf(100000.98))
													.status(EmployeeStatus.ACTIVE)
													.build();

		  Employee emp = repository.save(employee);			

			Employee savedEmployee = repository
														.findById(emp.getId()).orElseThrow();

			savedEmployee.setName("Ankit Kumar");
			savedEmployee.setDescription("Ankit is good guy");

			repository.save(savedEmployee);
		};
	}
}
