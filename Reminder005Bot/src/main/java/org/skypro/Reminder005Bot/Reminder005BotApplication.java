package org.skypro.Reminder005Bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Reminder005BotApplication {

	public static void main(String[] args) {
		SpringApplication.run(Reminder005BotApplication.class, args);
	}

}
