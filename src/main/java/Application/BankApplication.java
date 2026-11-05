package Application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"Application", "service", "Controller"})
@SpringBootApplication(scanBasePackages = {"Application", "Application.Controller"})
    public class BankApplication {

        public static void main(String[] args) {
            // This single line starts the entire Spring Boot engine
            SpringApplication.run(BankApplication.class, args);
        }
    }

