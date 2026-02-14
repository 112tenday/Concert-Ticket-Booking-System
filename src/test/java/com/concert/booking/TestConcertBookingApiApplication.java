package com.concert.booking;

import org.springframework.boot.SpringApplication;

public class TestConcertBookingApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(ConcertBookingApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
