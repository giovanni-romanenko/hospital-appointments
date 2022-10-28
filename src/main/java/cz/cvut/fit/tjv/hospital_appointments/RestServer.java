package cz.cvut.fit.tjv.hospital_appointments;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class RestServer {
    public static void main(String[] args) {
        run(RestServer.class, args);
    }
}
