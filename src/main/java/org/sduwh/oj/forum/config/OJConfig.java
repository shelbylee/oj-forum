package org.sduwh.oj.forum.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PropertySource("classpath:/oj.properties")
public class OJConfig {

    @Value("${OJ_SSO_URL}")
    private String OJ_SSO_URL;

    @Value("${OJ_CONTEST_URL}")
    private String OJ_CONTEST_URL;

    public String getOJ_SSO_URL() {
        return OJ_SSO_URL;
    }

    public String getOJ_CONTEST_URL() {
        return OJ_CONTEST_URL;
    }
}
