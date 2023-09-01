package org.example.demo.rest.webservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan("org.example.demo")
public class RestWebServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestWebServiceApplication.class, args);
    }

}
