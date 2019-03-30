package org.sduwh.oj.forum;

import org.mybatis.spring.annotation.MapperScan;
import org.sduwh.oj.forum.interceptor.CorsFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"org.sduwh.oj.forum"})
@MapperScan("org.sduwh.oj.forum.mapper")
public class OjForumApplication {

    public static void main(String[] args) {
        SpringApplication.run(OjForumApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean testFilterRegistration() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new CorsFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("corsFilter");
        registration.setOrder(1);
        return registration;
    }
}
