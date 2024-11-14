package com.g1.mychess.tournament;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class TournamentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TournamentServiceApplication.class, args);
    }
}
