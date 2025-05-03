package de.yagub.deliverysystem.msprocessmanager;

import de.yagub.deliverysystem.msprocessmanager.service.client.msuser.UserServiceClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(clients = {UserServiceClient.class})
public class MsProcessManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsProcessManagerApplication.class, args);
    }

}
