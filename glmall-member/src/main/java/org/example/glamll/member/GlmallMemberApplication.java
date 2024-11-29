package org.example.glamll.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "org.example.glamll.member.feign")
@SpringBootApplication
@EnableDiscoveryClient
public class GlmallMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlmallMemberApplication.class, args);
    }

}
