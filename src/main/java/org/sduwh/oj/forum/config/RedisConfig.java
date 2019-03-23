package org.sduwh.oj.forum.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Slf4j
@Configuration
public class RedisConfig {

    public static String host;
    public static int port;

    static {
        var props = new Properties();
        try {
            var stream = RedisConfig.class.getClassLoader().getResourceAsStream("redis.properties");
            if (stream != null) {
                props.load(stream);
            }
        } catch (Exception e) {
            log.error("Failed to load redis.properties", e);
        }

        host = props.getProperty("spring.forum.redis.hostname");
        port = Integer.parseInt(props.getProperty("spring.forum.redis.port"));
    }

}
