package me.igrade;

import com.netflix.discovery.EurekaNamespace;
import me.igrade.config.UtilsProperties;
import me.igrade.security.config.AdminProperties;
import me.igrade.security.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableConfigurationProperties({UtilsProperties.class, RsaKeyProperties.class, AdminProperties.class})
@EnableDiscoveryClient
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
