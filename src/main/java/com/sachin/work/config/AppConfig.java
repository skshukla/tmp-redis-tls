package com.sachin.work.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SslOptions;
import io.lettuce.core.protocol.ProtocolVersion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.File;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AppConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.ssl:false}")
    private boolean sslEnabled;

    private final ResourceLoader resourceLoader;

    @Autowired
    @Bean
    RedisConnectionFactory redisConnectionFactory() throws IOException {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);

        final File file = new File(AppConfig.class.getResource("/cert.pem").getFile());
        SslOptions sslOptions = SslOptions.builder().trustManager(file)
                .cipherSuites("TLS_CHACHA20_POLY1305_SHA256")
                .build();
        ClientOptions clientOptions = ClientOptions.builder().sslOptions(sslOptions).protocolVersion(ProtocolVersion.RESP3).build();
        LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
                .clientOptions(clientOptions).useSsl().build();
        LettuceConnectionFactory connectionFactory =  new LettuceConnectionFactory(redisStandaloneConfiguration,
                lettuceClientConfiguration);
        return connectionFactory;
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() throws IOException {
        RedisTemplate<?, ?> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }
}
