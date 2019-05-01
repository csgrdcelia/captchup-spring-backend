package fr.esgi.j2e.group6.captchup.Hello.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello")
public class HelloWorldController {
    @GetMapping
    public String greeting() {
        return "Hello";
    }

    @GetMapping("/world")
    public String greetingAll() {
        return "Hello world";
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String greetingForU(/*@RequestBody*/ String name) {
        return "Hello " + name;
    }
}
