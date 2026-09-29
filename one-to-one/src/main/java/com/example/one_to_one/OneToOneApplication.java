package com.example.one_to_one;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class OneToOneApplication {
    private final StudentRepository studentRepository;
    private final AddressRepository addressRepository;

    public static void main(String[] args) {
        SpringApplication.run(OneToOneApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
//			owningSideOperation();

//			INVERSE SIDE OPERATION
            Student newStudent = Student.builder()
					.studentName("Aiswarya")
					.studentEmail("aiswarya@gmail.com")
					.build();

			Address newAddress = Address.builder()
					.city("Khordha")
					.state("Odisha")
					.country("india")
					.student(newStudent)
					.build();

			newStudent.setAddress(newAddress);
			addressRepository.save(newAddress);

        };
    }

    private void owningSideOperation() {
        Address address = Address.builder()
                .city("CTC")
                .state("Odisha")
                .country("IN")
                .build();

        Student student = Student.builder()
                .studentName("Baldev")
                .studentEmail("baldev@gmail.com")
                .address(address)
                .build();

//			studentRepository.save(student); // because when we try to save owning side, inverse side should must be present in the database

        // 1. Mannualy save Address Object then Save Student Object
//			addressRepository.save(address);
//			studentRepository.save(student);

        // 2. use Cascading
//			studentRepository.save(student);


//			UPDATE
//			Student existingStudent = studentRepository.findById(4).orElseThrow();
//			existingStudent.setStudentName("Baldev3");
//			existingStudent.setStudentEmail("b3@gmailcom");
//			Address existingAddress = existingStudent.getAddress();
//			existingAddress.setCity("CTC");
//			studentRepository.save(existingStudent);


//			REMOVE
//			studentRepository.deleteById(3);

//			Retrieve
//			Student studentWithRoll5 = studentRepository.findById(5).orElseThrow();
//			System.out.println("Student Name:- " + studentWithRoll5.getStudentName());
//			System.out.println("Student Email:- " + studentWithRoll5.getStudentEmail());
//
//			Address studentWithRoll5Address = studentWithRoll5.getAddress();
//			System.out.println("Address City:- " + studentWithRoll5Address.getCity());
//			System.out.println("Address State:- " + studentWithRoll5Address.getState());
//			System.out.println("Address Country:- " + studentWithRoll5Address.getCountry());
    }
}
