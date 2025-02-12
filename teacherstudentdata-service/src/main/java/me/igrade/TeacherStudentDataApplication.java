package me.igrade;


import lombok.RequiredArgsConstructor;
import me.igrade.config.UtilsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableConfigurationProperties({UtilsProperties.class})
@RequiredArgsConstructor
@EnableDiscoveryClient
public class TeacherStudentDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeacherStudentDataApplication.class, args);
    }
}
