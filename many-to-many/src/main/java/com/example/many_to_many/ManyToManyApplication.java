package com.example.many_to_many;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class ManyToManyApplication {
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public static void main(String[] args) {
        SpringApplication.run(ManyToManyApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
//            oneWayBinding();

//			SAVE - using inverse side

//			Extract - using inverse side

//            UPDATE
        };
    }

    private void oneWayBinding() {
//		SAVE
        Subject subject1 = Subject.builder().subjectName("C").build();
        Subject subject2 = Subject.builder().subjectName("C++").build();
        Subject subject3 = Subject.builder().subjectName("Java").build();
        Subject subject4 = Subject.builder().subjectName("Python").build();

        Student student1 = Student.builder()
                .studentName("Amit")
                .studentEmail("amit@gmail.com")
                .subjects(List.of(subject1, subject2))
                .build();
        Student student2 = Student.builder()
                .studentName("Ankit")
                .studentEmail("ankit@gmail.com")
                .subjects(List.of(subject2, subject3))
                .build();
        Student student3 = Student.builder()
                .studentName("Raj")
                .studentEmail("raj@gmail.com")
                .subjects(List.of(subject3, subject4))
                .build();

//		studentRepository.saveAll(List.of(student1, student2, student3));

//		UPDATE

//		DELETE

//		Extract
		studentRepository.findAll().forEach(student -> {
			System.out.println("student Name is " + student.getStudentName());

			student.getSubjects().forEach(subject -> {
				System.out.println(subject.getSubjectName());
			});
		});
    }
}
