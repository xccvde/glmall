package org.example.glamll.ware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GlmallWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlmallWareApplication.class, args);
    }

}
