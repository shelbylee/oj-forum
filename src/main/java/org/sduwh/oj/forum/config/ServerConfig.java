package org.sduwh.oj.forum.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Slf4j
//@Configuration
public class ServerConfig {

    public static int port;

    static {
        var props = new Properties();
        try {
            var stream = RedisConfig.class.getClassLoader().getResourceAsStream("server.properties");
            if (stream != null) {
                props.load(stream);
            }
        } catch (Exception e) {
            log.error("Failed to load server.properties", e);
        }
        port = Integer.parseInt(props.getProperty("server.port"));
    }
}
