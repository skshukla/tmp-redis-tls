package com.sachin.work;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
@Slf4j
public class MyProjHandler implements CommandLineRunner {

    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(MyProjHandler.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Storing the key value pair to Redis Over SSL/TLS");
        stringRedisTemplate.opsForValue().set("k1", "v1");
        log.info("Retrieved val for key{{}} is {{}}", "k1", stringRedisTemplate.opsForValue().get("k1"));
    }
}
