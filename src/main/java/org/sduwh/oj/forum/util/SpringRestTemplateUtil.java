package org.sduwh.oj.forum.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.sduwh.oj.forum.param.OjResponseParam;
import org.sduwh.oj.forum.param.UserParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class SpringRestTemplateUtil {

    private static final String OJ_SSO_URL = "http://localhost:8000/api/sso";

    @Autowired
    private RestTemplate restTemplate;

    public UserParam getUserInfo(HttpServletRequest request) {

        Gson gson = new Gson();

        HttpHeaders getHeaders = new HttpHeaders();

        Cookie[] cookies = request.getCookies();
        List<String> cookieList = new ArrayList<>();

        for (Cookie cookie : cookies) {
            cookieList.add(cookie.getName()+"="+cookie.getValue());
            getHeaders.put(HttpHeaders.COOKIE, cookieList);
        }

        HttpEntity getRequest = new HttpEntity(getHeaders);

       // ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, getRequest, String.class);
        ResponseEntity<String> responseEntity = restTemplate.exchange(OJ_SSO_URL, HttpMethod.GET, getRequest, String.class);

        JsonObject respBody = new JsonParser().parse(Objects.requireNonNull(responseEntity.getBody())).getAsJsonObject();

        // {"error":null,"data":{"token":"6a49bb3eb46264010277cd96c49953a9"}}
        OjResponseParam ojResponseParam = gson.fromJson(respBody, OjResponseParam.class);

        List<String> tokenList = new ArrayList<>();
        tokenList.add(ojResponseParam.getData().getToken());
        HttpHeaders postHeaders = new HttpHeaders();

        postHeaders.put("token", tokenList);

        HttpEntity postRequest = new HttpEntity(ojResponseParam.getData(), null);
        ResponseEntity<String> result = restTemplate.postForEntity(OJ_SSO_URL, postRequest, String.class);

        JsonObject userBody = new JsonParser().parse(Objects.requireNonNull(result.getBody())).getAsJsonObject();

        OjResponseParam userInfo = gson.fromJson(userBody, OjResponseParam.class);

        UserParam user = new UserParam();
        user.setUserId(userInfo.getData().getUserId());
        user.setUserName(userInfo.getData().getUserName());
        user.setUserType(userInfo.getData().getUserType());

        return user;
    }

}
