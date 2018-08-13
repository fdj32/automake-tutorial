package ru.mastercvv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAutoConfiguration
@SpringBootApplication
@EnableScheduling
public class App {
	
	static {
		System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "128");
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
