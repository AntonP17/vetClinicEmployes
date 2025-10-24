package by.antohakon.vetclinicemployes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class VetClinicEmployesApplication {

    public static void main(String[] args) {
        SpringApplication.run(VetClinicEmployesApplication.class, args);
    }

}
