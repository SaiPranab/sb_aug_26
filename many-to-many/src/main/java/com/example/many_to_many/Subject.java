package com.example.many_to_many;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subjectId;

    private String subjectName;

    @ManyToMany(mappedBy = "subjects")
    private List<Student> students;
}
