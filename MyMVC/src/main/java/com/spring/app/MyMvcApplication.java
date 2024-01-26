package com.spring.app;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.spring.app.common.FullBeanNameGenerator;

@EnableAspectJAutoProxy  // AOP 를 찾을 수 있게 해주는 것이다.
@ComponentScan(nameGenerator = FullBeanNameGenerator.class)
@SpringBootApplication
public class MyMvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyMvcApplication.class, args);
	}

}
