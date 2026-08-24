package com.jt.intro_to_web;

import java.io.PrintWriter;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HelloController {
  @RequestMapping("/home")
  public void sayHello(PrintWriter writer) {
    System.out.println("Hello Web");
    writer.println("<h1>Hello Spring Web</h1> <p>Welcome Home</P>");
  }

  @RequestMapping("/")
  public void landingPage(PrintWriter printWriter) {
    System.out.println("landing Page1");
    printWriter.println("Our First Landing Page1");
  }

  @RequestMapping("/contact")
  public String contact() {
    return "contact-page";
  }

  // @RequestMapping("/submit-details")
  // public String submitDetails(HttpServletRequest request, Model model) {
  // System.out.println("Submit Details handled");

  // String name = request.getParameter("name");
  // String phone = request.getParameter("phone");

  // // System.out.println("name is:- " + name);
  // // System.out.println("phone is:- " + phone);

  // model.addAttribute("name1", name);
  // model.addAttribute("phone", phone);

  // return "details-page";
  // }

  @RequestMapping("/submit-details")
  public String submitDetails(@RequestParam(value = "name1") String name1, @RequestParam String phone, Model model) {
    model.addAttribute("name1", name1);
    model.addAttribute("phone", phone);

    return "details-page";
  }
}
