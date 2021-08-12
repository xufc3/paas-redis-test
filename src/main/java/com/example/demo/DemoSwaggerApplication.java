package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DemoSwaggerApplication {
	//定义静态的ApplicationContext
    public static ConfigurableApplicationContext applicationContext;
    
	public static void main(String[] args) {
		System.out.println(System.getProperty("java.class.path"));
		applicationContext = SpringApplication.run(DemoSwaggerApplication.class, args);		
	}

}
