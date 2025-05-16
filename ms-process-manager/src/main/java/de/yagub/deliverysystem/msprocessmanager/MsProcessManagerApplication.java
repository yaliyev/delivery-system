package de.yagub.deliverysystem.msprocessmanager;

import de.yagub.deliverysystem.msprocessmanager.client.order.OrderServiceClient;
import de.yagub.deliverysystem.msprocessmanager.client.user.UserServiceClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(clients = {UserServiceClient.class, OrderServiceClient.class})
public class MsProcessManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsProcessManagerApplication.class, args);
    }

}
