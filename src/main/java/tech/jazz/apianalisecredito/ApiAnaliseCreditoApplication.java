package tech.jazz.apianalisecredito;

import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@Generated
public class ApiAnaliseCreditoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiAnaliseCreditoApplication.class, args);
    }
}
