package com.jt.intro_to_rest;

import java.util.List;

import javax.swing.text.html.FormSubmitEvent.MethodType;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

// @Controller
// @ResponseBody
@RestController
public class StudentController {
  private ObjectMapper mapper;

  public StudentController(ObjectMapper mapper) {
    this.mapper = mapper;
  }
  
  @RequestMapping(value = "/student", method = RequestMethod.GET)
  // @ResponseBody // It tells SpringBoot to return a JSON Object instead of a view file
  public Student getStudent() {  
    Student student = new Student(101, "Ankit", new String[]{"Java", "Python"});

    System.out.println("//////" + student);
    return student;
  }

  @RequestMapping("/student1")
  public void convertjavaObjToJSON() throws JacksonException{
    Student student = new Student(101, "Ankit", new String[]{"Java", "Python"});

    String json = mapper.writeValueAsString(student);
    System.out.println("Java to JSON:- " + json);
  }

  @RequestMapping("/student2")
  public void convertJSONToJavaObj() throws JacksonException{
    String json = """
    {
      "id": 102,
      "name": "Aniket",
      "courses": ["C", "C++"]
    }
        """;

    Student student = mapper.readValue(json, Student.class);
    System.out.println("After converting from json to java Object " + student);
  }

  @RequestMapping("/fruits")
  // @ResponseBody 
  public List<String> geStrings() {
    return List.of("Apple", "Mango", "Grapes");
  }
} 
