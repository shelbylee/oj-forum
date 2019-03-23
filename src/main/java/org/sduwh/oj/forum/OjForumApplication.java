package org.sduwh.oj.forum;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.sduwh.oj.forum"})
@MapperScan("org.sduwh.oj.forum.mapper")
public class OjForumApplication {

    public static void main(String[] args) {
        SpringApplication.run(OjForumApplication.class, args);
    }

}
