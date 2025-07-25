package de.yagub.deliverysystem.mswallet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MsWalletApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsWalletApplication.class, args);
    }

}
