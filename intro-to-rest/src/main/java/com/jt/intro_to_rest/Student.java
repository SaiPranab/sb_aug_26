package com.jt.intro_to_rest;

import java.util.Arrays;

public class Student {
  private int id;
  private String name;
  private String courses[];

  public Student(int id, String name, String[] courses) {
    this.id = id;
    this.name = name;
    // this.courses = courses;
  }

  public Student() {
  }

  @Override
  public String toString() {
    return "Student [id=" + id + ", name=" + name + ", courses=" + Arrays.toString(courses) + "]";
  }

  public int getId() {
    System.out.println("##########");
    return id;
  }
  
  public void setId(int id) {
    System.out.println("$$$$$$$$$$");
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String[] getCourses() {
    return courses;
  }

  public void setCourses(String[] courses) {
    this.courses = courses;
  }

  
}
